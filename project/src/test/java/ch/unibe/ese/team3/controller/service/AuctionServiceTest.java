package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import ch.unibe.ese.team3.model.Message;
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
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
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
	MessageService messageService;

	private User bidder1, bidder2, premiumBidder, purchaser1, purchaser2, auctionOwner1, auctionOwner2;

	private Ad auctionAd1, auctionAd2;

	@Before
	public void setUp() throws ParseException {

		bidder1 = createUser("bidder1@ithaca.com", "password", "Bidder1", "Bidder1", Gender.MALE, AccountType.BASIC);
		bidder2 = createUser("bidder2@ithaca.com", "password", "Bidder1", "Bidder1", Gender.MALE, AccountType.BASIC);
		premiumBidder = createUser("premiumbidder@ithaca.com", "password", "Premium", "Bidder", Gender.FEMALE, AccountType.PREMIUM);
		auctionOwner2 = createUser("auctionowner@ithaca.com", "password", "Auction", "Owner", Gender.FEMALE, AccountType.BASIC);
		
		bidder1.setAboutMe("About");
		bidder2.setAboutMe("About");
		premiumBidder.setAboutMe("About");
		auctionOwner2.setAboutMe("About");

		userDao.save(bidder1);
		userDao.save(bidder2);
		userDao.save(premiumBidder);
		userDao.save(auctionOwner2);

		purchaser1 = bidder1;
		purchaser2 = bidder2;
		auctionOwner1 = userDao.findByUsername("jane@doe.com");

		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

		createAuctionAd1(formatter);
		createAuctionAd2(formatter);
	}

	private void createAuctionAd1(SimpleDateFormat formatter) throws ParseException {
		auctionAd1 = new Ad();
		auctionAd1.setZipcode(3506);
		auctionAd1.setType(Type.HOUSE);
		auctionAd1.setBuyMode(BuyMode.BUY);
		auctionAd1.setMoveInDate(formatter.parse("01.01.2017"));
		auctionAd1.setCreationDate(formatter.parse("12.11.2016"));
		auctionAd1.setPrice(1200000);
		auctionAd1.setSquareFootage(120);
		auctionAd1.setRoomDescription("A nice house out in the green. Just a few minutes away from the train station.");
		auctionAd1.setUser(auctionOwner1);
		auctionAd1.setTitle("New renovated house");
		auctionAd1.setStreet("Trogmattweg 3");
		auctionAd1.setCity("Grosshöchstetten");
		auctionAd1.setNumberOfBath(3);

		auctionAd1.setBalcony(true);
		auctionAd1.setGarage(true);
		auctionAd1.setDishwasher(true);
		auctionAd1.setElevator(false);
		auctionAd1.setParking(false);

		auctionAd1.setBuildYear(1984);
		auctionAd1.setRenovationYear(2015);
		auctionAd1.setDistancePublicTransport(400);
		auctionAd1.setDistanceSchool(600);
		auctionAd1.setDistanceShopping(600);
		auctionAd1.setFloorLevel(3);
		auctionAd1.setNumberOfRooms(8);
		auctionAd1.setInfrastructureType(InfrastructureType.CABLE);

		auctionAd1.setAuction(true);
		auctionAd1.setStartPrice(900000);
		auctionAd1.setIncreaseBidPrice(1000);
		auctionAd1.setcurrentAuctionPrice(auctionAd1.getStartPrice() + auctionAd1.getIncreaseBidPrice());
		auctionAd1.setStartDate(formatter.parse("10.10.2016"));
		auctionAd1.setEndDate(formatter.parse("12.12.2016"));
		
		adDao.save(auctionAd1);
	}
	
	private void createAuctionAd2(SimpleDateFormat formatter) throws ParseException {
		auctionAd2 = new Ad();
		auctionAd2.setZipcode(3506);
		auctionAd2.setType(Type.APARTMENT);
		auctionAd2.setBuyMode(BuyMode.BUY);
		auctionAd2.setMoveInDate(formatter.parse("01.01.2017"));
		auctionAd2.setCreationDate(formatter.parse("12.11.2016"));
		auctionAd2.setPrice(1200000);
		auctionAd2.setSquareFootage(120);
		auctionAd2.setRoomDescription("A nice house out in the green. Just a few minutes away from the train station.");
		auctionAd2.setUser(auctionOwner2);
		auctionAd2.setTitle("New renovated apartment");
		auctionAd2.setStreet("Dorfstrasse 6");
		auctionAd2.setCity("Grosshöchstetten");
		auctionAd2.setNumberOfBath(3);

		auctionAd2.setBalcony(true);
		auctionAd2.setGarage(true);
		auctionAd2.setDishwasher(true);
		auctionAd2.setElevator(false);
		auctionAd2.setParking(false);

		auctionAd2.setBuildYear(1994);
		auctionAd2.setRenovationYear(2005);
		auctionAd2.setDistancePublicTransport(500);
		auctionAd2.setDistanceSchool(700);
		auctionAd2.setDistanceShopping(800);
		auctionAd2.setFloorLevel(6);
		auctionAd2.setNumberOfRooms(3);
		auctionAd2.setInfrastructureType(InfrastructureType.FOC);

		auctionAd2.setAuction(true);
		auctionAd2.setStartPrice(900000);
		auctionAd2.setIncreaseBidPrice(1000);
		auctionAd2.setcurrentAuctionPrice(auctionAd2.getStartPrice() + auctionAd2.getIncreaseBidPrice());
		auctionAd2.setStartDate(formatter.parse("10.10.2016"));
		auctionAd2.setEndDate(formatter.parse("12.12.2016"));
		
		adDao.save(auctionAd2);
	}

	@Test
	public void singleBidSuccessful() {
		int amount = 901000;

		boolean bidSuccess = auctionService.checkAndBid(auctionAd1, bidder1, amount);
		assertTrue(bidSuccess);
		assertEquals(auctionAd1.getCurrentAuctionPrice(), 902000);

		Bid firstBid = bidDao.findTopByAdOrderByAmountDesc(auctionAd1);
		assertEquals(901000, firstBid.getAmount());
		assertEquals(bidder1, firstBid.getBidder());
	}

	@Test
	public void singleBidNotSuccessful() {
		int amount = 900000;

		boolean bidSuccess = auctionService.checkAndBid(auctionAd1, bidder1, amount);
		assertFalse(bidSuccess);
	}

	@Test
	public void multipleBidsSuccessful() {

		int amount1 = 901000;
		int amount2 = 902000;

		boolean successBid1 = auctionService.checkAndBid(auctionAd1, bidder1, amount1);
		boolean successBid2 = auctionService.checkAndBid(auctionAd1, bidder2, amount2);

		assertTrue(successBid1);
		assertTrue(successBid2);

		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(auctionAd1);
		assertEquals(amount2, maxBid.getAmount());
		assertEquals(bidder2, maxBid.getBidder());
	}

	@Test
	public void multipleBidsNotSuccessful() {
		int amount1 = 901000;
		int amount2 = 901000;

		boolean successBid1 = auctionService.checkAndBid(auctionAd1, bidder1, amount1);
		boolean successBid2 = auctionService.checkAndBid(auctionAd1, bidder2, amount2);

		assertTrue(successBid1);
		assertFalse(successBid2);

		Bid maxBid = bidDao.findTopByAdOrderByAmountDesc(auctionAd1);
		assertEquals(amount1, maxBid.getAmount());
		assertEquals(bidder1, maxBid.getBidder());
	}

	@Test
	public void bidOnExpiredAuction() {
		auctionAd1.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd1);

		int amount = 901000;

		boolean successBid = auctionService.checkAndBid(auctionAd1, bidder1, amount);
		assertFalse(successBid);
		assertEquals(null, bidDao.findTopByAdOrderByAmountDesc(auctionAd1));
	}

	@Test
	public void bidOnUnstartedAuction() {
		auctionAd1.setStartDate(
				Date.from(LocalDate.now().plusDays(10l).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd1);

		int amount = 901000;

		boolean successBid = auctionService.checkAndBid(auctionAd1, bidder1, amount);
		assertFalse(successBid);
		assertEquals(null, bidDao.findTopByAdOrderByAmountDesc(auctionAd1));
		assertEquals(1, auctionService.getNotYetRunningAuctionsForUser(auctionOwner1).size());
	}

	@Test
	public void bidOnOwnAuctionFails() {
		boolean success = auctionService.checkAndBid(auctionAd1, auctionOwner1, 901000);
		assertFalse(success);
	}

	@Test
	public void buyRequest() {
		boolean buySuccessful = auctionService.checkAndBuy(auctionAd1, purchaser1);

		assertTrue(buySuccessful);

		int count = ListUtils.countIterable(purchaseRequestDao.findByAd(auctionAd1));
		assertEquals(1, count);
	}

	@Test
	public void multipleBuyRequest() {
		boolean buySuccessful1 = auctionService.checkAndBuy(auctionAd1, purchaser1);
		boolean buySuccessful2 = auctionService.checkAndBuy(auctionAd1, purchaser2);

		assertTrue(buySuccessful1);
		assertTrue(buySuccessful2);

		int count = ListUtils.countIterable(purchaseRequestDao.findByAd(auctionAd1));
		assertEquals(2, count);
	}

	@Test
	public void buyRequestForOwnAuctionFails() {
		boolean success = auctionService.checkAndBuy(auctionAd1, auctionOwner1);
		assertFalse(success);
	}

	@Test
	public void stopAuction() {
		auctionService.stopAuction(auctionAd1);
		assertFalse(auctionAd1.isAvailableForAuction());
		assertEquals(2, auctionService.getStoppedAuctionsForUser(auctionOwner1).size());
	}

	@Test
	public void bidOnStoppedAuction() {
		auctionService.stopAuction(auctionAd1);
		boolean success = auctionService.checkAndBid(auctionAd1, bidder1, 901000);
		assertFalse(success);
	}

	@Test
	public void buyRequestOnStoppedAuction() {
		auctionService.stopAuction(auctionAd1);
		boolean success = auctionService.checkAndBuy(auctionAd1, purchaser1);
		assertFalse(success);
	}

	@Test
	public void resumeStoppedAuction() {
		auctionService.stopAuction(auctionAd1);
		auctionService.resumeAuction(auctionAd1);
		assertTrue(auctionAd1.isAvailableForAuction());
	}

	@Test
	public void bidOnResumedAuction() {
		auctionService.stopAuction(auctionAd1);
		auctionService.resumeAuction(auctionAd1);
		boolean success = auctionService.checkAndBid(auctionAd1, bidder1, 901000);
		assertTrue(success);
	}

	@Test
	public void bidOnResumedButExpiredAuction() {
		auctionAd1.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd1);

		auctionService.stopAuction(auctionAd1);
		auctionService.resumeAuction(auctionAd1);
		boolean success = auctionService.checkAndBid(auctionAd1, bidder1, 901000);
		assertFalse(success);
		assertEquals(2, auctionService.getExpiredAuctionsForUser(auctionOwner1).size());
	}

	@Test
	public void completeAuction() {
		auctionService.completeAuction(auctionAd1);
		assertTrue(auctionAd1.isAuctionCompleted());
		assertEquals(2, auctionService.getCompletedAuctionsForUser(auctionOwner1).size());
	}

	@Test
	public void bidOnCompletedAuction() {
		auctionService.completeAuction(auctionAd1);
		boolean success = auctionService.checkAndBid(auctionAd1, bidder1, 901000);
		assertFalse(success);
	}

	@Test
	public void purchaseCompletedAuction() {
		auctionService.completeAuction(auctionAd1);
		boolean success = auctionService.checkAndBuy(auctionAd1, purchaser1);
		assertFalse(success);
	}

	@Test
	public void getCompletedAuctions() {
		List<Ad> completedAuctions = auctionService.getCompletedAuctionsForUser(auctionOwner1);
		assertEquals(1, completedAuctions.size());
	}

	@Test
	public void getRunningAuctions() {
		List<Ad> runningAuctions = auctionService.getRunningAuctionsForUser(auctionOwner1);
		assertEquals(3, runningAuctions.size());
	}

	@Test
	public void getPausedAuctions() {
		List<Ad> pausedAuctions = auctionService.getStoppedAuctionsForUser(auctionOwner1);
		assertEquals(1, pausedAuctions.size());
	}

	@Test
	public void getExpiredAuctions() {
		List<Ad> expiredAuctions = auctionService.getExpiredAuctionsForUser(auctionOwner1);
		assertEquals(1, expiredAuctions.size());
	}

	@Test
	public void getNotYetRunningAuctions() {
		List<Ad> notRunningAuctions = auctionService.getNotYetRunningAuctionsForUser(auctionOwner1);
		assertEquals(0, notRunningAuctions.size());
	}

	@Test
	public void userHasSentPurchaseRequestForAd() {
		auctionService.checkAndBuy(auctionAd1, purchaser1);
		assertTrue(auctionService.hasUserSentBuyRequest(auctionAd1, purchaser1));
	}

	@Test
	public void userHasNotSentPurchaseRequestForAd() {
		assertFalse(auctionService.hasUserSentBuyRequest(auctionAd1, purchaser1));
	}

	@Test
	public void getBidsForAdNoBids() {
		List<Bid> bids = auctionService.getBidsForAd(auctionAd1);
		assertEquals(0, bids.size());
	}

	@Test
	public void getBidsForAdWithSingleBid() {
		int amount = 901000;

		auctionService.checkAndBid(auctionAd1, bidder1, amount);

		List<Bid> bids = auctionService.getBidsForAd(auctionAd1);
		assertEquals(1, bids.size());
	}

	@Test
	public void getBidsForAdWithMultipleBid() {
		int amount1 = 901000;
		int amount2 = 902000;

		auctionService.checkAndBid(auctionAd1, bidder1, amount1);
		auctionService.checkAndBid(auctionAd1, bidder2, amount2);

		List<Bid> bids = auctionService.getBidsForAd(auctionAd1);
		assertEquals(2, bids.size());

		assertEquals(amount2, bids.get(0).getAmount());
		assertEquals(amount1, bids.get(1).getAmount());
	}

	@Test
	public void getPurchaseRequestsForAdNoRequests() {
		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(auctionAd1);
		assertEquals(0, purchaseRequests.size());
	}

	@Test
	public void getPurchaseRequestsForAdWithSingleRequest() {
		auctionService.checkAndBuy(auctionAd1, bidder1);

		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(auctionAd1);
		assertEquals(1, purchaseRequests.size());
	}

	@Test
	public void getPurchaseRequestsForAdWithMultipleRequests() {
		auctionService.checkAndBuy(auctionAd1, bidder1);
		auctionService.checkAndBuy(auctionAd1, bidder2);

		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(auctionAd1);
		assertEquals(2, purchaseRequests.size());
	}

	@Test
	public void getMostRecentBidsLessThanTenBids() {
		int amount1 = 901000;
		int amount2 = 902000;

		auctionService.checkAndBid(auctionAd1, bidder1, amount1);
		auctionService.checkAndBid(auctionAd1, bidder2, amount2);

		List<Bid> bids = auctionService.getMostRecentBidsForAd(auctionAd1);
		assertEquals(2, bids.size());

		assertEquals(amount2, bids.get(0).getAmount());
		assertEquals(amount1, bids.get(1).getAmount());
	}

	@Test
	public void getMostRecentBidsMoreThanTenBids() {
		int amount = 901000;

		User[] users = new User[] { bidder1, bidder2 };

		for (int i = 0; i < 20; i++) {
			auctionService.checkAndBid(auctionAd1, users[i % 2], amount);
			amount += 1000;
		}

		List<Bid> bids = auctionService.getMostRecentBidsForAd(auctionAd1);
		assertEquals(10, bids.size());

		assertEquals(amount - 1000, bids.get(0).getAmount());
		assertEquals(amount - 10000, bids.get(9).getAmount());
	}

	@Test
	public void getBidsByUserNoBids() {
		Map<Ad, SortedSet<Bid>> bids = auctionService.getBidsByUser(bidder1);
		assertTrue(bids.isEmpty());
	}

	@Test
	public void getBidsByUserSingleBid() {
		auctionService.checkAndBid(auctionAd1, bidder1, 901000);

		Map<Ad, SortedSet<Bid>> bids = auctionService.getBidsByUser(bidder1);
		assertTrue(bids.containsKey(auctionAd1));
		assertEquals(1, bids.get(auctionAd1).size());
		assertEquals(901000, bids.get(auctionAd1).first().getAmount());
	}

	@Test
	public void getBidsByUserMultipleBids() {
		auctionService.checkAndBid(auctionAd1, bidder1, 901000);
		auctionService.checkAndBid(auctionAd1, bidder1, 902000);

		Map<Ad, SortedSet<Bid>> bids = auctionService.getBidsByUser(bidder1);
		assertTrue(bids.containsKey(auctionAd1));
		assertEquals(2, bids.get(auctionAd1).size());
		assertEquals(902000, bids.get(auctionAd1).first().getAmount());
	}

	@Test
	public void premiumUserOverbiddenShouldReceiveMessage() {
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(premiumBidder)));

		auctionService.checkAndBid(auctionAd1, premiumBidder, 901000);
		auctionService.checkAndBid(auctionAd1, bidder1, 902000);

		List<Message> inbox = ListUtils.convertToList(messageService.getInboxForUser(premiumBidder));

		assertEquals(1, inbox.size());
		assertEquals("Overbid", inbox.get(0).getSubject());
	}

	@Test
	public void noOverbiddenMessageForNonPremiumUser() {
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(bidder1)));

		auctionService.checkAndBid(auctionAd1, bidder1, 901000);
		auctionService.checkAndBid(auctionAd1, premiumBidder, 902000);

		List<Message> inbox = ListUtils.convertToList(messageService.getInboxForUser(bidder1));

		assertEquals(0, inbox.size());
	}
	
	@Test
	public void noBidsMessageToOwnerOnExpiredAuction(){
		auctionAd2.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd2);
		
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(auctionOwner2)));
		
		messageService.checkForExpiredAuctions();
		List<Message> inbox = ListUtils.convertToList(messageService.getInboxForUser(auctionOwner2));

		assertEquals(1, inbox.size());
		assertEquals("Auction expired", inbox.get(0).getSubject());
	}
	
	@Test
	public void auctionSuccessMessageToBidderAndOwner(){
		auctionService.checkAndBid(auctionAd2, bidder1, 901000);
		
		auctionAd2.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd2);
		
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(auctionOwner2)));
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(bidder1)));
		
		messageService.checkForExpiredAuctions();
		
		List<Message> inboxOwner = ListUtils.convertToList(messageService.getInboxForUser(auctionOwner2));
		List<Message> inboxBidder = ListUtils.convertToList(messageService.getInboxForUser(bidder1));
		
		assertEquals(1, inboxOwner.size());
		assertEquals("Your auction was successfully", inboxOwner.get(0).getSubject());
		
		assertEquals(1, inboxBidder.size());
		assertEquals("You won an auction", inboxBidder.get(0).getSubject());
	}
	
	@Test
	public void auctionSuccessMessageToBidderAndOwnerMultipleBidders(){
		auctionService.checkAndBid(auctionAd2, bidder2, 901000);
		auctionService.checkAndBid(auctionAd2, bidder1, 902000);
		
		auctionAd2.setEndDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		adDao.save(auctionAd2);
		
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(auctionOwner2)));
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(bidder1)));
		assertEquals(0, ListUtils.countIterable(messageService.getInboxForUser(bidder2)));
		
		messageService.checkForExpiredAuctions();
		
		List<Message> inboxOwner = ListUtils.convertToList(messageService.getInboxForUser(auctionOwner2));
		List<Message> inboxBidder1 = ListUtils.convertToList(messageService.getInboxForUser(bidder1));
		List<Message> inboxBidder2 = ListUtils.convertToList(messageService.getInboxForUser(bidder2));
		
		assertEquals(0, inboxBidder2.size());
		
		assertEquals(1, inboxOwner.size());
		assertEquals("Your auction was successfully", inboxOwner.get(0).getSubject());
		
		assertEquals(1, inboxBidder1.size());
		assertEquals("You won an auction", inboxBidder1.get(0).getSubject());
	}

	public User createUser(String email, String password, String firstName, String lastName, Gender gender,
			AccountType type) {
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