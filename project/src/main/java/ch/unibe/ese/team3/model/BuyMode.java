package ch.unibe.ese.team3.model;

import ch.unibe.ese.team3.enums.PageMode;

public enum BuyMode {
	BUY("buy", "buying"), RENT("rent", "renting");
	
	private String name;
	private String label;
	
	public static BuyMode fromPageMode(PageMode mode){
		switch(mode){
		case RENT:
			return BuyMode.RENT;
		default:
			return BuyMode.BUY;
		}
	}
	
	private BuyMode(String name, String label){
		this.name = name;
		this.label = label;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getLabel(){
		return this.label;
	}
}