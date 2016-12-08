package ch.unibe.ese.team3.controller.pojos.forms;

import java.time.Year;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ch.unibe.ese.team3.model.CreditcardType;
import ch.unibe.ese.team3.model.Gender;

/** This form is used when a user want to sign up for an account. */
public class SignupForm {

	@Size(min = 6, message = "Password must be at least 6 characters long")
	@NotNull
	private String password;

	/*
	 * @Pattern(regexp = password, message =
	 * "Your password validation doesn't match the original password"); private
	 * String passwordValidation;
	 */

	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Must be valid email address")
	@NotNull
	private String email;

	@Pattern(regexp = "[a-zA-Z\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC]+", message = "First name must be a valid name")
	@NotNull
	private String firstName;

	@Pattern(regexp = "[a-zA-Z\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC]+", message = "Last name must be a valid name")
	@NotNull
	private String lastName;

	@NotNull
	private Gender gender;

	@NotNull
	private boolean isPremium;
	
	@NotNull
	private int duration;

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

	private void validCreditcardDetails() {

		if (isPremium) {
			try {
				this.validCreditCard = validCreditCardWhenPremium();
				this.validCreditcardName = validCreditcardNameWhenPremium();
				this.validCreditcardType = validCreditcardTypeWhenPremium();
				this.validSecurityNumber = validSecurityNumberWhenPremium();
				this.validExpirationMonth = validExpirationMonthWhenPremium();
				this.validExpirationYear = validExpirationYearWhenPremium();
			} catch (NumberFormatException e) {
			}
		} else {
			this.validCreditCard = true;
			this.validCreditcardName = true;
			this.validCreditcardType = true;
			this.validSecurityNumber = true;
			this.validExpirationMonth = true;
			this.validExpirationYear = true;
		}

	}

	private boolean validCreditCardWhenPremium() {
		boolean validCreditcard = true;

		if (isPremium) {
			if (!java.util.regex.Pattern.matches("^[0-9 -]{16,19}$", creditCard)) {
				validCreditcard = false;
			}
		}

		this.validCreditCard = validCreditcard;
		return validCreditcard;
	}

	private boolean validCreditcardNameWhenPremium() {
		boolean validCreditcardName = true;

		if (isPremium) {
			if (!(java.util.regex.Pattern.matches("[a-zA-Z ]+", creditcardName)))
				validCreditcardName = false;
		}

		this.validCreditcardName = validCreditcardName;
		return validCreditcardName;
	}

	private boolean validExpirationMonthWhenPremium() {
		boolean validExpirationMonth = true;

		try {
			if (isPremium) {
				int month = Integer.parseInt(expirationMonth);

				if (!(1 <= month && month <= 12)) {
					validExpirationMonth = false;
				}
			}
		} catch (NumberFormatException e) {
			validExpirationMonth = false;
		}

		this.validExpirationMonth = validExpirationMonth;
		return validExpirationMonth;
	}

	private boolean validExpirationYearWhenPremium() {
		boolean validExpirationYear = true;

		try {
			if (isPremium) {

				if (!(Year.now().getValue() <= Integer.parseInt(expirationYear)))
					validExpirationYear = false;
			}
		} catch (NumberFormatException e) {
			validExpirationYear = false;
		}

		this.validExpirationYear = validExpirationYear;
		return validExpirationYear;
	}

	private boolean validSecurityNumberWhenPremium() {
		boolean validSecurityNumber = true;

		try {
			if (isPremium) {
				if (!(99 < Integer.parseInt(securityNumber) && Integer.parseInt(securityNumber) < 1000))
					validSecurityNumber = false;
			}
		} catch (NumberFormatException e) {
			validSecurityNumber = false;
		}

		this.validSecurityNumber = validSecurityNumber;
		return validSecurityNumber;
	}

	private boolean validCreditcardTypeWhenPremium() {
		boolean validCreditcardType = true;

		if (isPremium) {
			if (creditcardType == null)
				validCreditcardType = false;
		}

		this.validCreditcardType = validCreditcardType;
		return validCreditcardType;
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

	public boolean getIsPremium() {
		return isPremium;
	}

	public void setIsPremium(boolean premium) {
		this.isPremium = premium;
		validCreditcardDetails();
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String card) {
		this.creditCard = card;
		validCreditcardDetails();
	}

	public String getCreditcardName() {
		return creditcardName;
	}

	public void setCreditcardName(String name) {
		this.creditcardName = name;
		validCreditcardDetails();
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String month) {
		this.expirationMonth = month;
		validCreditcardDetails();
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String year) {
		this.expirationYear = year;
		validCreditcardDetails();
	}

	public String getSecurityNumber() {
		return securityNumber;
	}

	public void setSecurityNumber(String number) {
		this.securityNumber = number;
		validCreditcardDetails();
	}

	public CreditcardType getCreditcardType() {
		return creditcardType;
	}

	public void setCreditcardType(CreditcardType type) {
		this.creditcardType = type;
		validCreditcardDetails();
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	
	public int getDuration(){
		return duration;
	}
}
