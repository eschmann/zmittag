package io.eschmann.zmittag.entities;

import java.io.Serializable;

public class PostedRating implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5844579097084897651L;
	private double rating;

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

}
