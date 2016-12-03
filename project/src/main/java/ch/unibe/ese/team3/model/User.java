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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** Describes a user on the platform. */
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private Gender gender;

	@Column(nullable = false)
	private boolean enabled;
	
	@Column(nullable = true)
	private boolean isGoogleUser;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@Enumerated(EnumType.STRING)
	private CreditcardType creditcardType;
	
	@Column(nullable = true)
	private String creditCard;
	
	@Column(nullable = true)
	private String securityNumber;
	
	@Column(nullable = true)
	private String expirationMonth;
	
	@Column(nullable = true)
	private String expirationYear;
	
	@Column(nullable = true)
	private String creditcardName;
	
	@Column(nullable = true)
	private Date premiumExpiryDate;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.MERGE)
	private PremiumChoice premiumChoice;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserRole> userRoles;

	@Column(nullable = true)
	private String googlePicture;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private UserPicture picture;
	
	@Column(nullable = true)
	@Lob
	private String aboutMe;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Ad> bookmarkedAds;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "bidder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private List<Bid> bids;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "purchaser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private List<PurchaseRequest> purchaseRequests;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AlertResult> alertResults;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Message> sentMessages;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Message> receivedMessages;
	
	@ManyToMany(mappedBy = "visitors", fetch = FetchType.EAGER)
	private List<Visit> visits;
	
	@OneToMany(mappedBy = "sender", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<VisitEnquiry> enquiries;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public boolean getIsGoogleUser(){
		return this.isGoogleUser;
	}
	
	public void setIsGoogleUser(boolean googleUser){
		this.isGoogleUser = googleUser;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public AccountType getAccountType(){
		return accountType;
	}
	
	public void setAccountType(AccountType type){
		accountType = type;
	}
	
	public CreditcardType getCreditcardType(){
		return creditcardType;
	}
	
	public void setCreditcardType(CreditcardType type){
		creditcardType = type;
	}
	
	public boolean isPremium(){
		if(accountType == AccountType.PREMIUM){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void removePremium(){
		accountType = AccountType.BASIC;
		creditCard = null;
		securityNumber = null;
		creditcardType = null;
		expirationMonth = null;
		expirationYear = null;
		creditcardName = null;
		premiumChoice = null;
	}
	
	public String getCreditCard(){
		return creditCard;
	}
	
	public void setCreditCard(String card){
		creditCard = card;
	}
	
	public String getSecurityNumber(){
		return securityNumber;
	}
	
	public void setSecurityNumber(String number){
		securityNumber = number;
	}
	
	public String getExpirationMonth(){
		return expirationMonth;
	}
	
	public void setExpirationMonth(String month){
		expirationMonth = month;
	}
	
	public String getExpirationYear(){
		return expirationYear;
	}
	
	public void setExpirationYear(String year){
		expirationYear = year;
	}
	
	public String getCreditcardName(){
		return creditcardName;
	}
	
	public void setCreditcardName(String name){
		creditcardName = name;
	}
	
	public Date getPremiumExpiryDate(){
		return premiumExpiryDate;
	}
	
	public void setPremiumExpiryDate(Date date){
		premiumExpiryDate = date;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}
	
	public void addUserRole(UserRole role){
		this.userRoles.add(role);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getGooglePicture(){
		return googlePicture;
	}
	
	public void setGooglePicture(String url){
		this.googlePicture = url;
	}

	public UserPicture getPicture() {
		return picture;
	}

	public void setPicture(UserPicture picture) {
		this.picture = picture;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	
	public List<Ad> getBookmarkedAds() {
		return bookmarkedAds;
	}
	
	public void addBookmark(Ad ad){
		this.bookmarkedAds.add(ad);
	}
	
	public void removeBookmark(Ad ad){
		this.bookmarkedAds.remove(ad);
	}

	public List<Bid> getBids() {
		return bids;
	}
	
	public PremiumChoice getPremiumChoice(){
		return premiumChoice;
	}
	
	public void setPremiumChoice(PremiumChoice premiumChoice){
		this.premiumChoice = premiumChoice;
	}

	public List<AlertResult> getAlertResults() {
		return alertResults;
	}
	
	public void addAlertResult(AlertResult result){
		if (!alertResults.contains(result)){
			alertResults.add(result);
			result.setUser(this);
		}
	}
	
	public void removeAlertResult(AlertResult result){
		alertResults.remove(result);
		result.setUser(null);
	}

	public List<Message> getSentMessages() {
		return sentMessages;
	}

	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}

	public void setGoogleUser(boolean isGoogleUser) {
		this.isGoogleUser = isGoogleUser;
	}

	public List<PurchaseRequest> getPurchaseRequests() {
		return purchaseRequests;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public List<VisitEnquiry> getEnquiries() {
		return enquiries;
	}
	
	public User(){
		this.alertResults = new ArrayList<AlertResult>();
		this.bookmarkedAds = new ArrayList<Ad>();
		this.bids = new ArrayList<Bid>();
		this.enquiries = new ArrayList<VisitEnquiry>();
		this.purchaseRequests = new ArrayList<PurchaseRequest>();
		this.receivedMessages = new ArrayList<Message>();
		this.sentMessages = new ArrayList<Message>();
		this.userRoles = new ArrayList<UserRole>();
		this.visits = new ArrayList<Visit>();
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void addVisit(Visit visit) {
		if (!visits.contains(visit)){
			this.visits.add(visit);
		}
	}

	public void removeVisit(Visit visit) {
		this.visits.remove(visit);		
	}
	
}
