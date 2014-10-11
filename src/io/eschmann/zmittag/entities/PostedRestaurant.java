package io.eschmann.zmittag.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class PostedRestaurant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1206243177645002945L;

	private String name;
	private double latitude;
	private double longitude;
	private String searchTag;
	private double maxDistance;
	private long ratingCount;
	private double averageRating;
	private Set<String> tags;
	
	public PostedRestaurant() {
		this.tags = new HashSet<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public String getSearchTag() {
		return searchTag;
	}

	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

	public long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

}
