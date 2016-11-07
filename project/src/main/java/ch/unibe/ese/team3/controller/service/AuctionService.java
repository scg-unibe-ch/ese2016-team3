package ch.unibe.ese.team3.controller.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.BidDao;

@Service
public class AuctionService {

	@Autowired
	private BidDao bidDao;
	
	@Autowired
	private AdDao adDao;
	
	private boolean checkIfAuctionisStillRunning(Ad ad) {
		boolean isStillRunning;
		Date endDate = ad.getEndDate();
		Date currentDate = new Date();

		if (currentDate.after(endDate))
			isStillRunning = false;

		else
			isStillRunning = true;

		return isStillRunning;
	}

	@Transactional
	public boolean checkAndBid(Ad ad, User bidder, int amount) {
		if (!canBid(ad, amount)) {
			return false;
		}

		bid(ad, bidder, amount);
		return true;
	}
	
	@Transactional
	public boolean checkAndBuy(Ad ad, User purchaser) {
		if (!canBuy(ad)) {
			return false;
		}

		buy(ad, purchaser);
		return true;
	}

	private void bid(Ad ad, User user, int amount) {
		
		Bid bid = new Bid();
		bid.setAd(ad);
		bid.setAmount(amount);
		bid.setBidder(user);
		bid.setTimeStamp(new Date());
		bidDao.save(bid);
		incrementBidPriceForUser(ad);
	}

	private void buy(Ad ad, User user) {
		ad.setAvailable(false);
		ad.setPurchaser(user);
		adDao.save(ad);
	}

	private void incrementBidPriceForUser(Ad ad) {

		ad.setcurrentAuctionPrice(ad.getcurrentAuctionPrice() + ad.getIncreaseBidPrice());
		adDao.save(ad);
	}
	
	private boolean canBuy(Ad ad) {
		if (!ad.isAvailable() || !checkIfAuctionisStillRunning(ad)) {
			return false;
		}
		return true;
	}

	private boolean canBid(Ad ad, int amount) {
		if (!ad.isAvailable() || !checkIfAuctionisStillRunning(ad)) {
			return false;
		}
		
		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(ad);
		if (maxBid != null){
			return maxBid.getAmount() < amount;
		}
		return true;
	}

}
