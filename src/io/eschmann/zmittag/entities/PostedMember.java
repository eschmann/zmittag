package io.eschmann.zmittag.entities;

import java.io.Serializable;

public class PostedMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8106521764366169284L;

	private String member;
	private String email;
	private String group;

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
