package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.PurchaseRequest;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.BidDao;
import ch.unibe.ese.team3.model.dao.PurchaseRequestDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.util.ListUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })

@WebAppConfiguration
@Transactional
public class AuctionServiceTest {

	@Autowired
	UserDao userDao;

	@Autowired
	AdDao adDao;

	@Autowired
	BidDao bidDao;
	
	@Autowired
	PurchaseRequestDao purchaseRequestDao;

	@Autowired
	AuctionService auctionService;

	@Autowired
	AlertService alertService;

	private User bidder1;
	private User bidder2;
	private User purchaser1;
	private User purchaser2;
	private User auctionOwner;

	private Ad auctionAd;

	@Before
	public void setUp() throws ParseException {
		
		bidder1 = createUser("bidder1@ithaca.com", "password", "Bidder1", "Bidder1", Gender.MALE, AccountType.BASIC);
		bidder2 = createUser("bidder2@ithaca.com", "password", "Bidder1", "Bidder1", Gender.MALE, AccountType.BASIC);
		bidder1.setAboutMe("About");
		bidder2.setAboutMe("About");
		
		userDao.save(bidder1);
		userDao.save(bidder2);
		
		purchaser1 = bidder1;
		purchaser2 = bidder2;
		auctionOwner = userDao.findByUsername("jane@doe.com");

		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

		auctionAd = new Ad();
		auctionAd.setZipcode(3506);
		auctionAd.setType(Type.HOUSE);
		auctionAd.setBuyMode(BuyMode.BUY);
		auctionAd.setMoveInDate(formatter.parse("01.01.2017"));
		auctionAd.setCreationDate(formatter.parse("12.11.2016"));
		auctionAd.setPrice(1200000);
		auctionAd.setSquareFootage(120);
		auctionAd.setRoomDescription("A nice house out in the green. Just a few minutes away from the train station.");
		auctionAd.setUser(auctionOwner);
		auctionAd.setTitle("New renovated house");
		auctionAd.setStreet("Trogmattweg 3");
		auctionAd.setCity("Grosshöchstetten");
		auctionAd.setNumberOfBath(3);

		auctionAd.setBalcony(true);
		auctionAd.setGarage(true);
		auctionAd.setDishwasher(true);
		auctionAd.setElevator(false);
		auctionAd.setParking(false);

		auctionAd.setBuildYear(1984);
		auctionAd.setRenovationYear(2015);
		auctionAd.setDistancePublicTransport(400);
		auctionAd.setDistanceSchool(600);
		auctionAd.setDistanceShopping(600);
		auctionAd.setFloorLevel(3);
		auctionAd.setNumberOfRooms(8);
		auctionAd.setInfrastructureType(InfrastructureType.CABLE);

		auctionAd.setAuction(true);
		auctionAd.setStartPrice(900000);
		auctionAd.setIncreaseBidPrice(1000);
		auctionAd.setcurrentAuctionPrice(auctionAd.getStartPrice() + auctionAd.getIncreaseBidPrice());
		auctionAd.setStartDate(formatter.parse("10.10.2016"));
		auctionAd.setEndDate(formatter.parse("12.12.2016"));

		adDao.save(auctionAd);
	}

	@Test
	public void singleBidSuccessful() {
		int amount = 901000;

		boolean bidSuccess = auctionService.checkAndBid(auctionAd, bidder1, amount);
		assertTrue(bidSuccess);
		assertEquals(auctionAd.getCurrentAuctionPrice(), 902000);

		Bid firstBid = bidDao.findTopByAdOrderByAmountDesc(auctionAd);
		assertEquals(901000, firstBid.getAmount());
		assertEquals(bidder1, firstBid.getBidder());
	}

	@Test
	public void singleBidNotSuccessful() {
		int amount = 900000;
		
		boolean bidSuccess = auctionService.checkAndBid(auctionAd, bidder1, amount);
		assertFalse(bidSuccess);
	}

	@Test
	public void multipleBidsSuccessful() {

		int amount1 = 901000;
		int amount2 = 902000;
		
		boolean successBid1 = auctionService.checkAndBid(auctionAd, bidder1, amount1);
		boolean successBid2 = auctionService.checkAndBid(auctionAd, bidder2, amount2);
		
		assertTrue(successBid1);
		assertTrue(successBid2);
		
		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(auctionAd);
		assertEquals(amount2, maxBid.getAmount());
		assertEquals(bidder2, maxBid.getBidder());
	}
	
	@Test
	public void multipleBidsNotSuccessful(){
		int amount1 = 901000;
		int amount2 = 901000;
		
		boolean successBid1 = auctionService.checkAndBid(auctionAd, bidder1, amount1);
		boolean successBid2 = auctionService.checkAndBid(auctionAd, bidder2, amount2);
		
		assertTrue(successBid1);
		assertFalse(successBid2);
		
		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(auctionAd);
		assertEquals(amount1, maxBid.getAmount());
		assertEquals(bidder1, maxBid.getBidder());
	}
	
