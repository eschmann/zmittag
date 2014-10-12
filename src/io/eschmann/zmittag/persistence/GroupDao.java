package io.eschmann.zmittag.persistence;

import io.eschmann.zmittag.Constants;
import io.eschmann.zmittag.entities.Group;

import java.util.Date;
import java.util.List;

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
	
	public List<Group> findActiveGroupRestaurants() {
		long leastDate = new Date().getTime() - Constants.DAY_IN_MILLIS;
		final Query<Group> query = createQuery().field("date").greaterThan(leastDate);
		final List<Group> foundGroups = find(query).asList();
		return foundGroups;
	}
	
	public List<Group> findMemberGroups(final String memberName) {
		final Query<Group> query = createQuery().field("members").contains(memberName);
		final List<Group> foundGroups = find(query).asList();
		return foundGroups;
	}
	
	public UpdateResults addMemberToGroup(final String groupId, final String memberName) {
		final ObjectId oid = this.connectionManager.getObjectId(groupId);
		final Query<Group> query = createQuery().field("_id").equal(oid);
		final UpdateOperations<Group> update = createUpdateOperations().add("members", memberName);
		final UpdateResults result = update(query,  update);
		return result;
	}
	
	public UpdateResults removeMemberFromGroup(final String groupId, final String memberName) {
		final ObjectId oid = this.connectionManager.getObjectId(groupId);
		final Query<Group> query = createQuery().field("_id").equal(oid);
		final UpdateOperations<Group> update = createUpdateOperations().removeAll("members", memberName);
		final UpdateResults result = update(query,  update);
		return result;
	}
}
