package ch.unibe.ese.team3.controller.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.BidDao;

public class BidService {

	
	@Autowired
	private BidDao bidDao;
	
	
	/**
	 * Saves new Bid in DB
	 * @param ad
	 * @param user
	 * @param amount
	 */
	private void saveBid(Ad ad, User user, int amount){
		
		Bid bid = new Bid();
		bid.setAd(ad);
		bid.setAmount(amount);
		bid.setBidder(user);
		bid.setTimeStamp(new Date());
		bidDao.save(bid);		
		
	}
}
