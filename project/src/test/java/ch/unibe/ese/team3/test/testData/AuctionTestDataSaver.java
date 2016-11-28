package ch.unibe.ese.team3.test.testData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.PurchaseRequest;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.*;

@Service
public class AuctionTestDataSaver {

	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BidDao bidDao;
	
	@Autowired
	private PurchaseRequestDao purchaseDao;
	
	@Transactional
	public void saveTestData() throws Exception {
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		User oprah = userDao.findByUsername("oprah@ithaca.com");
		User jane = userDao.findByUsername("jane@doe.com");
		
		User[] bidders = new User[]{bernerBaer, ese, oprah};
		User[] purchasers = new User[]{bernerBaer, ese, oprah};
		
		Iterable<Ad> auctionAds = adDao.findByUserAndAuction(jane, true);
		
		Ad auctionAd2 = null, auctionAd3 = null, auctionAd4 = null, auctionAd5 = null;
		
		Iterator<Ad> iter = auctionAds.iterator();
		while (iter.hasNext()){
			Ad current = iter.next();
			switch (current.getTitle()){
			case "Nice house very close to town center":
				auctionAd2 = current;
				break;
			case "Lovely flat near the lake of Thun":
				auctionAd3 = current;
				break;
			case "Lovely studio in the old town of Bern":
				auctionAd4 = current;
				break;
			case "Old farm house in the Emmental":
				auctionAd5 = current;
				break;
			default:
				break;
			}
		}
		
		addBidsForAd(bidders, auctionAd2, 10, LocalDateTime.of(2016, 11, 2, 13, 20, 20));
		addBidsForAd(bidders, auctionAd3, 21, LocalDateTime.of(2016, 11, 4, 7, 13, 45));
		addBidsForAd(bidders, auctionAd4, 14, LocalDateTime.of(2016, 10, 10, 8, 44, 31));
		addBidsForAd(bidders, auctionAd5, 3, LocalDateTime.of(2016, 10, 10, 19, 6, 12));
		addPurchasersForAd(purchasers, auctionAd2, 1, LocalDateTime.of(2016, 11, 2, 13, 14, 23));
		addPurchasersForAd(purchasers, auctionAd3, 2, LocalDateTime.of(2016, 11, 12, 10, 54, 45));
		addPurchasersForAd(purchasers, auctionAd5, 1, LocalDateTime.of(2016, 11, 9, 16, 33, 28));
	}

	private void addBidsForAd(User[] bidders, Ad ad, int bids, LocalDateTime start) {
		int amount = ad.getCurrentAuctionPrice();
		LocalDateTime date = start;
		Random r = new Random();
		
		for (int i = 0; i < bids; i++){
						
			Bid bid = new Bid();
			bid.setAd(ad);
			bid.setTimeStamp(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
			bid.setBidder(bidders[i % bidders.length]);
			bid.setAmount(amount);
			amount += ad.getIncreaseBidPrice();
			ad.getBids().add(bid);
			bidDao.save(bid);
			int additionalSeconds = r.nextInt(3600 * 6); //max 6 hours
			date = date.plusSeconds(additionalSeconds);
		}
		ad.setcurrentAuctionPrice(amount);
		adDao.save(ad);
	}
	
	private void addPurchasersForAd(User[] purchasers, Ad ad, int purchases, LocalDateTime start) {
		LocalDateTime date = start;
		Random r = new Random();
		
		for (int i = 0; i < purchases; i++){
						
			PurchaseRequest request = new PurchaseRequest();
			request.setAd(ad);
			request.setPurchaser(purchasers[i % purchasers.length]);
			request.setCreated(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
			ad.getPurchaseRequests().add(request);
			purchaseDao.save(request);
			int additionalSeconds = r.nextInt(3600*6); //max 6 hours
			date = date.plusSeconds(additionalSeconds);
		}
		adDao.save(ad);
	}
}
