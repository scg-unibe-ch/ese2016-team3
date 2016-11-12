package ch.unibe.ese.team3.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class AlertResult {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne	
	private User user;
	
	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private Date triggerDate;
	
	@ManyToOne
	private Ad triggerAd;
	
	@Column(nullable = false)
	private boolean notified;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser (User user) {
		this.user = user;
	}
	
	public Date getTriggerDate() {
		return triggerDate;
	}
	
	public void setTriggerDate(Date triggerDate) {
		this.triggerDate = triggerDate;
	}
	
	public Ad getTriggerAd() {
		return triggerAd;
	}
	
	public void setTriggerAd(Ad triggerAd) {
		this.triggerAd = triggerAd;
	}
	
	public boolean getNotified() {
		return notified;
	}
	
	public void setNotified(boolean notified) {
		this.notified = notified;
	}

}
