package ch.unibe.ese.team3.model;

import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Describes an alert. An alert can be created by a user. If ads matching the
 * criteria of the alert are added to the platform later, the user will be
 * notified.
 */
@Entity
public class Alert {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private User user;

	@Column(nullable = false)
	private int zipcode;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int radius;

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private List<AlertType> alertTypes;
	
	@Enumerated(EnumType.STRING)
	private BuyMode buyMode;
	
	@Column(nullable = true)
	private boolean extendedAlert;
	
	//------------------
	// Extended Alert Criteria
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date earliestMoveInDate;
	
	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date latestMoveInDate;
	
	@Column(nullable = true)
	private boolean elevator;
	
	@Column(nullable = true)
	private boolean parking;
	
	@Column(nullable = true)
	private boolean balcony;
	
	@Column(nullable = true)
	private boolean garage;
	
	@Column(nullable = true)
	private boolean dishwasher;	
	
	@Enumerated(EnumType.STRING)
	private InfrastructureType infrastructureType;
	
	@Column(nullable = true)
	private int squareFootageMin;
	
	@Column(nullable = true)
	private int squareFootageMax;
	
	@Column(nullable = true)
	private int buildYearMin;
	
	@Column(nullable = true)
	private int buildYearMax;
	
	@Column(nullable = true)
	private int renovationYearMin;
	
	@Column(nullable = true)
	private int renovationYearMax;
	
	@Column(nullable = true)
	private int numberOfRoomsMin;
	
	@Column(nullable = true)
	private int numberOfRoomsMax;
	
	@Column(nullable = true)
   	private int numberOfBathMin;
	
	@Column(nullable = true)
   	private int numberOfBathMax;
   	
	@Column(nullable = true)
	private int distanceSchoolMin;
	
	@Column(nullable = true)
	private int distanceSchoolMax;
	
	@Column(nullable = true)
	private int distanceShoppingMin;
	
	@Column(nullable = true)
	private int distanceShoppingMax;
	
	@Column(nullable = true)
	private int distancePublicTransportMin;
	
	@Column(nullable = true)
	private int distancePublicTransportMax;
	
	@Column(nullable = true)
	private int floorLevelMin;
	
	@Column(nullable = true)
	private int floorLevelMax;
   	

	public BuyMode getBuyMode() {
		return buyMode;
	}

	public void setBuyMode(BuyMode buyMode) {
		this.buyMode = buyMode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<AlertType> getAlertTypes() {
		return alertTypes;
	}
	
	public void addAlertType(AlertType type){
		if (!alertTypes.contains(type)){
			this.alertTypes.add(type);
			type.setAlert(this);
		}
	}

	public Date getEarliestMoveInDate() {
		return earliestMoveInDate;
	}

	public void setEarliestMoveInDate(Date earliestMoveInDate) {
		this.earliestMoveInDate = earliestMoveInDate;
	}

	public Date getLatestMoveInDate() {
		return latestMoveInDate;
	}

	public void setLatestMoveInDate(Date latestMoveInDate) {
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
	
	public Alert() {
		this.alertTypes = new ArrayList<AlertType>();
	}
}
