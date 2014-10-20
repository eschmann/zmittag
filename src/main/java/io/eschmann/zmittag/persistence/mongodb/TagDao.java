package io.eschmann.zmittag.persistence.mongodb;

import io.eschmann.zmittag.entities.Tag;

import java.util.List;

import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

public class TagDao extends BasicDAO<Tag, String> {

	private ConnectionManager connectionManager;

	public TagDao(final ConnectionManager connectionManager) {
		super(Tag.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;
	}
	
	public List<Tag> orderedList() {
		final Query<Tag> query = createQuery().order("name");
		final List<Tag> foundTags = find(query).asList();
		return foundTags;
	}
	
	public Tag findOneTag(final String id) {
		return findOne("_id", this.connectionManager.getObjectId(id));
	}
	
	public Tag findByName(final String name) {
		return findOne("name", name);
	}
	
	public Tag addTagIfNotExist(final String tagName) {
		final Tag existingTag = findByName(tagName);
		
		if(existingTag != null) {
			return existingTag;
		}
		
		final Tag newTag = new Tag();
		newTag.setName(tagName);
		save(newTag);
		
		return newTag;
	}
}
