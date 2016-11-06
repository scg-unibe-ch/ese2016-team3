package ch.unibe.ese.team3.model;

import ch.unibe.ese.team3.enums.PageMode;

public enum BuyMode {
	BUY("buy"), RENT("rent");
	
	private String name;
	
	public static BuyMode fromPageMode(PageMode mode){
		switch(mode){
		case RENT:
			return BuyMode.RENT;
		default:
			return BuyMode.BUY;
		}
	}
	
	private BuyMode(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}