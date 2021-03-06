package ch.unibe.ese.team3.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ch.unibe.ese.team3.model.enums.AuctionStatus;
import ch.unibe.ese.team3.model.enums.Distance;

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

	@Column
	private Double longitude;
	@Column
	private Double latitude;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date moveInDate;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int auctionPrice;

	@Column(nullable = false)
	private int squareFootage;

	// new
	@Column(nullable = false)
	private int numberOfRooms;

	@Column(nullable = false)
	private int numberOfBath;

	@Column(nullable = false)
	private int buildYear;

	@Column(nullable = false)
	private int renovationYear;

	// new
	@Column(nullable = false)
	private int distanceSchool;

	@Column(nullable = false)
	private int distanceShopping;

	@Column(nullable = false)
	private int distancePublicTransport;

	@Column(nullable = false)
	@Lob
	private String roomDescription;

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
	@OneToMany(mappedBy = "ad",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<AdPicture> pictures;

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "ad", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Bid> bids;

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "triggerAd", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<AlertResult> alertResults;


	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<PurchaseRequest> purchaseRequests;


	@ManyToOne(optional = false)
	private User user;

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

	@Column(nullable = true)
	private int currentAuctionPrice;

	@Column(nullable = false)
	private boolean auction;

	@Column(nullable = true)
	private boolean availableForAuction;

	@Column(nullable = true)
	private boolean auctionCompleted;
	
	private boolean auctionMessageSent;
	
	public List<Bid> getBids() {
		return bids;
	}

	public void addBid(Bid bid){
		if (!bids.contains(bid)){
			bids.add(bid);
			bid.setAd(this);
		}
	}
	
	public void removeBid(Bid bid){
		bids.remove(bid);
		bid.setAd(null);
	}
	
	public List<PurchaseRequest> getPurchaseRequests() {
		return purchaseRequests;
	}
	
	public void addPurchaseRequest(PurchaseRequest request){
		if (!this.purchaseRequests.contains(request)){
			this.purchaseRequests.add(request);
			request.setAd(this);
		}
	}
	
	public void removePurchaseRequest(PurchaseRequest request){
		this.purchaseRequests.remove(request);
		request.setAd(null);
	}
	

	public boolean isAuctionMessageSent() {
		return auctionMessageSent;
	}

	public void setAuctionMessageSent(boolean auctionMessageSent) {
		this.auctionMessageSent = auctionMessageSent;
	}

	public boolean isAuction() {
		return auction;
	}

	public void setAuction(boolean auction) {
		this.auction = auction;
	}

	public boolean isAvailableForAuction() {
		return availableForAuction;
	}

	public void setAvailableForAuction(boolean available) {
		this.availableForAuction = available;
	}

	public boolean isAuctionCompleted() {
		return auctionCompleted;
	}

	public void setAuctionCompleted(boolean auctionCompleted) {
		this.auctionCompleted = auctionCompleted;
	}

	public int getCurrentAuctionPrice() {
		return this.currentAuctionPrice;
	}

	public void setcurrentAuctionPrice(int Price) {
		this.currentAuctionPrice = Price;
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

	public int getAuctionPrice() {
		return auctionPrice;
	}

	public void setAuctionPrice(int price) {
		this.auctionPrice = price;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public BuyMode getBuyMode() {
		return buyMode;
	}

	public void setBuyMode(BuyMode buyMode) {
		this.buyMode = buyMode;
	}

	public boolean getElevator() {
		return elevator;
	}

	public void setElevator(boolean withElevator) {
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

	public int getDistanceSchool() {
		return distanceSchool;
	}

	public void setDistanceSchool(int distanceToSchool) {
		this.distanceSchool = distanceToSchool;
	}

	public Distance getDistanceSchoolAsEnum() {
		return Distance.fromInt(distanceSchool);
	}

	public int getDistanceShopping() {
		return distanceShopping;
	}

	public Distance getDistanceShoppingAsEnum() {
		return Distance.fromInt(distanceShopping);
	}

	public void setDistanceShopping(int distanceShopping) {
		this.distanceShopping = distanceShopping;
	}

	public Distance getDistancePublicTransportAsEnum() {
		return Distance.fromInt(distancePublicTransport);
	}

	public int getDistancePublicTransport() {
		return distancePublicTransport;
	}

	public String getRoomDescription() {
		return roomDescription;
	}

	public String getRoomDescriptionWithLineBreaks() {
		return roomDescription.replaceAll("\\r\\n?|\\n", "<br/>");
	}

	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}

	public List<AdPicture> getPictures() {
		return pictures;
	}
	
	public void addPicture(AdPicture picture){
		if (!this.pictures.contains(picture)){
			this.pictures.add(picture);
			picture.setAd(this);
		}
	}
	
	public void removePicture(AdPicture picture){
		this.pictures.remove(picture);
		picture.setAd(this);
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
	
	public void addVisit(Visit visit){
		if (!visits.contains(visit)){
			this.visits.add(visit);
			visit.setAd(this);
		}
	}
	
	public void removeVisit(Visit visit){
		this.visits.remove(visit);
		visit.setAd(null);
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
		if (this.user != null) {
			return this.user.isPremium();
		}
		return false;
	}

	public Ad() {
		this.bids = new ArrayList<Bid>();
		this.purchaseRequests = new ArrayList<PurchaseRequest>();
		this.visits = new ArrayList<Visit>();
		this.pictures = new ArrayList<AdPicture>();
		this.alertResults = new ArrayList<AlertResult>();
		this.auction = false;
		this.auctionCompleted = false;
		this.availableForAuction = true;
	}

	public boolean isAuctionStopped() {
		Date now = new Date();
		return !auctionCompleted && !availableForAuction && now.after(startDate) && now.before(endDate);
	}

	public boolean hasAuctionExpired() {
		Calendar now = Calendar.getInstance();
		Calendar expired = Calendar.getInstance();
		expired.setTime(endDate);
		expired.set(Calendar.HOUR_OF_DAY, 23);
		expired.set(Calendar.MINUTE, 59);
		expired.set(Calendar.SECOND, 59);
		

		return !auctionCompleted && availableForAuction && now.after(expired);

	}

	public boolean isAuctionRunning() {
		Date now = new Date();
		Calendar calNow = Calendar.getInstance();
		Calendar expired = Calendar.getInstance();
		expired.setTime(endDate);
		expired.set(Calendar.HOUR_OF_DAY, 23);
		expired.set(Calendar.MINUTE, 59);
		expired.set(Calendar.SECOND, 59);
		
		return !auctionCompleted && availableForAuction && now.after(startDate) && calNow.before(expired);
	}

	public boolean isAuctionNotYetRunning() {
		Date now = new Date();
		return !auctionCompleted && availableForAuction && now.before(startDate);
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude.doubleValue();
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatLongNull() {
		this.longitude = null;
		this.latitude = null;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude.doubleValue();
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public AuctionStatus getAuctionStatus() {
		if (isAuctionStopped()) {
			return AuctionStatus.Stopped;
		}
		if (hasAuctionExpired()) {
			return AuctionStatus.Expired;
		}
		if (isAuctionRunning()) {
			return AuctionStatus.Running;
		}
		if (isAuctionNotYetRunning()) {
			return AuctionStatus.NotYetRunning;
		}
		if (isAuctionCompleted()) {
			return AuctionStatus.Completed;
		}

		return AuctionStatus.NoAuction;
	}

	public List<AlertResult> getAlertResults() {
		return this.alertResults;
	}
}