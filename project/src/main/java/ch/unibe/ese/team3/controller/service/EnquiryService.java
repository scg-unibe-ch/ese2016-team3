package ch.unibe.ese.team3.controller.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Rating;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.VisitEnquiry;
import ch.unibe.ese.team3.model.VisitEnquiryState;
import ch.unibe.ese.team3.model.dao.VisitDao;
import ch.unibe.ese.team3.model.dao.VisitEnquiryDao;

/** Provides access to enquiries saved in the database. */
@Service
public class EnquiryService {

	@Autowired
	private VisitEnquiryDao enquiryDao;

	@Autowired
	private VisitDao visitDao;

	/**
	 * Returns all enquiries that were sent to the given user sorted by date
	 * sent
	 * 
	 * @param recipient
	 *            the user to search for
	 * @return an Iterable of all matching enquiries
	 */
	@Transactional
	public Iterable<VisitEnquiry> getEnquiriesByRecipient(User recipient) {
		List<VisitEnquiry> enquiries = new LinkedList<VisitEnquiry>();
		for (VisitEnquiry enquiry : enquiryDao.findAll()) {
			if (enquiry.getVisit().getAd().getUser().getId() == recipient
					.getId()) {
				enquiries.add(enquiry);
			}
		}
		Collections.sort(enquiries, new Comparator<VisitEnquiry>() {
			@Override
			public int compare(VisitEnquiry enquiry1, VisitEnquiry enquiry2) {
				return enquiry2.getDateSent().compareTo(enquiry1.getDateSent());
			}
		});
		return enquiries;
	}
	
	public Map<Long, VisitEnquiry> getEnquiriesForAdBySender(Ad ad, User sender){
		Iterable<VisitEnquiry> sentEnquiries = enquiryDao.findBySender(sender);
		Map<Long, VisitEnquiry> sentEnquiriesByVisit = new HashMap<Long, VisitEnquiry>();
		for (VisitEnquiry enquiry : sentEnquiries){
			if (enquiry.getVisit().getAd().equals(ad)){
				sentEnquiriesByVisit.put(new Long(enquiry.getVisit().getId()), enquiry);
			}
		}		
		return sentEnquiriesByVisit;
	}

	/** Saves the given visit enquiry. */
	@Transactional
	public void saveVisitEnquiry(VisitEnquiry visitEnquiry) {
		enquiryDao.save(visitEnquiry);
	}

	/** Accepts the enquiry with the given id. */
	@Transactional
	public void acceptEnquiry(long enquiryId) {
		// accept visit
		VisitEnquiry enquiry = enquiryDao.findOne(enquiryId);
		enquiry.setState(VisitEnquiryState.ACCEPTED);
		enquiryDao.save(enquiry);

		// add user to the visitor list
		Visit visit = enquiry.getVisit();
		visit.addVisitor(enquiry.getSender());
		visitDao.save(visit);
	}

	/** Declines the enquiry with the given id. */
	@Transactional
	public void declineEnquiry(long enquiryId) {
		VisitEnquiry enquiry = enquiryDao.findOne(enquiryId);
		enquiry.setState(VisitEnquiryState.DECLINED);
		enquiryDao.save(enquiry);
	}

	/**
	 * Resets the enquiry with the given id, meaning that its state will be set
	 * to open again.
	 */
	@Transactional
	public void reopenEnquiry(long enquiryId) {
		VisitEnquiry enquiry = enquiryDao.findOne(enquiryId);
		enquiry.setState(VisitEnquiryState.OPEN);
		enquiryDao.save(enquiry);

		Visit visit = enquiry.getVisit();
		visit.removeVisitor(enquiry.getSender());
		visitDao.save(visit);
	}

}
