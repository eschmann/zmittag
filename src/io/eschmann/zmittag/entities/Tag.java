package io.eschmann.zmittag.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "tags", noClassnameStored = true)
public class Tag {

	@Id
	private String id;
	private String name;
	
	public Tag() {
		
	}
	
	public Tag(final PostedTag newTag) {
		this.name = newTag.getName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
