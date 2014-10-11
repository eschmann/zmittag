package io.eschmann.zmittag.entities;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity(value = "groups", noClassnameStored = true)
public class Group {

	@Id
	private String id;
	private String name;
	private long date;
	private Set<String> members;

	@Transient
	private Set<Member> memberList;

	@Transient
	private double[] location;

	public Group() {
		this.members = new HashSet<String>();
	}

	public Group(final PostedGroup newGroup) {
		this();
		this.name = newGroup.getName();
		this.members.add(newGroup.getMember());
		this.date = newGroup.getDate();
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

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		if (members == null) {
			this.members = new HashSet<String>();
		}
		this.members = members;
	}

	public void addMember(final String name) {
		if (StringUtils.isNotBlank(name)) {
			this.members.add(name);
		}
	}

	public Set<Member> getMemberList() {
		return memberList;
	}

	public void setMemberList(Set<Member> memberList) {
		this.memberList = memberList;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

}
