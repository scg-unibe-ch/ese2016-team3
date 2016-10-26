package ch.unibe.ese.team3.controller.pojos.forms;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;

/** This form is used when a user wants to place a new ad. */
public class PlaceAdForm {
	
	@NotBlank(message = "Required")
	private String title;
	
	@NotBlank(message = "Required")
	private String street;
	
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;
	
	@NotBlank(message = "Required")
	private String moveInDate;
	
	private String moveOutDate;

	@Min(value = 1, message = "Has to be equal to 1 or more")
	private int prize;

	@Min(value = 1, message = "Has to be equal to 1 or more")
	private int squareFootage;
	//new
	@Min(value=1700, message ="Has to be equal to 1700 or more")
	private int buildYear;

	@Min(value=1900, message ="Has to be equal to 1900 or more")
	private int renovationYear;
	
    @Min(value=1, message ="Has to be equal to 1 or more")
	private int numberOfRooms;
    
    @Min(value=1, message ="Has to be equal to 1 or more")
   	private int numberOfBath;
	
	//new
	@Min(value = 1, message ="Has to be equal to 1 or more")
	private int distanceSchool;
	@Min(value = 1, message ="Has to be equal to 1 or more")
	private int distanceShopping;
	@Min(value = 1, message ="Has to be equal to 1 or more")
	private int distancePublicTransport;
	@Min(value = 0, message ="Has to be equal to 0 or more")
	private int floorLevel;
	
	// new
	private InfrastructureType infrastructureType;
	private Type type;

	@NotBlank(message = "Required")
	private String roomDescription;

	private String preferences;
	
	
	// auction specific attributes
	
	private String startDate;
	private String endDate;
	private int startPrice;
	private int increaseBidPrice;
	private int buyItNowPrice;
	
	
	// optional for input
	private boolean parking;
	private boolean elevator;
	private boolean balcony;
	private boolean garage;
	private boolean dishwasher;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getIncreaseBidPrice() {
		return increaseBidPrice;
	}

	public void setIncreaseBidPrice(int increaseBidPrice) {
		this.increaseBidPrice = increaseBidPrice;
	}

	public int getBuyItNowPrice() {
		return buyItNowPrice;
	}

	public void setBuyItNowPrice(int buyItNowPrice) {
		this.buyItNowPrice = buyItNowPrice;
	}

	public boolean getDishwasher() {
		return dishwasher;
	}

	public void setDishwasher(boolean dishwasher) {
		this.dishwasher = dishwasher;
	}

	private List<String> visits;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public String getRoomDescription() {
		return roomDescription;
	}

	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public int getSquareFootage() {
		return squareFootage;
	}
	//new
	public int getBuildYear(){
		return buildYear;
	}
	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
	}
	public int getRenovationYear(){
		return renovationYear;
	}
	public void setRenovationYear(int renovationyear){
		this.renovationYear = renovationyear;
	}
	
	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	//new
	public int getDistanceSchool(){
		return distanceSchool;
	}
	public void setDistanceSchool(int distanceToSchool){
		this.distanceSchool = distanceToSchool;
	}
	public int getDistanceShopping(){
		return distanceShopping;
	}
	public void setDistanceShopping(int distanceShopping){
		this.distanceShopping = distanceShopping;
	}
	public int getDistancePublicTransport(){
		return distancePublicTransport;
	}
	public void setDistancePublicTransport(int distancePublicTransport) {
		this.distancePublicTransport = distancePublicTransport;
	}
	

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}
	public boolean isElevator(){		//new
		return elevator;
	}
	public void setElevator(boolean elevator){		//new
		this.elevator = elevator;
	}
	
	// new
	public int getNumberOfBath() {
		return numberOfBath;
	}

	public void setNumberOfBath(int numberOfBath) {
		this.numberOfBath = numberOfBath;
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

	public String getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(String moveInDate) {
		this.moveInDate = moveInDate;
	}

	public String getMoveOutDate() {
		return moveOutDate;
	}

	public void setMoveOutDate(String moveOutDate) {
		this.moveOutDate = moveOutDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Type getType(){
		return type;
	}
	public void setType(Type type){
		this.type = type;
	}

	public List<String> getVisits() {
		return visits;
	}

	public void setVisits(List<String> visits) {
		this.visits = visits;
	}

	public boolean isParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}
	
	public InfrastructureType getInfrastructureType() {
		return infrastructureType;
	}
	public void setInfrastructureType(InfrastructureType infrastructureType) {
		this.infrastructureType = infrastructureType;
	}
	public void setFloorLevel(int floorLevel) {
		this.floorLevel = floorLevel;
	}
	public int getFloorLevel() {
		return this.floorLevel;
	}

	public int getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}
}
