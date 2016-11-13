package ch.unibe.ese.team3.controller.pojos.forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;

/** This form is used for searching for an ad. */
public class SearchForm {

	@NotBlank(message = "Required")
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private Integer price;

	private Type[] types;
	
	private String earliestMoveInDate;
	private String latestMoveInDate;
	
	private boolean elevator;
	private boolean parking;
	private boolean balcony;
	private boolean garage;
	private boolean dishwasher;

	private boolean roomHelper;
	
	// new

	private InfrastructureType infrastructureType;
	private int squareFootageMin;
	private int squareFootageMax;
	private int buildYearMin;
	private int buildYearMax;
	private int renovationYearMin;
	private int renovationYearMax;
	private int numberOfRoomsMin;
	private int numberOfRoomsMax;
   	private int numberOfBathMin;
   	private int numberOfBathMax;
	
	
	//new
	private int distanceSchoolMin;
	private int distanceSchoolMax;
	private int distanceShoppingMin;
	private int distanceShoppingMax;
	private int distancePublicTransportMin;
	private int distancePublicTransportMax;
	private int floorLevelMin;
	private int floorLevelMax;

	// the ugly stuff
	private boolean studioHelper; // to remove?

	public int getSquareFootageMin() {
		return squareFootageMin;
	}

	public void setSquareFootageMin(int squareFootageMin) {
		this.squareFootageMin = squareFootageMin;
	}

	public int getSquareFootageMax() {
		return squareFootageMax;
	}

	public void setSquareFootageMax(int squareFootageMax) {
		this.squareFootageMax = squareFootageMax;
	}

	public int getBuildYearMin() {
		return buildYearMin;
	}

	public void setBuildYearMin(int buildYearMin) {
		this.buildYearMin = buildYearMin;
	}

	public int getBuildYearMax() {
		return buildYearMax;
	}

	public void setBuildYearMax(int buildYearMax) {
		this.buildYearMax = buildYearMax;
	}

	public int getRenovationYearMin() {
		return renovationYearMin;
	}

	public void setRenovationYearMin(int renovationYearMin) {
		this.renovationYearMin = renovationYearMin;
	}

	public int getRenovationYearMax() {
		return renovationYearMax;
	}

	public void setRenovationYearMax(int renovationYearMax) {
		this.renovationYearMax = renovationYearMax;
	}

	public int getNumberOfRoomsMin() {
		return numberOfRoomsMin;
	}

	public void setNumberOfRoomsMin(int numberOfRoomsMin) {
		this.numberOfRoomsMin = numberOfRoomsMin;
	}

	public int getNumberOfRoomsMax() {
		return numberOfRoomsMax;
	}

	public void setNumberOfRoomsMax(int numberOfRoomsMax) {
		this.numberOfRoomsMax = numberOfRoomsMax;
	}

	public int getNumberOfBathMin() {
		return numberOfBathMin;
	}

	public void setNumberOfBathMin(int numberOfBathMin) {
		this.numberOfBathMin = numberOfBathMin;
	}

	public int getNumberOfBathMax() {
		return numberOfBathMax;
	}

	public void setNumberOfBathMax(int numberOfBathMax) {
		this.numberOfBathMax = numberOfBathMax;
	}

	public int getDistanceSchoolMin() {
		return distanceSchoolMin;
	}

	public void setDistanceSchoolMin(int distanceSchoolMin) {
		this.distanceSchoolMin = distanceSchoolMin;
	}

	public int getDistanceSchoolMax() {
		return distanceSchoolMax;
	}

	public void setDistanceSchoolMax(int distanceSchoolMax) {
		this.distanceSchoolMax = distanceSchoolMax;
	}

	public int getDistanceShoppingMin() {
		return distanceShoppingMin;
	}

	public void setDistanceShoppingMin(int distanceShoppingMin) {
		this.distanceShoppingMin = distanceShoppingMin;
	}

	public int getDistanceShoppingMax() {
		return distanceShoppingMax;
	}

	public void setDistanceShoppingMax(int distanceShoppingMax) {
		this.distanceShoppingMax = distanceShoppingMax;
	}

	public int getDistancePublicTransportMin() {
		return distancePublicTransportMin;
	}

	public void setDistancePublicTransportMin(int distancePublicTransportMin) {
		this.distancePublicTransportMin = distancePublicTransportMin;
	}

	public int getDistancePublicTransportMax() {
		return distancePublicTransportMax;
	}

	public void setDistancePublicTransportMax(int distancePublicTransportMax) {
		this.distancePublicTransportMax = distancePublicTransportMax;
	}

	public int getFloorLevelMin() {
		return floorLevelMin;
	}

	public void setFloorLevelMin(int floorLevelMin) {
		this.floorLevelMin = floorLevelMin;
	}

	public int getFloorLevelMax() {
		return floorLevelMax;
	}

	public void setFloorLevelMax(int floorLevelMax) {
		this.floorLevelMax = floorLevelMax;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	
	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public boolean getElevator() {
		return elevator;
	}

	public void setElevator(boolean elevator) {
		this.elevator = elevator;
	}

	public boolean getParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}


	public String getEarliestMoveInDate() {
		return earliestMoveInDate;
	}

	public void setEarliestMoveInDate(String earliestMoveInDate) {
		this.earliestMoveInDate = earliestMoveInDate;
	}

	public String getLatestMoveInDate() {
		return latestMoveInDate;
	}

	public void setLatestMoveInDate(String latestMoveInDate) {
		this.latestMoveInDate = latestMoveInDate;
	}

	public boolean getStudioHelper() {
		return studioHelper;
	}

	public void setStudioHelper(boolean helper) {
		this.studioHelper = helper;
	}

	public boolean getRoomHelper() {
		return roomHelper;
	}

	public void setRoomHelper(boolean helper) {
		this.roomHelper = helper;
	}

	public Type[] getTypes() {
		return types;
	}

	public void setTypes(Type[] types) {
		this.types = types;
	}

	public boolean getDishwasher() {
		return dishwasher;
	}

	public void setDishwasher(boolean dishwasher) {
		this.dishwasher = dishwasher;
	}
	public InfrastructureType getInfrastructureType() {
		return infrastructureType;
	}

	public void setInfrastructureType(InfrastructureType infrastructureType) {
		this.infrastructureType = infrastructureType;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

}