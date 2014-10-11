package io.eschmann.zmittag.entities;

import java.io.Serializable;

public class PostedTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3005594867763104108L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
