package ch.unibe.ese.team3.controller.service;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

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
public class AuctionService extends BaseService {

	@Autowired
	private BidDao bidDao;

	@Autowired
	private PurchaseRequestDao purchaseRequestDao;

	@Autowired
	private AdDao adDao;

	@Transactional
	public boolean checkAndBid(Ad ad, User bidder, int amount) {
		if (!canBid(ad, bidder, amount)) {
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
		if (!canBuy(ad, purchaser)) {
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

	private boolean canBuy(Ad ad, User purchaser) {
		if (!ad.isAvailableForAuction() || !ad.isAuctionRunning() || ad.getUser().equals(purchaser)) {
			return false;
		}
		return true;
	}

	private boolean canBid(Ad ad, User bidder, int amount) {
		if (!ad.isAvailableForAuction() || !ad.isAuctionRunning() || ad.getUser().equals(bidder)) {
			return false;
		}

		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(ad);
		if (maxBid != null) {
			return maxBid.getAmount() < amount;
		}

		return amount >= ad.getCurrentAuctionPrice();
	}

	public void resumeAuction(Ad ad) {
		ad.setAvailableForAuction(true);
		adDao.save(ad);
	}

	public void stopAuction(Ad ad) {
		ad.setAvailableForAuction(false);
		adDao.save(ad);
	}

	public void completeAuction(Ad ad) {
		ad.setAuctionCompleted(true);
		ad.setAvailableForAuction(false);
		adDao.save(ad);
	}

	public List<Ad> getNotYetRunningAuctionsForUser(User owner) {
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAuctionNotYetRunning()) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getRunningAuctionsForUser(User owner) {
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();
		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAuctionRunning()) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getExpiredAuctionsForUser(User owner) {
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.hasAuctionExpired()) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getStoppedAuctionsForUser(User owner) {
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAuctionStopped()) {
				ads.add(ad);
			}
		}

		return ads;
	}

	public List<Ad> getCompletedAuctionsForUser(User owner) {
		ArrayList<Ad> ads = new ArrayList<Ad>();
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(owner, true);
		Iterator<Ad> iter = auctionAds.iterator();

		while (iter.hasNext()) {
			Ad ad = iter.next();
			if (ad.isAuctionCompleted()) {
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

	public Map<Ad, SortedSet<Bid>> getBidsByUser(User bidder) {
		List<Bid> bidsByUser = convertToList(bidDao.findByBidder(bidder));
		Map<Ad, SortedSet<Bid>> bidsByAd = new HashMap<Ad, SortedSet<Bid>>();
		for (Bid bid : bidsByUser) {
			Ad ad = bid.getAd();
			if (!bidsByAd.containsKey(ad)) {
				bidsByAd.put(ad, new TreeSet<Bid>(new BidComparator()));
			}
			bidsByAd.get(ad).add(bid);
		}
		return bidsByAd;
	}

	private class BidComparator implements Comparator<Bid> {

		@Override
		public int compare(Bid o1, Bid o2) {
			if (o1.getAmount() < o2.getAmount()) {
				return 1;
			} else if (o1.getAmount() == o2.getAmount()) {
				return 0;
			} else {
				return -1;
			}
		}

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

	public boolean hasUserSentBuyRequest(Ad ad, User user) {
		Iterable<PurchaseRequest> requests = purchaseRequestDao.findByAdAndPurchaser(ad, user);
		return requests.iterator().hasNext();
	}

}
