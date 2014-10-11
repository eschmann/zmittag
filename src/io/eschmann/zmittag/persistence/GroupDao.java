package io.eschmann.zmittag.persistence;

import io.eschmann.zmittag.entities.Group;

import org.mongodb.morphia.dao.BasicDAO;

public class GroupDao extends BasicDAO<Group, String> {

	private ConnectionManager connectionManager;

	public GroupDao(final ConnectionManager connectionManager) {
		super(Group.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;
	}

}
