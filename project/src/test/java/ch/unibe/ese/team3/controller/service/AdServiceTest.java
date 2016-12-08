package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.util.ListUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@Transactional
public class AdServiceTest {

	@Autowired
	private AdService adService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private AdDao adDao;

	SearchForm basicSearchForm;
	Ad searchAd;
	User adPlacer;

	/**
	 * In order to test the saved ad, I need to get it back from the DB again,
	 * so these two methods need to be tested together, normally we want to test
	 * things isolated of course. Testing just the returned ad from saveFrom()
	 * wouldn't answer the question whether the ad has been saved correctly to
	 * the db.
	 * 
	 * @throws ParseException
	 */

	@Before
	public void setup() {
		adPlacer = userDao.findByUsername("ese@unibe.ch");

		// create searchform with basic criteria
		basicSearchForm = new SearchForm();
		basicSearchForm.setCity("6001 - Luzern");
		basicSearchForm.setPrice(2000000);
		basicSearchForm.setRadius(300);
		Type[] types = { Type.APARTMENT, Type.HOUSE, Type.LOFT, Type.STUDIO, Type.VILLA };
		basicSearchForm.setTypes(types);

		// ad to search for
		Date date = new Date();
		searchAd = new Ad();
		searchAd.setZipcode(3012);
		searchAd.setBuyMode(BuyMode.BUY);
		searchAd.setMoveInDate(convertStringToDate("01-01-2016"));
		searchAd.setCreationDate(date);
		searchAd.setPrice(1000000);
		searchAd.setSquareFootage(80);
		searchAd.setType(Type.APARTMENT);
		searchAd.setRoomDescription("test");
		searchAd.setUser(adPlacer);
		searchAd.setTitle("searchAdTest");
		searchAd.setStreet("Hochfeldstrasse 44");
		searchAd.setCity("Bern");

		searchAd.setDishwasher(false);
		searchAd.setElevator(false);
		searchAd.setGarage(false);
		searchAd.setBalcony(false);
		searchAd.setParking(false);

		searchAd.setFloorLevel(3);
		searchAd.setSquareFootage(100);
		searchAd.setNumberOfBath(2);
		searchAd.setNumberOfRooms(5);
		searchAd.setDistancePublicTransport(900);
		searchAd.setDistanceSchool(100);
		searchAd.setDistanceShopping(450);
		searchAd.setRenovationYear(1990);
		searchAd.setBuildYear(1940);

		searchAd.setInfrastructureType(InfrastructureType.SATELLITE);

		adDao.save(searchAd);
	}

	@Test
	public void saveFromAndGetById() throws ParseException {
		// Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");

		// new criteria
		// test newly added fields
		placeAdForm.setDishwasher(true);
		placeAdForm.setBalcony(false);
		placeAdForm.setGarage(true);
		placeAdForm.setParking(true);
		placeAdForm.setElevator(false);

		placeAdForm.setNumberOfBath(2);
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setInfrastructureType(InfrastructureType.CABLE);

		placeAdForm.setDistancePublicTransport(1000);
		placeAdForm.setDistanceSchool(2000);
		placeAdForm.setDistanceShopping(1500);

		placeAdForm.setBuildYear(1960);
		placeAdForm.setRenovationYear(1980);

		placeAdForm.setFloorLevel(5);
		placeAdForm.setNumberOfRooms(5);

		// set auction specific fields (required for saveForm)
		placeAdForm.setStartDate("03.10.2016");
		placeAdForm.setEndDate("03.12.2016");

		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");

		User hans = createUser("hans@kanns.ch", "password", "Hans", "Kanns", Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);

		adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);

		Ad ad = new Ad();
		Iterable<Ad> ads = adService.getAllAds();
		Iterator<Ad> iterator = ads.iterator();

		while (iterator.hasNext()) {
			ad = iterator.next();
		}

		// Testing
		assertEquals("Bern", ad.getCity());
		assertEquals(3018, ad.getZipcode());
		assertEquals("Test Room description", ad.getRoomDescription());
		assertEquals(600, ad.getPrice());
		assertEquals(50, ad.getSquareFootage());
		assertEquals("title", ad.getTitle());
		assertEquals("Hauptstrasse 13", ad.getStreet());

