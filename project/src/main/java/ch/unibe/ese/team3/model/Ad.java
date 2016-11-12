package ch.unibe.ese.team3.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ch.unibe.ese.team3.enums.Distance;

/** Describes an advertisement that users can place and search for. */
@Entity
public class Ad {
	
	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private int zipcode;

	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date moveInDate;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int squareFootage;
	
	//new
	@Column(nullable = false)
	private int numberOfRooms;
	
	@Column(nullable = false)
	private int numberOfBath;
	
	@Column(nullable = false)
	private int buildYear;
	
	@Column(nullable = false)
	private int renovationYear;
	
	//new
	@Column(nullable = false)
	private int distanceSchool;
	
	@Column(nullable = false)
	private int distanceShopping;
	
	@Column(nullable = false)
	private int distancePublicTransport;

	@Column(nullable = false)
	@Lob
	private String roomDescription;

	//new
	@Column(nullable = false)
	private boolean elevator;
	
	@Enumerated(EnumType.STRING)
	private InfrastructureType infrastructureType;
	
	@Column(nullable = false)
	private boolean parking;

	@Column(nullable = false)
	private boolean balcony;

	@Column(nullable = false)
	private boolean garage;
	
	@Column(nullable = false)
	private boolean dishwasher;
	
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@Enumerated(EnumType.STRING)
	private BuyMode buyMode;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AdPicture> pictures;
	
	
//	//new
//	@Fetch(FetchMode.SELECT)
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
//	private List<Bid> bids;

	

	@ManyToOne(optional = false)
	private User user;
	
	@ManyToOne(optional = true)
	private User purchaser;

	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Visit> visits;
	
	@Column(nullable = false)
	private int floorLevel;
	
	// auction specific attributes
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(nullable = true)
	private int startPrice;

	@Column(nullable = true)
	private int increaseBidPrice;
	
	@Column(nullable=true)
	private int currentAuctionPrice;
	
	@Column(nullable = false)
	private boolean auction;
	
	// not available if EndDate of auction is passed or 
	//if someone bought the house before the auction ended
	private boolean available = true;	
	
	
	public boolean isAuction() {
		return auction;
	}
	public void setAuction(boolean auction) {
		this.auction = auction;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getcurrentAuctionPrice(){
		return this.currentAuctionPrice;
	}
	public void setcurrentAuctionPrice(int Price){
		this.currentAuctionPrice=Price;
	}
	
	public boolean isAuctionRunning(){
		return available && endDate.after(new Date());
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}
	public int getIncreaseBidPrice() {
		return increaseBidPrice;
	}
	public void setIncreaseBidPrice(int increaseBidPrice) {
		this.increaseBidPrice = increaseBidPrice;
	}
	public void setFloorLevel(int floorLevel) {
		this.floorLevel = floorLevel;
	}
	public int getFloorLevel() {
		return this.floorLevel;
	}

	public InfrastructureType getInfrastructureType() {
		return this.infrastructureType;
	}

	public void setInfrastructureType(InfrastructureType infrastructureType) {
		this.infrastructureType = infrastructureType;
	}

	public void setDistancePublicTransport(int distancePublicTransport) {
		this.distancePublicTransport = distancePublicTransport;
	}



	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getNumberOfBath() {
		return numberOfBath;
	}

	public void setNumberOfBath(int numberOfBath) {
		this.numberOfBath = numberOfBath;
	}

	public int getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
	}

	public int getRenovationYear() {
		return renovationYear;
	}

	public void setRenovationYear(int renovationYear) {
		this.renovationYear = renovationYear;
	}

	public boolean getParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Type getType(){
		return type;
	}
	public void setType(Type type){
		this.type = type;
	}
	
	public BuyMode getBuyMode() {
		return buyMode;
	}
	public void setBuyMode(BuyMode buyMode) {
		this.buyMode = buyMode;
	}
	
	//new
	public boolean getElevator(){
		return elevator;
	}
	public void setElevator(boolean withElevator){
		this.elevator = withElevator;
	}
	

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean hasBalcony) {
		this.balcony = hasBalcony;
	}

	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public Date getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(Date moveInDate) {
		this.moveInDate = moveInDate;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}
	
	//new
	public int getDistanceSchool(){
		return distanceSchool;
	}
	
	public void setDistanceSchool(int distanceToSchool){
		this.distanceSchool = distanceToSchool;
	}
	
	public Distance getDistanceSchoolAsEnum(){
		return Distance.fromInt(distanceSchool);
	}
	
	public int getDistanceShopping(){
		return distanceShopping;
	}
	
	public Distance getDistanceShoppingAsEnum(){
		return Distance.fromInt(distanceShopping);
	}
	
	public void setDistanceShopping(int distanceShopping){
		this.distanceShopping = distanceShopping;
	}
	
	public Distance getDistancePublicTransportAsEnum(){
		return Distance.fromInt(distancePublicTransport);
	}
	
	public int getDistancePublicTransport(){
		return distancePublicTransport;
	}
			
	public String getRoomDescription() {
		return roomDescription;
	}
	
	public String getRoomDescriptionWithLineBreaks(){
		return roomDescription.replaceAll("\\r\\n?|\\n", "<br/>");
	}

	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public List<AdPicture> getPictures() {
		return pictures;
	}

	public void setPictures(List<AdPicture> pictures) {
		this.pictures = pictures;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}
	
	public User getPurchaser() {
		return purchaser;
	}
	public void setPurchaser(User purchaser) {
		this.purchaser = purchaser;
	}
	
	public boolean getDishwasher() {
		return dishwasher;
	}

	public void setDishwasher(boolean dishwasher) {
		this.dishwasher = dishwasher;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	// equals method is defined to check for id only
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ad other = (Ad) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public boolean isPremiumAd() {
		if (this.user != null){
			return this.user.isPremium();
		}
		return false;
	}
}