package io.eschmann.zmittag.entities;


import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "members", noClassnameStored = true)
public class Member {

	@Id
	private String id;
	private String name;
	private String email;
	
	public Member() {
		
	}
	
	public Member(final PostedMember member) {
		this.name = member.getMember();
		this.email = member.getEmail();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
