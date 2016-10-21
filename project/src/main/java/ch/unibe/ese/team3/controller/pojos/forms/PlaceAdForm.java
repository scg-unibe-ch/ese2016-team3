package ch.unibe.ese.team3.controller.pojos.forms;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

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
	
	public int getNumberOfBath() {
		return numberOfBath;
	}

	public void setNumberOfBath(int numberOfBath) {
		this.numberOfBath = numberOfBath;
	}

	//new
	@Min(value = 1, message ="Has to be equal to 1 or more")
	private int distanceSchool;
	//new
	@Min(value = 1, message ="Has to be equal to 1 or more")
	private int distanceShopping;
	//new
	@Min(value = 1, message ="Has to be equal to 1 or more")
	private int distancePublicTransport;
	
	
	@NotBlank(message = "Required")
	private String roomDescription;

	private String preferences;
	
	//@NotBlank(message = "Required")
	//private int floorlevel;			//only for appartment
	
	
	
	//@NotBlank(message = "Required")
	//private String artInternetanschluss;
	
	// optional for input
	private String roomFriends;
	
	//müssen wir wegnehmen
	//und neu Type typ = studio, house, flat,etc...
	//true if studio, false if room
	private boolean studio;
	
	
	private Type type;
	
	//private boolean 
	//private boolean dishwasher;		//only for renting
	private boolean parking;
	private boolean elevator;
	private boolean smokers;
	private boolean animals; //weg
	private boolean garden; //weg
	private boolean balcony;
	private boolean cellar;	//weg
	private boolean furnished;
	private boolean cable;
	private boolean garage;
	private boolean internet; // würde ersetzt werden
	
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
	public boolean isSmokers() {
		return smokers;
	}

	public void setSmokers(boolean smoker) {
		this.smokers = smoker;
	}

	public boolean isAnimals() {
		return animals;
	}

	public void setAnimals(boolean animals) {
		this.animals = animals;
	}
	
	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	
	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean cellar) {
		this.cellar = cellar;
	}
	
	public boolean isFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean getCable() {
		return cable;
	}

	public void setCable(boolean cable) {
		this.cable = cable;
	}
	
	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public boolean getInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
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

	public String getRoomFriends() {
		return roomFriends;
	}

	public void setRoomFriends(String roomFriends) {
		this.roomFriends = roomFriends;
	}
	
	
	//will be removed
	public boolean getStudio() {
		return studio;
	}
	
	public void setStudio(boolean studio) {
		this.studio = studio;
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
}
