package ch.unibe.ese.team3.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a picture that is linked to an ad and therefore is linked to an
 * ad.
 */
@Entity
public class AdPicture extends Picture {
	
	@ManyToOne
	@JoinColumn(name = "ad_id")
	private Ad ad;

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}
}
