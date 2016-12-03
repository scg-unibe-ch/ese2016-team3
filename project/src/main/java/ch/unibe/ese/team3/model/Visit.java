package ch.unibe.ese.team3.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;

/** A visit for a flat, has a time window. */
@Entity
public class Visit {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private Ad ad;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_visits",
		joinColumns = @JoinColumn(name = "visit_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> visitors;
	
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<VisitEnquiry> enquiries;
	
	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTimestamp;
	
	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTimestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public List<User> getVisitors() {
		return visitors;
	}
	
	public void addVisitor(User visitor){
		this.visitors.add(visitor);
		visitor.addVisit(this);
	}
	
	public void removeVisitor(User visitor){
		this.visitors.remove(visitor);
		visitor.removeVisit(this);
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Date getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public List<VisitEnquiry> getEnquiries() {
		return enquiries;
	}
	
	public void addEnquiry(VisitEnquiry enquiry){
		if (!enquiries.contains(enquiry)){
			enquiries.add(enquiry);
			enquiry.setVisit(this);
		}
	}
	
	public void removeEnquiry(VisitEnquiry enquiry){
		this.enquiries.remove(enquiry);
		enquiry.setVisit(null);
	}
	
	public Visit(){
		this.enquiries = new ArrayList<VisitEnquiry>();
		this.visitors = new ArrayList<User>();
	}
	
}