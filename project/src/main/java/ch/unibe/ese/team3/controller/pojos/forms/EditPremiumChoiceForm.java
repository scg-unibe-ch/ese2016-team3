package ch.unibe.ese.team3.controller.pojos.forms;


import org.hibernate.validator.constraints.NotBlank;

/** This Form is used if an administrator wants to change Premium packages */
public class EditPremiumChoiceForm {
	
	//@NotBlank(message = "Required")
	private int duration;
	
	//@NotBlank(message = "Required")
	private double price;
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
