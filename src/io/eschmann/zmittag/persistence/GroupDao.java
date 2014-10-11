package io.eschmann.zmittag.persistence;

import io.eschmann.zmittag.entities.Group;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

public class GroupDao extends BasicDAO<Group, String> {

	private ConnectionManager connectionManager;

	public GroupDao(final ConnectionManager connectionManager) {
		super(Group.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;
	}
	
	public Group findOneGroup(final String id) {
		return findOne("_id", this.connectionManager.getObjectId(id));
	}
	
	public UpdateResults addMemberToGroup(final String groupId, final String memberName) {
		final ObjectId oid = this.connectionManager.getObjectId(groupId);
		final Query<Group> query = createQuery().field("_id").equal(oid);
		final UpdateOperations<Group> update = createUpdateOperations().add("members", memberName);
		final UpdateResults result = update(query,  update);
		return result;
	}

}
