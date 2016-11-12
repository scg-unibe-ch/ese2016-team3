package ch.unibe.ese.team3.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.PurchaseRequest;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.BidDao;
import ch.unibe.ese.team3.model.dao.PurchaseRequestDao;

@Service
public class AuctionService {

	private static final Logger logger = Logger.getLogger("Ithaca logger");

	@Autowired
	private BidDao bidDao;

	@Autowired
	private PurchaseRequestDao purchaseRequestDao;

	@Autowired
	private AdDao adDao;

	private boolean auctionIsRunning(Ad ad) {
		Date endDate = ad.getEndDate();
		Date startDate = ad.getStartDate();
		Date currentDate = new Date();

		return !(currentDate.after(endDate) || currentDate.before(startDate));
	}

	@Transactional
	public boolean checkAndBid(Ad ad, User bidder, int amount) {
		if (!canBid(ad, amount)) {
			logger.info(String.format("Bid failed for ad %d by user %s", ad.getId(), bidder.getEmail()));
			return false;
		}

		bid(ad, bidder, amount);
		logger.info(String.format("Successful bid for ad %d by user %s, amount: %d", ad.getId(), bidder.getEmail(),
				amount));
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

	private void bid(Ad ad, User user, int amount) {
		Bid bid = new Bid();
		bid.setAd(ad);
		bid.setAmount(amount);
		bid.setBidder(user);
		bid.setTimeStamp(new Date());
		ad.getBids().add(bid);
		bidDao.save(bid);
		incrementBidPrice(ad);
	}

	private void buy(Ad ad, User purchaser) {
		PurchaseRequest request = new PurchaseRequest();
		request.setAd(ad);
		request.setPurchaser(purchaser);
		request.setCreated(new Date());
		ad.getPurchaseRequests().add(request);
		purchaseRequestDao.save(request);
	}

	private void incrementBidPrice(Ad ad) {

		ad.setcurrentAuctionPrice(ad.getCurrentAuctionPrice() + ad.getIncreaseBidPrice());
		adDao.save(ad);
	}

	private boolean canBuy(Ad ad) {
		if (!ad.isAvailable() || !auctionIsRunning(ad)) {
			return false;
		}
		return true;
	}

	private boolean canBid(Ad ad, int amount) {
		if (!ad.isAvailable() || !auctionIsRunning(ad)) {
			return false;
		}

		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(ad);
		if (maxBid != null) {
			return maxBid.getAmount() < amount;
		}

		return amount >= ad.getCurrentAuctionPrice();
	}

	public void resumeAuction(Ad ad) {
		ad.setAvailable(true);
		adDao.save(ad);
	}

	public void stopAuction(Ad ad) {
		ad.setAvailable(false);
		adDao.save(ad);
	}

	public List<Ad> getNotYetRunningAuctionsForUser(User owner) {
		Date now = new Date();
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAvailable() && now.before(ad.getStartDate())) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getRunningAuctionsForUser(User owner) {
		Date now = new Date();
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();
		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAvailable() && (now.after(ad.getStartDate()) && now.before(ad.getEndDate()))) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getExpiredAuctionsForUser(User owner) {
		Date now = new Date();
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAvailable() && now.after(ad.getEndDate())) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getStoppedAuctionsForUser(User owner) {
		Date now = new Date();
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (!ad.isAvailable() && (now.after(ad.getStartDate()) && now.before(ad.getEndDate()))) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getCompletedAuctionsForUser(User owner) {
		Date now = new Date();
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (!ad.isAvailable() && now.after(ad.getEndDate())) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Bid> getBidsForAd(Ad ad) {
		return convertToList(bidDao.findByAdOrderByAmountDesc(ad));
	}

	public List<PurchaseRequest> getPurchaseRequestForAd(Ad ad) {
		return convertToList(purchaseRequestDao.findByAdOrderByCreatedAsc(ad));
	}

	private <T> List<T> convertToList(Iterable<T> iterable) {
		ArrayList<T> list = new ArrayList<T>();

		Iterator<T> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			T item = iterator.next();
			list.add(item);
		}

		return list;
	}

}
