package ch.unibe.ese.team3.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PurchaseRequest {
	@Id
	@GeneratedValue
	private long id; 
	
	@ManyToOne
	private User purchaser;
	
	@ManyToOne
	@JoinColumn(name = "ad_id")
	private Ad ad;
	
	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(User purchaser) {
		this.purchaser = purchaser;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
