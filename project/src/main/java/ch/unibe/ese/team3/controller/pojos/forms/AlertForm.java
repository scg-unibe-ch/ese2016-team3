package ch.unibe.ese.team3.controller.pojos.forms;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;

/** This form is used when a user wants to create a new alert. */
public class AlertForm {
	
	
	private User user;
	private List<Type> types;

	@NotBlank(message = "Required")
	@Pattern(regexp = "^[0-9]{4} - [-;\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;

	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private Integer price;
	
	private int zipCode;
	
	// Extended Alert Criteria
	private String earliestMoveInDate;
	private String latestMoveInDate;
	
	private boolean elevator;
	private boolean parking;
	private boolean balcony;
	private boolean garage;
	private boolean dishwasher;
	
	private boolean extendedAlert;

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
	
	
	@NotNull(message = "Required")
	private BuyMode buyMode;

	public BuyMode getBuyMode() {
		return buyMode;
	}

	public void setBuyMode(BuyMode buyMode) {
		this.buyMode = buyMode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zip) {
		this.zipCode = zip;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
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

	public boolean isElevator() {
		return elevator;
	}

	public void setElevator(boolean elevator) {
		this.elevator = elevator;
	}

	public boolean isParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}

	public boolean isBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean isGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public boolean isDishwasher() {
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
	
	public boolean isExtendedAlert() {
		return extendedAlert;
	}

	public void setExtendedAlert(boolean extendedAlert) {
		this.extendedAlert = extendedAlert;
	}
	
	
}
