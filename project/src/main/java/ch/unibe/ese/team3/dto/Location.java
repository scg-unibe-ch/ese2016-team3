package ch.unibe.ese.team3.dto;

/**
 * Represents a zip code and the data that belongs to it. Is not marked as
 * entity, since it is not used in Hibernate.
 */
public class Location {

	private int zip;
	private String city;
	private double latitude;
	private double longitude;
	private String department;

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
	
	public String getDepartment(){
		return this.department;
	}
	
	public void setDepartment(String department){
		this.department = department;
	}

}
