package io.eschmann.zmittag.entities;

import java.io.Serializable;
import java.util.Date;

public class PostedGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5695246269381650046L;
	private String name;
	private String member;
	private String email;
	private long date;
	
	public PostedGroup() {
		this.date = new Date().getTime();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
