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

import ch.unibe.ese.team3.base.BaseService;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.PurchaseRequest;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.BidDao;
import ch.unibe.ese.team3.model.dao.PurchaseRequestDao;
import ch.unibe.ese.team3.util.ListUtils;

@Service
public class AuctionService extends BaseService {

	@Autowired
	private BidDao bidDao;

	@Autowired
	private PurchaseRequestDao purchaseRequestDao;

	@Autowired
	private AdDao adDao;
	
	@Autowired
	private MessageService messageService;

	/**
	 * Check whether the bidder can bid for the specified ad
	 * and save the bid if he can.
	 * 
	 * A bidder can bid if the amount is larger than the highest
	 * bid so far and the auction is still running.
	 * 
	 * @param ad the ad to bid for
	 * @param bidder the bidder
	 * @param amount the amount to bid
	 * @return true if the bid was successful, otherwise false
	 */
	@Transactional
	public boolean checkAndBid(Ad ad, User bidder, int amount) {
		if (!canBid(ad, bidder, amount)) {
			logger.info(String.format("Bid failed for ad %d by user %s", ad.getId(), bidder.getEmail()));
			return false;
		}
		
		messageService.sendOverbiddenMessage(ad,bidder);
		bid(ad, bidder, amount);
		logger.info(String.format("Successful bid for ad %d by user %s, amount: %d", ad.getId(), bidder.getEmail(),
				amount));
		return true;
	}

	/**
	 * Check whether the purchaser can place a purchase request
	 * for the specified ad and save the request if he can.
	 * 
	 * A purchaser can place a request if the auction is still
	 * running and the purchaser didn't place a purchase request
	 * before for this ad.
	 * 
	 * @param ad
	 * @param purchaser
	 * @return true if the purchase request was successful, otherwise false
	 */
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
		bid.setAmount(amount);
		bid.setBidder(user);
		bid.setTimeStamp(new Date());
		ad.addBid(bid);
		bidDao.save(bid);
		incrementBidPrice(ad);
		incrementPrice(ad);
	}

	private void buy(Ad ad, User purchaser) {
		PurchaseRequest request = new PurchaseRequest();
		request.setPurchaser(purchaser);
		request.setCreated(new Date());
		ad.addPurchaseRequest(request);
		purchaseRequestDao.save(request);
	}

	private void incrementBidPrice(Ad ad) {

		ad.setcurrentAuctionPrice(ad.getCurrentAuctionPrice() + ad.getIncreaseBidPrice());
		adDao.save(ad);
	}
	
	private void incrementPrice(Ad ad) {
		ad.setPrice(ad.getCurrentAuctionPrice() - ad.getIncreaseBidPrice());
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

	/**
	 * Resume a paused auction.
	 * 
	 * @param ad the auction to resume
	 * @return true if the auction could be resumed, otherwise false
	 */
	public boolean resumeAuction(Ad ad) {
		if (!ad.isAuctionStopped()){
			return false;
		}
		
		ad.setAvailableForAuction(true);
		adDao.save(ad);
		
		return true;
	}

	/**
	 * Pause a running auction.
	 * 
	 * @param ad the auction to pause
	 * @return true if the auction could be paused, otherwise false
	 */
	public boolean stopAuction(Ad ad) {
		if (!ad.isAuctionRunning() && !ad.isAuctionNotYetRunning()){
			return false;
		}
		
		ad.setAvailableForAuction(false);
		adDao.save(ad);
		
		return true;
	}

	/**
	 * Complete a running, paused or expired auction
	 * 
	 * @param ad
	 * @return true if the auction could be completed, otherwise false
	 */
	public boolean completeAuction(Ad ad) {
		if (ad.isAuctionCompleted()){
			return false;
		}		
		
		ad.setAuctionCompleted(true);
		ad.setAvailableForAuction(false);
		adDao.save(ad);
		
		return true;
	}

	/**
	 * Get all auctions of a user which are not yet running
	 * 
	 * @param owner
	 * @return
	 */
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

	/**
	 * Get all auctions of a user which are running
	 * 
	 * @param owner
	 * @return
	 */
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

	/**
	 * Get all expired auctions of a user
	 * 
	 * @param owner
	 * @return
	 */
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

	/**
	 * Get all paused auctions of a user
	 * 
	 * @param owner
	 * @return
	 */
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

	/**
	 * Get all completed auctions of a user
	 * 
	 * @param owner
	 * @return
	 */
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

	/**
	 * Get all bids for a specific ad under auction
	 * 
	 * @param ad
	 * @return
	 */
	public List<Bid> getBidsForAd(Ad ad) {
		return ListUtils.convertToList(bidDao.findByAdOrderByAmountDesc(ad));
	}

	/**
	 * Get all purchase requests for a specific ad under auction
	 * @param ad
	 * @return
	 */
	public List<PurchaseRequest> getPurchaseRequestForAd(Ad ad) {
		return ListUtils.convertToList(purchaseRequestDao.findByAdOrderByCreatedAsc(ad));
	}

	/**
	 * Get all placed bids of a user, grouped by auction and
	 * ordered by amount descending
	 * 
	 * @param bidder
	 * @return
	 */
	public Map<Ad, SortedSet<Bid>> getBidsByUser(User bidder) {
		List<Bid> bidsByUser = ListUtils.convertToList(bidDao.findByBidder(bidder));
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

	/**
	 * Compares bids according to their amount, descending.
	 *
	 */
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

	/**
	 * Check whether a user placed a purchase request for a
	 * specific ad
	 * 
	 * @param ad
	 * @param user
	 * @return true if the user placed a purchase request, otherwise false
	 */
	public boolean hasUserSentBuyRequest(Ad ad, User user) {
		Iterable<PurchaseRequest> requests = purchaseRequestDao.findByAdAndPurchaser(ad, user);
		return requests.iterator().hasNext();
	}

	/**
	 * Get the ten most recent bids for a specific ad
	 * 
	 * @param ad
	 * @return
	 */
	public List<Bid> getMostRecentBidsForAd(Ad ad) {
		return ListUtils.convertToList(bidDao.findTop10ByAdOrderByAmountDesc(ad));
	}

}
