package io.eschmann.zmittag.api;

import io.eschmann.zmittag.Constants;
import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.entities.Member;
import io.eschmann.zmittag.entities.PostedGroup;
import io.eschmann.zmittag.entities.PostedMember;
import io.eschmann.zmittag.entities.Restaurant;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.GroupDao;
import io.eschmann.zmittag.persistence.MemberDao;
import io.eschmann.zmittag.persistence.RestaurantDao;
import io.eschmann.zmittag.service.ServiceHelper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.tuple.Pair;
import org.mongodb.morphia.query.UpdateResults;

@Path("groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class GroupService {

	private GroupDao groupDao;
	private MemberDao memberDao;
	private RestaurantDao restaurantDao;

	public GroupService() {
		try {
			final ConnectionManager connectionManager = new ConnectionManager();
			this.groupDao = new GroupDao(connectionManager);
			this.memberDao = new MemberDao(connectionManager);
			this.restaurantDao = new RestaurantDao(connectionManager);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@GET
	public String index() {
		return "{\"type\": \"Group\"}";
	}

	@GET
	@Path("list")
	public Response list() {
		final List<Group> groups = this.groupDao.find().asList();

		long now = new Date().getTime();

		final List<Group> todayGroups = new ArrayList<Group>();

		for (Group group : groups) {
			if ((now - group.getDate()) > Constants.DAY_IN_MILLIS) {
				continue;
			}

			todayGroups.add(group);

			final Set<String> names = group.getMembers();
			final Set<Member> memberList = new HashSet<Member>();
			for (String name : names) {
				final Member foundMember = this.memberDao.findOneByName(name);
				if (foundMember != null) {
					memberList.add(foundMember);
				}
			}
			if (!memberList.isEmpty()) {
				group.setMemberList(memberList);
			}

			final Restaurant restaurant = this.restaurantDao.findByName(group
					.getName());
			if (restaurant != null) {
				group.setLocation(restaurant.getLocation());
			}
		}
		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(todayGroups)).build();
	}

	@POST
	@Path("add")
	public Response add(PostedGroup newGroup) {
		
		final String memberName = newGroup.getMember();
		removeMemberFromOtherGroups(memberName);

		final Group group = new Group(newGroup);

		this.groupDao.save(group);
		final Member member = this.memberDao.addMemberIfNotExist(
				memberName, newGroup.getEmail());
		
		final Pair<Group, Member> pair = Pair.of(group, member);

		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(pair)).build();
	}

	@POST
	@Path("{id}/join")
	public Response join(@PathParam("id") String groupId, PostedMember groupAdd) {
		final String memberName = groupAdd.getMember();

		removeMemberFromOtherGroups(memberName);
		
		final UpdateResults result = this.groupDao.addMemberToGroup(groupId,
				memberName);

		final Member member = this.memberDao.addMemberIfNotExist(memberName,
				groupAdd.getEmail());

		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(member)).build();
	}

	@POST
	@Path("{id}/leave")
	public Response leave(@PathParam("id") String groupId, PostedMember groupAdd) {
		final UpdateResults result = this.groupDao.removeMemberFromGroup(
				groupId, groupAdd.getMember());

		return ServiceHelper.createOkResponseBuilder().build();
	}

	@GET
	@Path("{id}/members")
	public Response listMembers(@PathParam("id") String groupId) {
		final Group foundGroup = this.groupDao.findOneGroup(groupId);
		final Set<String> members = foundGroup == null ? new HashSet<String>()
				: foundGroup.getMembers();

		List<Member> groupMembers = new ArrayList<Member>();
		for (String member : members) {
			final Member foundMember = this.memberDao.findOne("name", member);
			if (foundMember != null) {
				groupMembers.add(foundMember);
			}
		}

		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(groupMembers)).build();
	}

	private void removeMemberFromOtherGroups(final String memberName) {
		final List<Group> memberGroups = this.groupDao
				.findMemberGroups(memberName);
		
		if (memberGroups != null) {
			for (Group group : memberGroups) {
				this.groupDao.removeMemberFromGroup(group.getId(), memberName);
			}
		}
	}
}
