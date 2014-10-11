package io.eschmann.zmittag.persistence;

import io.eschmann.zmittag.entities.Tag;

import org.mongodb.morphia.dao.BasicDAO;

public class TagDao extends BasicDAO<Tag, String> {

	private ConnectionManager connectionManager;

	public TagDao(final ConnectionManager connectionManager) {
		super(Tag.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;
	}
	
	public Tag findOneTag(final String id) {
		return findOne("_id", this.connectionManager.getObjectId(id));
	}
	
	
}
