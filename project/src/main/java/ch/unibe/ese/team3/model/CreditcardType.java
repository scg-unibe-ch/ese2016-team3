package ch.unibe.ese.team3.model;

public enum CreditcardType {
	VISA("Visa"), MASTERCARD("Mastercard"), AMEX("American Express");
	
	private String creditcardTypeName;
	
	private CreditcardType(String creditcardTypeName){
		this.creditcardTypeName = creditcardTypeName;
	}
	
	private String getCreditcardName(){
		return this.creditcardTypeName;
	}
}
