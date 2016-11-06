package ch.unibe.ese.team3.controller.pojos.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ch.unibe.ese.team3.model.CreditcardType;

/** This form is used when a user wants to upgrade their profile. */
public class UpgradeForm {
	
	private String username;
	
	@NotNull
	private boolean isPremium;
	
	@NotNull
	private int duration;
	
	

	@Pattern(regexp = "[0-9]{16}", message = "Credit card number must be 16 digits")
	@NotNull
	private String creditCard;
	
	@NotNull
	private CreditcardType creditcardType;
	
	@Pattern(regexp = "[0-9]{3}", message = "Security number must be 3 digits")
	@NotNull
	private String securityNumber;
	
	@Pattern(regexp = "[1-9]|1[0-2]", message = "Month must be between 1 and 12")
	@NotNull
	private String expirationMonth;
	
	@Pattern(regexp = "2016|2017|2018|2019|202[0-5]", message = "Your credit card has expired!")
	@NotNull
	private String expirationYear;
	
	@Pattern(regexp = "[a-zA-Z ]+", message = "Not a valid name")
	@NotNull
	private String creditcardName;
	
	
	public String getCreditCard(){
		return creditCard;
	}
	
	public void setCreditCard(String card){
		creditCard = card;
	}
	
	public CreditcardType getCreditcardType(){
		return creditcardType;
	}
	
	public void setCreditcardType(CreditcardType type){
		creditcardType = type;
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
	
	public boolean getIsPremium(){
		return isPremium;
	}
	
	public void setIsPremium(boolean isPremium){
		this.isPremium = isPremium;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	
	public int getDuration(){
		return duration;
	}

}