		// test newly added fields
		assertEquals(ad.getDishwasher(), true);
		assertEquals(ad.getBalcony(), false);
		assertEquals(ad.getGarage(), true);
		assertEquals(ad.getParking(), true);
		assertEquals(ad.getElevator(), false);

		assertEquals(ad.getNumberOfBath(), 2);
		assertEquals(ad.getType(), Type.APARTMENT);
		assertEquals(ad.getBuyMode(), BuyMode.BUY);
		assertEquals(ad.getInfrastructureType(), InfrastructureType.CABLE);

		assertEquals(ad.getDistancePublicTransport(), 1000);
		assertEquals(ad.getDistanceSchool(), 2000);
		assertEquals(ad.getDistanceShopping(), 1500);

		assertEquals(ad.getBuildYear(), 1960);
		assertEquals(ad.getRenovationYear(), 1980);

		assertEquals(ad.getFloorLevel(), 5);
		assertEquals(ad.getNumberOfRooms(), 5);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date result = df.parse("2015-02-27");

		// Hack to avoid datetime issues when comparing
		assertEquals(result.toString(), ad.getMoveInDate().toString());
	}

	@Test
	public void saveFromWithCoordinatesAndVisitsTest() throws ParseException {
		// Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3072 - Ostermundigen");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Forelstrasse 22");
		placeAdForm.setMoveInDate("27-02-2015");

		// new criteria
		// test newly added fields
		placeAdForm.setDishwasher(false);
		placeAdForm.setBalcony(true);
		placeAdForm.setGarage(false);
		placeAdForm.setParking(false);
		placeAdForm.setElevator(true);

		placeAdForm.setNumberOfRooms(5);

		List<String> visits = new ArrayList<String>();
		String visit = "01-11-2016 ; 14:45 ; 15:55";
		visits.add(visit);

		placeAdForm.setVisits(visits);

		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");

		User hans = createUser("hansilein@kanns.ch", "password", "Hans", "Kanns", Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);

		adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);

		Ad ad = new Ad();
		Iterable<Ad> ads = adService.getAllAds();
		Iterator<Ad> iterator = ads.iterator();

		while (iterator.hasNext()) {
			ad = iterator.next();
		}

		assertEquals(46.960744, ad.getLatitude(), 0.00001);
		assertEquals(7.483973, ad.getLongitude(), 0.00001);
	}

	@Test
	public void getAdById() {
		Ad ad = adService.getAdById(4);
		assertEquals(ad.getId(), 4);
	}

	@Test
	public void getAllAds() {
		Iterable<Ad> adsInDBbuy = adDao.findByPriceLessThanAndBuyMode(2147483647, BuyMode.BUY);
		int numberOfAdsBuy = ListUtils.countIterable(adsInDBbuy);
		Iterable<Ad> adsInDBrent = adDao.findByPriceLessThanAndBuyMode(2147483647, BuyMode.RENT);
		int numberOfAdsRent = ListUtils.countIterable(adsInDBrent);

		int acctualNumberOfAds = numberOfAdsBuy + numberOfAdsRent;

		Iterable<Ad> ads = adService.getAllAds();
		int countReturnedAds = ListUtils.countIterable(ads);

		// assert number of returned ads equals number in DB
		assertEquals(acctualNumberOfAds, countReturnedAds);
	}

	@Test
	public void queryResults() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrice(700);
		searchForm.setRadius(5);
		Type[] types = { Type.APARTMENT };
		searchForm.setTypes(types);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList<Ad>) queryedAds;

		searchForm.setBalcony(false);
		searchForm.setGarage(true);
		searchForm.setDishwasher(true);
		searchForm.setElevator(true);
		searchForm.setGarage(false);

		assertEquals(1, adList.size());
		assertEquals("Cheap studio in Bern!", adList.get(0).getTitle());
	}

	@Test
	public void queryResultsWithouType() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrice(700);
		searchForm.setRadius(5);
		searchForm.setTypes(new Type[0]);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList<Ad>) queryedAds;

		assertEquals(1, adList.size());
		assertEquals("Cheap studio in Bern!", adList.get(0).getTitle());
	}

	@Test
	public void testFilterBalcony() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrice(6000);
		searchForm.setRadius(1000);
		searchForm.setBalcony(true);
		Type[] types = { Type.APARTMENT };
		searchForm.setTypes(types);

		SearchForm searchForm2 = new SearchForm();
		searchForm2.setCity("3001 - Bern");
		searchForm2.setPrice(6000);
		searchForm2.setRadius(1000);
		searchForm2.setBalcony(false);
		searchForm2.setTypes(types);

		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList<Ad>) queryedAds;

		Iterable<Ad> queryedAds2 = adService.queryResults(searchForm2, BuyMode.BUY);
		ArrayList<Ad> adList2 = (ArrayList<Ad>) queryedAds2;

		assertNotEquals(adList.get(0).getId(), adList2.get(0).getId());
	}

	@Test
	public void extendedQuerryWithNewCriteria() throws ParseException {
		Type[] types = { Type.APARTMENT, Type.HOUSE, Type.STUDIO, Type.VILLA };

		// convert movein dates to dates
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		Date earlyestMoveinDate = formatter.parse("03.10.2014");
		Date latestMoveinDate = formatter.parse("01.03.2015");

		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrice(1000000);
		searchForm.setRadius(400);

		searchForm.setTypes(types);

		searchForm.setBalcony(true); // does only filter if true
		searchForm.setElevator(false);
		searchForm.setDishwasher(false);
		searchForm.setGarage(false);
		searchForm.setParking(false);

		searchForm.setNumberOfBathMin(1);
		searchForm.setNumberOfBathMin(2);

		searchForm.setFloorLevelMin(1);
		searchForm.setFloorLevelMax(6);
		searchForm.setDistancePublicTransportMin(0);
		searchForm.setDistancePublicTransportMax(1000);
		searchForm.setDistanceSchoolMin(0);
		searchForm.setDistanceSchoolMax(1000);
		searchForm.setDistanceShoppingMin(0);
		searchForm.setDistanceShoppingMax(1000);

		searchForm.setBuildYearMax(2000);
		searchForm.setBuildYearMin(1900);
		searchForm.setSquareFootageMin(30);
		searchForm.setSquareFootageMax(60);

		searchForm.setEarliestMoveInDate("03.10.2014");
		searchForm.setLatestMoveInDate("01.03.2015");

		searchForm.setInfrastructureType(InfrastructureType.CABLE);

		searchForm.setRenovationYearMin(1960);

		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		// iterate over each returned ad and check if the search criteria are
		// fulfilled
		for (Ad ad : queryedAds) {
			assertTrue(ad.getPrice() < 1000000);
			assertTrue(ad.getType().equals(Type.APARTMENT) || ad.getType().equals(Type.HOUSE)
					|| ad.getType().equals(Type.STUDIO) || ad.getType().equals(Type.VILLA));
			assertTrue(ad.getBalcony());
			assertTrue(ad.getFloorLevel() > 0 && ad.getFloorLevel() < 7);
			assertTrue(ad.getDistancePublicTransport() >= 0 && ad.getDistancePublicTransport() <= 1000);
			assertTrue(ad.getDistanceSchool() >= 0 && ad.getDistanceSchool() <= 1000);
			assertTrue(ad.getDistanceShopping() >= 0 && ad.getDistanceShopping() <= 1000);
			assertTrue(ad.getNumberOfBath() >= 1 && ad.getNumberOfBath() <= 3);
			assertTrue(ad.getBuildYear() <= 2000);
			assertTrue(ad.getRenovationYear() >= 1960);
			assertEquals(ad.getInfrastructureType(), InfrastructureType.CABLE);
			assertTrue(ad.getSquareFootage() >= 30 && ad.getSquareFootage() <= 60);

			assertTrue((ad.getMoveInDate().before(latestMoveinDate) || ad.getMoveInDate().after(earlyestMoveinDate))
					|| ad.getMoveInDate().equals(latestMoveinDate) || ad.getMoveInDate().equals(earlyestMoveinDate));
		}

	}

	@Test
	public void getNewestAds() throws ParseException {
		// create 3 new ads

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> filePaths = new ArrayList<>();

		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("ad1");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");
		placeAdForm.setStartDate("03.10.2016");
		placeAdForm.setEndDate("03.12.2016");

		adService.saveFrom(placeAdForm, filePaths, userDao.findByUsername("ese@unibe.ch"), BuyMode.BUY);
		placeAdForm.setTitle("ad2");
		adService.saveFrom(placeAdForm, filePaths, userDao.findByUsername("ese@unibe.ch"), BuyMode.BUY);
		placeAdForm.setTitle("ad3");
		adService.saveFrom(placeAdForm, filePaths, userDao.findByUsername("ese@unibe.ch"), BuyMode.BUY);

		Iterable<Ad> newestdAds = adService.getNewestAds(3, BuyMode.BUY);
		ArrayList<Ad> listNewestAds = (ArrayList<Ad>) newestdAds;

		assertEquals(listNewestAds.size(), 3);

		assertEquals("ad3", listNewestAds.get(0).getTitle());
		assertEquals("ad2", listNewestAds.get(1).getTitle());
		assertEquals("ad1", listNewestAds.get(2).getTitle());

	}

	@Test
	public void getAdsByUser() {
		User user = userDao.findByUsername("ese@unibe.ch");

		Iterable<Ad> adsFromService = adService.getAdsByUser(user);
		ArrayList<Ad> listFromService = (ArrayList<Ad>) adsFromService;

		Iterable<Ad> adsFromDB = adDao.findByUser(user);
		ArrayList<Ad> listFromDB = (ArrayList<Ad>) adsFromDB;

		// make sure same number of elements are returned
		assertEquals(listFromService.size(), listFromDB.size());

		// make sure right ads are returned
		assertEquals(listFromDB.get(0).getId(), listFromService.get(0).getId());
		assertEquals(listFromDB.get(1).getId(), listFromService.get(1).getId());
		assertEquals(listFromDB.get(2).getId(), listFromService.get(2).getId());
	}

	/*
	 * @Test public void checkIfAlreadyAdded() {
	 * assertTrue(adService.checkIfAlreadyAdded("ese@unibe.ch", alreadyAdded));
	 * }
	 */
	@Test
	public void expiredAuctionNotVisible() {

		// Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.LOFT);
		placeAdForm.setRoomDescription("This is an Loft for Auction");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("AuctionLoft");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");

		// new criteria
		// test newly added fields
		placeAdForm.setDishwasher(true);
		placeAdForm.setBalcony(false);
		placeAdForm.setGarage(true);
		placeAdForm.setParking(true);
		placeAdForm.setElevator(false);

		placeAdForm.setNumberOfBath(2);
		placeAdForm.setType(Type.LOFT);
		placeAdForm.setInfrastructureType(InfrastructureType.CABLE);

		placeAdForm.setDistancePublicTransport(1000);
		placeAdForm.setDistanceSchool(2000);
		placeAdForm.setDistanceShopping(1500);

		placeAdForm.setBuildYear(1960);
		placeAdForm.setRenovationYear(1980);

		placeAdForm.setFloorLevel(5);
		placeAdForm.setNumberOfRooms(5);

		// set auction specific fields (required for saveForm)
		placeAdForm.setStartDate("03.10.2016");
		// yesterday expired
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -1);
		
		placeAdForm.setEndDate(String.format("%02d.%02d.%d", now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR)));
		placeAdForm.setAuction(true);

		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");

		User hans = createUser("hansi@kanns.ch", "password", "Hansi", "Kanns", Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);

		adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);

		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3018 - Bern");
		searchForm.setPrice(601);
		searchForm.setRadius(5);
		Type[] types = { Type.LOFT };
		searchForm.setTypes(types);
		searchForm.setDishwasher(true);
		searchForm.setBalcony(false);
		searchForm.setGarage(true);
		searchForm.setParking(true);
		searchForm.setElevator(false);

		searchForm.setRenovationYearMin(1979);
		searchForm.setRenovationYearMax(1981);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList<Ad>) queryedAds;
		
		assertEquals(0, adList.size());
	}

	@Test
	public void AuctionExpiredTodayVisible() {
		// Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.LOFT);
		placeAdForm.setRoomDescription("This is an Loft for Auction");
		placeAdForm.setPrice(6000);
		placeAdForm.setSquareFootage(100);
		placeAdForm.setTitle("AuctionLoftVisibel");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");

		// new criteria
		// test newly added fields
		placeAdForm.setDishwasher(true);
		placeAdForm.setBalcony(true);
		placeAdForm.setGarage(true);
		placeAdForm.setParking(true);
		placeAdForm.setElevator(true);

		placeAdForm.setNumberOfBath(3);
		placeAdForm.setType(Type.LOFT);
		placeAdForm.setInfrastructureType(InfrastructureType.CABLE);

		placeAdForm.setDistancePublicTransport(100);
		placeAdForm.setDistanceSchool(100);
		placeAdForm.setDistanceShopping(100);

		placeAdForm.setBuildYear(1970);
		placeAdForm.setRenovationYear(2000);

		placeAdForm.setFloorLevel(5);
		placeAdForm.setNumberOfRooms(5);

		// set auction specific fields (required for saveForm)
		placeAdForm.setStartDate("03.10.2016");
		// today expire
		Calendar now = Calendar.getInstance();
		placeAdForm.setEndDate(String.format("%02d.%02d.%d", now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR)));

		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");

		User hans = createUser("hänsu@kanns.ch", "password", "Hans", "Kanns", Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);

		adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);

		Ad ad = new Ad();
		Iterable<Ad> ads = adService.getAllAds();
		Iterator<Ad> iterator = ads.iterator();

		while (iterator.hasNext()) {
			ad = iterator.next();
		}

		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3018 - Bern");
		searchForm.setPrice(6000);
		searchForm.setRadius(5);
		Type[] types = { Type.LOFT };
		searchForm.setTypes(types);
		searchForm.setDishwasher(true);
		searchForm.setBalcony(true);
		searchForm.setGarage(true);
		searchForm.setParking(true);
		searchForm.setElevator(true);

		searchForm.setRenovationYearMin(1999);
		searchForm.setRenovationYearMax(2001);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList<Ad>) queryedAds;

		assertEquals("AuctionLoftVisibel", adList.get(0).getTitle());
		assertFalse(ad.hasAuctionExpired());
	}

	// ---------------------------------------
	// test individual search criteria
	// ---------------------------------------

	@Test
	public void floorLevelInRange() {
		basicSearchForm.setFloorLevelMax(5);
		basicSearchForm.setFloorLevelMin(0);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void floorLevelOutOfRange() {
		basicSearchForm.setFloorLevelMax(2);
		basicSearchForm.setFloorLevelMin(0);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void squareFootageInRange() {
		basicSearchForm.setSquareFootageMax(101);
		basicSearchForm.setSquareFootageMin(90);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void squareFootageOutOfRange() {
		basicSearchForm.setSquareFootageMax(90);
		basicSearchForm.setSquareFootageMin(80);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void numberOfBathInRange() {
		basicSearchForm.setNumberOfBathMax(4);
		basicSearchForm.setNumberOfBathMin(1);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void numberOfBathOutOfRange() {
		basicSearchForm.setNumberOfBathMax(1);
		basicSearchForm.setNumberOfBathMin(0);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void numberOfRoomsInRange() {
		basicSearchForm.setNumberOfRoomsMax(5);
		basicSearchForm.setNumberOfRoomsMin(0);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void numberOfRoomsOutOfRange() {
		basicSearchForm.setNumberOfRoomsMax(4);
		basicSearchForm.setNumberOfRoomsMin(4);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void distancePublicTransportInRange() {
		basicSearchForm.setDistancePublicTransportMax(2000);
		basicSearchForm.setDistancePublicTransportMin(800);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void distancePublicTransportOutOfRange() {
		basicSearchForm.setDistancePublicTransportMax(2000);
		basicSearchForm.setDistancePublicTransportMin(1000);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void distanceSchoolInRange() {
		basicSearchForm.setDistanceSchoolMax(200);
		basicSearchForm.setDistanceSchoolMin(0);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void distanceSchoolOutOfRange() {
		basicSearchForm.setDistanceSchoolMax(2000);
		basicSearchForm.setDistanceSchoolMin(200);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void distanceShoppingInRange() {
		basicSearchForm.setDistanceShoppingMax(0);
		basicSearchForm.setDistanceShoppingMin(400);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void distanceShoppingOutOfRange() {
		basicSearchForm.setDistanceShoppingMax(300);
		basicSearchForm.setDistanceShoppingMin(0);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void renovationYearInRange() {
		basicSearchForm.setRenovationYearMax(0);
		basicSearchForm.setRenovationYearMin(1900);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void renovationYearOutOfRange() {
		basicSearchForm.setRenovationYearMax(1980);
		basicSearchForm.setRenovationYearMin(1900);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void buildYearInRange() {
		basicSearchForm.setBuildYearMax(2015);
		basicSearchForm.setBuildYearMin(1930);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void buildYearOutOfRange() {
		basicSearchForm.setBuildYearMax(0);
		basicSearchForm.setBuildYearMin(1980);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void searchDishwasher() {
		basicSearchForm.setDishwasher(true);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void searchElevator() {
		basicSearchForm.setElevator(true);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void searchGarage() {
		basicSearchForm.setGarage(true);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void searchBalcony() {
		basicSearchForm.setBalcony(true);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void searchParking() {
		basicSearchForm.setParking(true);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void infrastructureTypeMatch() {
		basicSearchForm.setInfrastructureType(InfrastructureType.SATELLITE);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void infrastructureTypeNotMatch() {
		basicSearchForm.setInfrastructureType(InfrastructureType.CABLE);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void flatTypeNotMatch() {
		Type[] types = { Type.HOUSE };
		basicSearchForm.setTypes(types);

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void moveInDateInRange() {
		basicSearchForm.setEarliestMoveInDate("01-11-2015");
		basicSearchForm.setLatestMoveInDate("01-02-2016");

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void moveInDateOutOfRange() {
		basicSearchForm.setEarliestMoveInDate("02-01-2016");
		basicSearchForm.setLatestMoveInDate("05-01-2016");

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertFalse(searchAdInResults);
	}

	@Test
	public void onlyEarliestMoveDateSet() {
		basicSearchForm.setEarliestMoveInDate("02-11-2015");

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}

	@Test
	public void onlyLatestMoveDateSet() {
		basicSearchForm.setLatestMoveInDate("05-01-2016");

		Iterable<Ad> filteredAd = adService.queryResults(basicSearchForm, BuyMode.BUY);

		Boolean searchAdInResults = isSearchAdInResults(searchAd, filteredAd);

		assertTrue(searchAdInResults);
	}
	
	@Test
	public void checkIfAllreadyAddedFalse(){
		
		String email = "hansruediMeier@gmail.com";
		Iterable<User> itrUsers = userDao.findAll();
		Iterator<User> itr = itrUsers.iterator();
		String alreadyAdded="";
		
		while(itr.hasNext()){
			alreadyAdded += itr.next().getEmail();
		}
		
		
		assertFalse(adService.checkIfAlreadyAdded(email, alreadyAdded));
		
	}

	@Test
	public void checkIfAllreadyAddedTrue(){
		
		String email = "jane@doe.com";
		Iterable<User> itrUsers = userDao.findAll();
		Iterator<User> itr = itrUsers.iterator();
		String alreadyAdded="";
		
		while(itr.hasNext()){
			alreadyAdded += itr.next().getEmail();
			
		}
		
		
		assertTrue(adService.checkIfAlreadyAdded(email, alreadyAdded));
		
	}
	// checks if the created ad "searchAd" is returned by the filter function
	private Boolean isSearchAdInResults(Ad searchAd, Iterable<Ad> filteredAd) {
		Boolean searchAdContained = false;
		for (Ad ad : filteredAd) {
			if (searchAd.equals(ad)) { // string is title of "searchAdTest"
				searchAdContained = true;
			}
		}
		return searchAdContained;
	}

	// ---------------------------------------
	// Helper methods
	// ---------------------------------------
	private User createUser(String email, String password, String firstName, String lastName, Gender gender) {
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
		return user;
	}

	private Date convertStringToDate(String date) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date earliestMoveInDate = formatter.parse(date);
			return earliestMoveInDate;
		} catch (Exception e) {
		}
		return null;
	}

}
