package io.eschmann.zmittag.entities;

import java.io.Serializable;

public class GroupAdd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8106521764366169284L;

	private String member;
	private String group;

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
