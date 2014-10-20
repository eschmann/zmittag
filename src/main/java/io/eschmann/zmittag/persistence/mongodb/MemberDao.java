package io.eschmann.zmittag.persistence.mongodb;

import io.eschmann.zmittag.entities.Member;

import org.mongodb.morphia.dao.BasicDAO;

public class MemberDao extends BasicDAO<Member, String> {

	private ConnectionManager connectionManager;

	public MemberDao(final ConnectionManager connectionManager) {
		super(Member.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;
	}

	public Member findOneMember(final String id) {
		return findOne("_id", this.connectionManager.getObjectId(id));
	}
	
	public Member findOneByName(final String name) {
		return findOne("name", name);
	}
	
	public Member addMemberIfNotExist(final String name, final String email) {
		final Member existingMember = findOneByName(name);
		if(existingMember != null) {
			return existingMember;
		}
		
		final Member newMember = new Member();
		newMember.setName(name);
		newMember.setEmail(email);
		save(newMember);
		return newMember;
		
	}
}
