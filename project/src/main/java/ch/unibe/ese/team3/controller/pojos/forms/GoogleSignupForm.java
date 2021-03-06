package ch.unibe.ese.team3.controller.pojos.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/** This form is used when a user want to sign up for an account with google. */
public class GoogleSignupForm {


	private String email;
	
	private String googlePicture;

	@Pattern(regexp = "[a-zA-Z\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC]+", message = "First name must be a valid name")
	@NotNull
	private String firstName;

	@Pattern(regexp = "[a-zA-Z\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC]+", message = "Last name must be a valid name")
	@NotNull
	private String lastName;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGooglePicture(){
		return googlePicture;
	}
	
	public void setGooglePicture(String url){
		this.googlePicture = url;
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

}
