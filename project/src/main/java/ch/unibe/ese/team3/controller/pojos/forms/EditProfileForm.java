package ch.unibe.ese.team3.controller.pojos.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team3.model.AccountType;

/** This form is used when a user wants to edit their profile. */
public class EditProfileForm {

	@NotBlank(message = "Required")
	private String username;
	
	@NotBlank(message = "Required")
	private String password;

	@NotBlank(message = "Required")
	private String firstName;

	@NotBlank(message = "Required")
	private String lastName;
	
	@NotNull
	boolean isPremium;
	
	@Pattern(regexp = "[0-9]{16}", message = "Credit card number must be 16 digits")
	private String creditCard;
	
	private String aboutMe;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean getIsPremium(){
		return isPremium;
	}
	
	public void setIsPremium(boolean isPremium){
		this.isPremium = isPremium;
	}
	
	public String getCreditCard(){
		return creditCard;
	}
	
	public void setCreditCard(String card){
		creditCard = card;
	}
}
