package ch.unibe.ese.team3.model;

import java.util.List;
import java.util.Set;

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
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.MERGE)
	private PremiumChoice premiumChoice;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole> userRoles;

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
	
	//new
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private List<Bid> bids;

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

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
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
	
	public void setBookmarkedAds(List<Ad> bookmarkedAds) {
		this.bookmarkedAds = bookmarkedAds;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	
	public PremiumChoice getPremiumChoice(){
		return premiumChoice;
	}
	
	public void setPremiumChoice(PremiumChoice premiumChoice){
		this.premiumChoice = premiumChoice;
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
	
}
