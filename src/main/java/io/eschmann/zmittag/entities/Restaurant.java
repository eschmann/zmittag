package io.eschmann.zmittag.entities;

import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

@Entity(value = "restaurants", noClassnameStored = true)
public class Restaurant {

	@Id
	private String id;
	private String name;

	private double[] location;
	private long ratingCount;
	private double averageRating;

	private String address;
	private String url;

	private Set<String> tags;

	public Restaurant() {
		this.tags = new HashSet<String>();
		this.averageRating = 3.5d;
		this.ratingCount = 1;
	}

	public Restaurant(final PostedRestaurant postedRestaurant) {
		this();
		this.name = postedRestaurant.getName();
		setLocation(postedRestaurant.getLatitude(),
				postedRestaurant.getLongitude());
		final Set<String> tags = postedRestaurant.getTags();
		if (tags != null && !tags.isEmpty()) {
			this.tags = tags;
		}
		this.address = postedRestaurant.getAddress();
		this.url = postedRestaurant.getUrl();
	}
	
	public Restaurant(final BasicDBObject object) {
		this.name = object.getString("name");
		this.address = object.getString("address");
		this.url = object.getString("url");
		
		final Object location = object.get("location");
		if(location != null) {
			BasicDBList locations = (BasicDBList) location;
			double latitude = (double) locations.get(0);
			double longitude = (double)locations.get(1);
			setLocation(latitude, longitude);
		}
		
		this.averageRating = object.getDouble("averageRating");
		
		final BasicDBList tags = (BasicDBList)object.get("tags");
		if(tags != null) {
			this.tags = new HashSet<String>();
			for(Object tag : tags) {
				this.tags.add(String.valueOf(tag));
			}
		}
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

	public double[] getLocation() {
		if (this.location == null) {
			this.location = new double[] { 0.0d, 0.0d };
		}
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public void setLocation(double latitude, double longitude) {
		this.location = new double[] { latitude, longitude };
	}

	public long getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(long ratinCount) {
		this.ratingCount = ratinCount;
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

	public void addTag(final String tag) {
		this.tags.add(tag);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
