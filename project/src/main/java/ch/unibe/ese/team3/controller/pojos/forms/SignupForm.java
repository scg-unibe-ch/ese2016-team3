package ch.unibe.ese.team3.controller.pojos.forms;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.CreditcardType;

/** This form is used when a user want to sign up for an account. */
public class SignupForm {
	
	@Size(min = 6, message = "Password must be at least 6 characters long")
	@NotNull
	private String password;
	
	/*@Pattern(regexp = password, message = "Your password validation doesn't match the original password");
	private String passwordValidation;*/
	
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Must be valid email address")
	@NotNull
	private String email;

	@Pattern(regexp = "[a-zA-Z]+", message = "First name must be a valid name")
	@NotNull
	private String firstName;

	@Pattern(regexp = "[a-zA-Z]+", message = "Last name must be a valid name")
	@NotNull
	private String lastName;
	
	@NotNull
	private Gender gender;
	
	@NotNull
	private boolean isPremium;
	
	private String creditCard;
	
	private CreditcardType creditcardType;
	
	private String creditcardName;
	
	private String expirationMonth;
	
	private String expirationYear;
	
	private String securityNumber;
	
	@AssertTrue(message = "Credit card number must be 16 digits")
	private boolean validCreditCard;
	
	@AssertTrue(message = "Not a valid Name")
	private boolean validCreditcardName;
	
	@AssertTrue(message = "Month must be between 1 and 12")
	private boolean validExpirationMonth;
	
	@AssertTrue(message = "Your credit card has expired!")
	private boolean validExpirationYear;
	
	@AssertTrue(message = "Security number must be 3 digits")
	private boolean validSecurityNumber;
	
	@AssertTrue(message = "Please choose the type of your creditcard")
	private boolean validCreditcardType;
	
	public boolean isValidCreditCard() {
		return validCreditCard;
	}
	
	public boolean isValidCreditcardName() {
		return validCreditcardName;
	}
	
	public boolean isValidCreditcardType() {
		return validCreditcardType;
	}
	
	public boolean isValidExpirationMonth() {
		return validExpirationMonth;
	}
	
	public boolean isValidExpirationYear() {
		return validExpirationYear;
	}
	
	public boolean isValidSecurityNumber() {
		return validSecurityNumber;
	}

	public void setValidCreditcardName(boolean validCreditCardName) {
		this.validCreditcardName = validCreditCardName;
	}
	
	public void setValidCreditcardType(boolean validCreditcardType) {
		this.validCreditcardType = validCreditcardType;
	}
	
	public void setValidExpirationMonth(boolean validExpirationMonth) {
		this.validExpirationMonth = validExpirationMonth;
	}
	
	public void setValidExpirationYear(boolean validExpirationYear) {
		this.validExpirationYear = validExpirationYear;
	}
	
	public void setValidSecurityNumber(boolean validSecurityNumber) {
		this.validSecurityNumber = validSecurityNumber;
	}

	private boolean validCreditCardWhenPremium(){
		boolean valid = true;
		
		if (isPremium){
			if (!java.util.regex.Pattern.matches("^[0-9]{16}$", creditCard)){
				valid = false;
			}
		}
		
		this.validCreditCard = valid;
		return valid;
	}
	
	private boolean validCreditcardNameWhenPremium(){
		boolean valid = true;
		
		if (isPremium){
			if (!(java.util.regex.Pattern.matches("[a-zA-Z ]+", creditcardName)))
				valid = false;
		}
		
		this.validCreditcardName = valid;
		return valid;
	}
	
	private boolean validExpirationMonthWhenPremium(){
		boolean valid = true;
		
		try{
		if (isPremium){
			
			if (!(1 <= Integer.parseInt(expirationMonth) && Integer.parseInt(expirationMonth) <= 12))
				valid = false;
		}
		}catch(NumberFormatException e) {
			valid = false;
		}
		
		this.validExpirationMonth = valid;
		return valid;
	}
	
	private boolean validExpirationYearWhenPremium(){
		boolean valid = true;
		
		try{
		if (isPremium){
			
			if (!(2016 <= Integer.parseInt(expirationMonth)))
				valid = false;
		}
		}catch(NumberFormatException e) {
			valid = false;
		}
		
		this.validExpirationYear = valid;
		return valid;
	}
	
	private boolean validSecurityNumberWhenPremium(){
		boolean valid = true;
		
		try{
		if (isPremium){
			if (!(99 < Integer.parseInt(securityNumber) && Integer.parseInt(securityNumber) < 1000))
				valid = false;
		}
		}catch(NumberFormatException e) {
			valid = false;
		}
		
		this.validSecurityNumber = valid;
		return valid;
	}
	
	private boolean validCreditcardTypeWhenPremium(){
		boolean valid = true;
		
		if (isPremium){
			if (creditcardType == null)
				valid = false;
		}
		
		this.validCreditcardType = valid;
		return valid;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public boolean getIsPremium(){
		return isPremium;
	}
	
	public void setIsPremium(boolean premium){
		this.isPremium = premium;
		validCreditCardWhenPremium();
	}
	
	public String getCreditCard(){
		return creditCard;
	}
	
	public void setCreditCard(String card){
		this.creditCard = card;
		validCreditCardWhenPremium();
	}
	
	public String getCreditcardName(){
		return creditcardName;
	}
	
	public void setCreditcardName(String name){
		this.creditcardName = name;
		validCreditcardNameWhenPremium();
	}
	
	public String getExpirationMonth(){
		return expirationMonth;
	}
	
	public void setExpirationMonth(String month){
		this.expirationMonth = month;
		validExpirationMonthWhenPremium();
	}
	
	public String getExpirationYear(){
		return expirationYear;
	}
	
	public void setExpirationYear(String year){
		this.expirationYear = year;
		validExpirationYearWhenPremium();
	}
	
	public String getSecurityNumber(){
		return securityNumber;
	}
	
	public void setSecurityNumber(String number){
		this.securityNumber = number;
		validSecurityNumberWhenPremium();
	}
	
	public CreditcardType getCreditcardType(){
		return creditcardType;
	}
	
	public void setCreditcardType(CreditcardType type){
		this.creditcardType = type;
		validCreditcardTypeWhenPremium();
	}
}