	@Test
	public void bidOnExpiredAuction(){
		auctionAd.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd);
		
		int amount = 901000;
		
		boolean successBid = auctionService.checkAndBid(auctionAd, bidder1, amount);
		assertFalse(successBid);
		assertEquals(null, bidDao.findTopByAdOrderByAmountDesc(auctionAd));
	}
	
	@Test
	public void bidOnUnstartedAuction(){
		auctionAd.setStartDate(Date.from(LocalDate.now().plusDays(10l).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd);
		
		int amount = 901000;
		
		boolean successBid = auctionService.checkAndBid(auctionAd, bidder1, amount);
		assertFalse(successBid);
		assertEquals(null, bidDao.findTopByAdOrderByAmountDesc(auctionAd));
		assertEquals(1, auctionService.getNotYetRunningAuctionsForUser(auctionOwner).size());
	}
	
	@Test
	public void bidOnOwnAuctionFails(){
		boolean success = auctionService.checkAndBid(auctionAd, auctionOwner, 901000);
		assertFalse(success);
	}
	
	@Test
	public void buyRequest(){
		boolean buySuccessful = auctionService.checkAndBuy(auctionAd, purchaser1);		
		
		assertTrue(buySuccessful);
		
		int count = ListUtils.countIterable(purchaseRequestDao.findByAd(auctionAd));		
		assertEquals(1, count);
	}
	
	@Test
	public void multipleBuyRequest(){
		boolean buySuccessful1 = auctionService.checkAndBuy(auctionAd, purchaser1);
		boolean buySuccessful2 = auctionService.checkAndBuy(auctionAd, purchaser2);
		
		assertTrue(buySuccessful1);
		assertTrue(buySuccessful2);
		
		int count = ListUtils.countIterable(purchaseRequestDao.findByAd(auctionAd));		
		assertEquals(2, count);
	}
	
	@Test
	public void buyRequestForOwnAuctionFails(){
		boolean success = auctionService.checkAndBuy(auctionAd, auctionOwner);
		assertFalse(success);
	}
	
	@Test
	public void stopAuction(){
		auctionService.stopAuction(auctionAd);
		assertFalse(auctionAd.isAvailableForAuction());
		assertEquals(2, auctionService.getStoppedAuctionsForUser(auctionOwner).size());
	}
	
	@Test
	public void bidOnStoppedAuction(){
		auctionService.stopAuction(auctionAd);
		boolean success = auctionService.checkAndBid(auctionAd, bidder1, 901000);
		assertFalse(success);
	}
	
	@Test
	public void buyRequestOnStoppedAuction(){
		auctionService.stopAuction(auctionAd);
		boolean success = auctionService.checkAndBuy(auctionAd, purchaser1);
		assertFalse(success);
	}
	
	@Test
	public void resumeStoppedAuction(){
		auctionService.stopAuction(auctionAd);
		auctionService.resumeAuction(auctionAd);
		assertTrue(auctionAd.isAvailableForAuction());
	}
	
	@Test
	public void bidOnResumedAuction(){
		auctionService.stopAuction(auctionAd);
		auctionService.resumeAuction(auctionAd);
		boolean success = auctionService.checkAndBid(auctionAd, bidder1, 901000);
		assertTrue(success);
	}
	
	@Test
	public void bidOnResumedButExpiredAuction(){
		auctionAd.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd);
		
		auctionService.stopAuction(auctionAd);
		auctionService.resumeAuction(auctionAd);
		boolean success = auctionService.checkAndBid(auctionAd, bidder1, 901000);
		assertFalse(success);
		assertEquals(2, auctionService.getExpiredAuctionsForUser(auctionOwner).size());
	}
	
	@Test
	public void completeAuction(){
		auctionService.completeAuction(auctionAd);
		assertTrue(auctionAd.isAuctionCompleted());
		assertEquals(2, auctionService.getCompletedAuctionsForUser(auctionOwner).size());
	}
	
	@Test
	public void bidOnCompletedAuction(){
		auctionService.completeAuction(auctionAd);
		boolean success = auctionService.checkAndBid(auctionAd, bidder1, 901000);
		assertFalse(success);
	}
	
	@Test
	public void purchaseCompletedAuction(){
		auctionService.completeAuction(auctionAd);
		boolean success = auctionService.checkAndBuy(auctionAd, purchaser1);
		assertFalse(success);
	}
	
	@Test
	public void getCompletedAuctions(){
		List<Ad> completedAuctions = auctionService.getCompletedAuctionsForUser(auctionOwner);
		assertEquals(1, completedAuctions.size());
	}
	
	@Test
	public void getRunningAuctions(){
		List<Ad> runningAuctions = auctionService.getRunningAuctionsForUser(auctionOwner);
		assertEquals(3, runningAuctions.size());
	}
	
	@Test
	public void getPausedAuctions(){
		List<Ad> pausedAuctions = auctionService.getStoppedAuctionsForUser(auctionOwner);
		assertEquals(1, pausedAuctions.size());
	}
	
	@Test
	public void getExpiredAuctions(){
		List<Ad> expiredAuctions = auctionService.getExpiredAuctionsForUser(auctionOwner);
		assertEquals(1, expiredAuctions.size());
	}
	
	@Test
	public void getNotYetRunningAuctions(){
		List<Ad> notRunningAuctions = auctionService.getNotYetRunningAuctionsForUser(auctionOwner);
		assertEquals(0, notRunningAuctions.size());
	}
	
	@Test
	public void userHasSentPurchaseRequestForAd(){
		auctionService.checkAndBuy(auctionAd, purchaser1);
		assertTrue(auctionService.hasUserSentBuyRequest(auctionAd, purchaser1));
	}
	
	@Test
	public void userHasNotSentPurchaseRequestForAd(){
		assertFalse(auctionService.hasUserSentBuyRequest(auctionAd, purchaser1));
	}
	
	@Test
	public void getBidsForAdNoBids(){
		List<Bid> bids = auctionService.getBidsForAd(auctionAd);
		assertEquals(0, bids.size());
	}
	
	@Test
	public void getBidsForAdWithSingleBid(){
		int amount = 901000;

		auctionService.checkAndBid(auctionAd, bidder1, amount);
		
		List<Bid> bids = auctionService.getBidsForAd(auctionAd);
		assertEquals(1, bids.size());
	}
	
	@Test
	public void getBidsForAdWithMultipleBid(){
		int amount1 = 901000;
		int amount2 = 902000;

		auctionService.checkAndBid(auctionAd, bidder1, amount1);
		auctionService.checkAndBid(auctionAd, bidder2, amount2);
		
		List<Bid> bids = auctionService.getBidsForAd(auctionAd);
		assertEquals(2, bids.size());
		
		assertEquals(amount2, bids.get(0).getAmount());
		assertEquals(amount1, bids.get(1).getAmount());
	}
	
	@Test
	public void getPurchaseRequestsForAdNoRequests(){
		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(auctionAd);
		assertEquals(0, purchaseRequests.size());
	}
	
	@Test
	public void getPurchaseRequestsForAdWithSingleRequest(){
		auctionService.checkAndBuy(auctionAd, bidder1);
		
		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(auctionAd);
		assertEquals(1, purchaseRequests.size());
	}
	
	@Test
	public void getPurchaseRequestsForAdWithMultipleRequests(){
		auctionService.checkAndBuy(auctionAd, bidder1);
		auctionService.checkAndBuy(auctionAd, bidder2);
		
		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(auctionAd);
		assertEquals(2, purchaseRequests.size());
	}
	
	@Test
	public void getMostRecentBidsLessThanTenBids(){
		int amount1 = 901000;
		int amount2 = 902000;

		auctionService.checkAndBid(auctionAd, bidder1, amount1);
		auctionService.checkAndBid(auctionAd, bidder2, amount2);
		
		List<Bid> bids = auctionService.getMostRecentBidsForAd(auctionAd);
		assertEquals(2, bids.size());
		
		assertEquals(amount2, bids.get(0).getAmount());
		assertEquals(amount1, bids.get(1).getAmount());
	}
	
	@Test
	public void getMostRecentBidsMoreThanTenBids(){
		int amount = 901000;
		
		User[] users = new User[]{bidder1, bidder2};
		
		for (int i = 0; i < 20; i++){
			auctionService.checkAndBid(auctionAd, users[i%2], amount);
			amount += 1000;
		}
		
		List<Bid> bids = auctionService.getMostRecentBidsForAd(auctionAd);
		assertEquals(10, bids.size());
		
		assertEquals(amount - 1000, bids.get(0).getAmount());
		assertEquals(amount - 10000, bids.get(9).getAmount());
	}
	
	@Test
	public void getBidsByUserNoBids(){
		Map<Ad, SortedSet<Bid>> bids = auctionService.getBidsByUser(bidder1);
		assertTrue(bids.isEmpty());
	}
	
	@Test
	public void getBidsByUserSingleBid(){
		auctionService.checkAndBid(auctionAd, bidder1, 901000);
		
		Map<Ad, SortedSet<Bid>> bids = auctionService.getBidsByUser(bidder1);
		assertTrue(bids.containsKey(auctionAd));
		assertEquals(1, bids.get(auctionAd).size());
		assertEquals(901000, bids.get(auctionAd).first().getAmount());
	}
	
	@Test
	public void getBidsByUserMultipleBids(){
		auctionService.checkAndBid(auctionAd, bidder1, 901000);
		auctionService.checkAndBid(auctionAd, bidder1, 902000);
		
		Map<Ad, SortedSet<Bid>> bids = auctionService.getBidsByUser(bidder1);
		assertTrue(bids.containsKey(auctionAd));
		assertEquals(2, bids.get(auctionAd).size());
		assertEquals(902000, bids.get(auctionAd).first().getAmount());
	}
	
	public User createUser(String email, String password, String firstName,
			String lastName, Gender gender, AccountType type) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		user.addUserRole(role);
		user.setAccountType(type);
		return user;
	}
};