package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.BidDao;
import ch.unibe.ese.team3.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class AuctionServiceTest {
	

	@Autowired
	UserDao userDao;
	
	@Autowired
	AdDao adDao;
	
	@Autowired
	BidDao bidDao;
	
	@Autowired
	AuctionService auctionService;
	
	@Autowired
	AlertService alertService;
	
	@Test
	public void multipleBids() {
		// users
		User bidder1 = userDao.findByUsername("ese@unibe.ch");
		User auctionOwner = userDao.findByUsername("jane@doe.com");
		User bidder2 = userDao.findByUsername("user@bern.com");
		
		// ad to bid for
		Iterable<Ad> ads = adDao.findByUser(auctionOwner);
		Ad adBiddable = new Ad();
		
		for (Ad ad : ads) {
			if(ad.getTitle().equals("Vintage Villa")) {
				adBiddable = ad;
			}
		}
		adBiddable.setcurrentAuctionPrice(1500);
		
		// initial auction price is increased by increaseBidPrice
		int initialPrice = adBiddable.getcurrentAuctionPrice();
		int priceBefore = adBiddable.getcurrentAuctionPrice();
		int increaseAmount = adBiddable.getIncreaseBidPrice();
		
		//-------
		// 1st bid
		// it is assumed, that the method checkAndBid already gets the increased bid amount as input
		auctionService.checkAndBid(adBiddable, bidder1, priceBefore + increaseAmount);
		assertEquals(adBiddable.getcurrentAuctionPrice(), priceBefore + increaseAmount);
		
		// assert Bid in db has the correct currentAuctionPrice
		Bid firstBid = bidDao.findTopByAdOrderByAmountDesc(adBiddable);
		assertEquals(firstBid.getAmount(), adBiddable.getcurrentAuctionPrice());
		
		//-------
		// 2nd bid
		priceBefore = adBiddable.getcurrentAuctionPrice();
		
		auctionService.checkAndBid(adBiddable, bidder1, priceBefore + increaseAmount);
		assertEquals(adBiddable.getcurrentAuctionPrice(), priceBefore + increaseAmount);
		
		Bid secondBid = bidDao.findTopByAdOrderByAmountDesc(adBiddable);
		assertEquals(secondBid.getAmount(), adBiddable.getcurrentAuctionPrice());
		
		//-------
		// 3rd bid
		priceBefore = adBiddable.getcurrentAuctionPrice();
		
		auctionService.checkAndBid(adBiddable, bidder2, priceBefore + increaseAmount);
		assertEquals(adBiddable.getcurrentAuctionPrice(), priceBefore + increaseAmount);
		
		Bid thirdBid = bidDao.findTopByAdOrderByAmountDesc(adBiddable);
		assertEquals(thirdBid.getAmount(), adBiddable.getcurrentAuctionPrice());
		
		priceBefore = adBiddable.getcurrentAuctionPrice();
		
		// assert currentAuctionPrice has been augmented the right amount of time
		assertEquals(adBiddable.getcurrentAuctionPrice(), initialPrice + increaseAmount*3);
		
		//------------------------------------
		// BUY
		auctionService.checkAndBuy(adBiddable, bidder1);
		
		// assert Purchase was registeredProperly
		assertFalse(adBiddable.isAvailable());
		assertEquals(adBiddable.getPurchaser().getUsername(), bidder1.getUsername());	
		
		// does not change purchaser
		auctionService.checkAndBuy(adBiddable, bidder2);
		assertEquals(adBiddable.getPurchaser().getUsername(), bidder1.getUsername());
	}
};