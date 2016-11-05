package ch.unibe.ese.team3.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bid {
	

	@Id
	@GeneratedValue
	private long id; 
	
	@ManyToOne
	private User bidder;
	
	@ManyToOne
	private Ad ad;
	
	private int amount;
	
	private Date date;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	

}
