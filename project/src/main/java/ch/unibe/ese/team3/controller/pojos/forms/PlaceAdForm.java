package ch.unibe.ese.team3.controller.pojos.forms;
 
import java.util.List;

import javax.validation.constraints.AssertTrue;
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
	
	@Pattern(regexp = "^[0-9]{4} - [\\S]*", message = "Please pick a city from the list")
	private String city;
	
	@NotBlank(message = "Required")
	private String moveInDate;

	private int price;

	@Min(value = 1, message = "Has to be equal to 1 or more")
	private int squareFootage;
	
	@Min(value=0, message ="Has to be equal to 1700 or more")
	private int buildYear;

	@Min(value=0, message ="Has to be equal to 1900 or more")
	private int renovationYear;
	
    @Min(value=1, message ="Has to be equal to 1 or more")
	private int numberOfRooms;
    
    @Min(value=0, message ="Has to be equal to 1 or more")
   	private int numberOfBath;
	
    @NotBlank(message = "Required")
	private String roomDescription;
    
	private int distanceSchool;
	private int distanceShopping;
	private int distancePublicTransport;
	private int floorLevel;
	
	private InfrastructureType infrastructureType;
	private Type type;
	
	// validation attributes
	
	@AssertTrue(message = "Required")
	private boolean validStartDate;
	
	@AssertTrue(message = "Required")
	private boolean validEndDate;
	
	@AssertTrue(message = "Must be greater than zero")
	private boolean validStartPrice;
	
	@AssertTrue(message = "Must be greater than zero")
	private boolean validIncreaseBidPrice;
	
	@AssertTrue(message = "Has to be equal to 1 or more")
	private boolean validPrice;
	
	// auction specific attributes
	private int auctionPrice;	
	private String startDate;
	private String endDate;
	private int startPrice;
	private int increaseBidPrice;
	private boolean auction;
	
	// optional for input
	private boolean parking;
	private boolean elevator;
	private boolean balcony;
	private boolean garage;
	private boolean dishwasher;
		
		
	public boolean isValidPrice() {
		return validPrice;
	}

	public void setValidPrice(boolean validPrice) {
		this.validPrice = validPrice;
	}

	public boolean isValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(boolean validStartDate) {
		this.validStartDate = validStartDate;
	}

	public boolean isValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(boolean validEndDate) {
		this.validEndDate = validEndDate;
	}

	public boolean isValidStartPrice() {
		return validStartPrice;
	}

	public void setValidStartPrice(boolean validStartPrice) {
		this.validStartPrice = validStartPrice;
	}

	public boolean isValidIncreaseBidPrice() {
		return validIncreaseBidPrice;
	}

	public void setValidIncreaseBidPrice(boolean validIncreaseBidPrice) {
		this.validIncreaseBidPrice = validIncreaseBidPrice;
	}
	
	public int getAuctionPrice(){
		return auctionPrice;
	}
	
	public void setAuctionPrice(int price){
		auctionPrice = price;
		validate();
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		validate();
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
		validate();
	}

	public int getIncreaseBidPrice() {
		return increaseBidPrice;
	}

	public void setIncreaseBidPrice(int increaseBidPrice) {
		this.increaseBidPrice = increaseBidPrice;
		validate();
	}

	public boolean getAuction() {
		return auction;
	}

	public void setAuction(boolean auction) {
		this.auction = auction;
		validate();
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
		validate();
	}

	public String getRoomDescription() {
		return roomDescription;
	}

	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public int getSquareFootage() {
		return squareFootage;
	}
	
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
	public boolean isElevator(){		
		return elevator;
	}
	public void setElevator(boolean elevator){		
		this.elevator = elevator;
	}

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
		validate();
	}
	
	public int getPriceForAd(){
		if (auction){
			return this.startPrice;
		}
		return price;
	}
	
	private void validate(){
		if (!auction){
			validEndDate = true;
			validStartDate = true;
			validStartPrice = true;
			validIncreaseBidPrice = true;
			validPrice = (price > 0);
		}
		else {
			validStartDate = (startDate != null && !startDate.isEmpty());
			validEndDate =(endDate != null && !endDate.isEmpty());
			validIncreaseBidPrice = increaseBidPrice > 0;
			validStartPrice = startPrice > 0;
			validPrice = (auctionPrice > 0);
		}
	}
}
