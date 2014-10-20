package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Member;
import io.eschmann.zmittag.entities.PostedMember;
import io.eschmann.zmittag.persistence.mongodb.ConnectionManager;
import io.eschmann.zmittag.persistence.mongodb.MemberDao;
import io.eschmann.zmittag.service.ServiceHelper;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("members")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MemberResource {
	
	private MemberDao memberDao;
	
	public MemberResource(final ConnectionManager connectionManager) {
		this.memberDao = new MemberDao(connectionManager);
	}
	
	@GET
	@Path("list")
	public Response list() {
		final List<Member> members = this.memberDao.find().asList();
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(members)).build();
	}
	
	@POST
	@Path("add") 
	public Response add(PostedMember postedMember) {
		final Member member = new Member(postedMember);
		this.memberDao.save(member);
		
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(member)).build();
	}
	
}
