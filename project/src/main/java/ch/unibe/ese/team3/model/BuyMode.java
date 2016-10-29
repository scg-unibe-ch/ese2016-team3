package ch.unibe.ese.team3.model;

import ch.unibe.ese.team3.enums.PageMode;

public enum BuyMode {
	BUY, RENT;
	
	public static BuyMode fromPageMode(PageMode mode){
		switch(mode){
		case RENT:
			return BuyMode.RENT;
		default:
			return BuyMode.BUY;
		}
	}
}