package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Member;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.MemberDao;

import java.net.UnknownHostException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("members")
@Stateless
public class MemberService {
	
	private MemberDao memberDao;
	
	public MemberService() {
		try {
			this.memberDao = new MemberDao(new ConnectionManager());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		final List<Member> members = this.memberDao.find().asList();
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(members)).build();
	}
	
}
