package ch.unibe.ese.team3.controller.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.BidDao;

@Service
public class AuctionService {

	private static final Logger logger = Logger.getLogger("Ithaca logger");

	@Autowired
	private BidDao bidDao;

	@Autowired
	private AdDao adDao;

	private boolean auctionIsStillRunning(Ad ad) {
		Date endDate = ad.getEndDate();
		Date currentDate = new Date();

		return !currentDate.after(endDate);
	}

	@Transactional
	public boolean checkAndBid(Ad ad, User bidder, int amount) {
		if (!canBid(ad, amount)) {
			logger.info(String.format("Bid failed for ad %d by user %s", ad.getId(), bidder.getEmail()));
			return false;
		}

		bid(ad, bidder, amount);
		logger.info(String.format("Successful bid for ad %d by user %s, amount: %d", ad.getId(), bidder.getEmail(), amount));
		return true;
	}

	@Transactional
	public boolean checkAndBuy(Ad ad, User purchaser) {
		if (!canBuy(ad)) {
			logger.info(String.format("Purchase failed for ad %d by user %s", ad.getId(), purchaser.getEmail()));
			return false;
		}

		buy(ad, purchaser);
		logger.info(String.format("Successful purchase for ad %d by user %s", ad.getId(), purchaser.getEmail()));
		return true;
	}

	public void bid(Ad ad, User user, int amount) {

		Bid bid = new Bid();
		bid.setAd(ad);
		bid.setAmount(amount);
		bid.setBidder(user);
		bid.setTimeStamp(new Date());
		bidDao.save(bid);
		incrementBidPriceForUser(ad);
	}

	public void buy(Ad ad, User user) {
		ad.setAvailable(false);
		ad.setPurchaser(user);
		adDao.save(ad);
	}

	private void incrementBidPriceForUser(Ad ad) {

		ad.setcurrentAuctionPrice(ad.getcurrentAuctionPrice() + ad.getIncreaseBidPrice());
		adDao.save(ad);
	}

	private boolean canBuy(Ad ad) {
		if (!ad.isAvailable() || !auctionIsStillRunning(ad)) {
			return false;
		}
		return true;
	}

	private boolean canBid(Ad ad, int amount) {
		if (!ad.isAvailable() || !auctionIsStillRunning(ad)) {
			return false;
		}

		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(ad);
		if (maxBid != null) {
			return maxBid.getAmount() < amount;
		}
		return true;
	}

}
