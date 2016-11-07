package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class AdServiceTest {

	@Autowired
	private AdService adService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private AdDao adDao;

	/**
	 * In order to test the saved ad, I need to get it back from the DB again,
	 * so these two methods need to be tested together, normally we want to test
	 * things isolated of course. Testing just the returned ad from saveFrom()
	 * wouldn't answer the question whether the ad has been saved correctly to
	 * the db.
	 * 
	 * @throws ParseException
	 */
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

		assertEquals(0, result.compareTo(ad.getMoveInDate()));
		
		adDao.delete(ad);
	}

	@Test
	public void getAdById() {
		Ad ad = adService.getAdById(4);
		assertEquals(ad.getId(), 4);
	}

	@Test
	public void getAllAds() {
		// return all Ads in DB
		int maxInt = 2147483647; // all ads should be cheaper than that value
		Iterable<Ad> adsInDB = adDao.findByPriceLessThanAndBuyMode(2147483647, BuyMode.BUY);
		int acctualAdNumber = countIterable(adsInDB);
		
		Iterable<Ad> ads = adService.getAllAds();
		int countReturnedAds = countIterable(ads);
		// convert to List
		ArrayList<Ad> adList = (ArrayList) ads;

		// assert number of returned ads equals number in DB
		assertEquals(countReturnedAds, acctualAdNumber);
	}

	@Test
	public void queryResults() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrice(500);
		searchForm.setRadius(5);
		searchForm.setBalcony(true);
		Type[] types = { Type.APARTMENT };
		searchForm.setTypes(types);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList) queryedAds;

		assertEquals(1, adList.size());
		assertEquals(1, adList.get(0).getId());
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
		ArrayList<Ad> adList = (ArrayList) queryedAds;
		
		Iterable<Ad> queryedAds2 = adService.queryResults(searchForm2, BuyMode.BUY);
		ArrayList<Ad> adList2 = (ArrayList) queryedAds2;

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
		
		searchForm.setBalcony(true);	// does only filter if true
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
		int adsQuerried = countIterable(queryedAds);
		
		// iterate over each returned ad and check if the search criteria are fulfilled
		for (Ad ad: queryedAds) {
			assertTrue(ad.getPrice()<1000000); // price of form still refers to PrizePerMonth
			assertTrue(ad.getType().equals(Type.APARTMENT) ||ad.getType().equals(Type.HOUSE) ||
					ad.getType().equals(Type.STUDIO) ||ad.getType().equals(Type.VILLA));
			assertTrue(ad.getBalcony());
			assertTrue(ad.getFloorLevel() > 0 && ad.getFloorLevel()<7);
			assertTrue(ad.getDistancePublicTransport()>= 0 && ad.getDistancePublicTransport()<=1000);
			assertTrue(ad.getDistanceSchool()>= 0 && ad.getDistanceSchool()<=1000);
			assertTrue(ad.getDistanceShopping()>= 0 && ad.getDistanceShopping()<=1000);
			assertTrue(ad.getNumberOfBath()>= 1 && ad.getNumberOfBath()<=3);
			assertTrue(ad.getBuildYear()<=2000);
			assertTrue(ad.getRenovationYear()>= 1960);
			assertEquals(ad.getInfrastructureType(), InfrastructureType.CABLE);
			assertTrue(ad.getSquareFootage()>= 30 && ad.getSquareFootage()<=60);
			
			assertTrue((ad.getMoveInDate().before(latestMoveinDate) ||  ad.getMoveInDate().after(earlyestMoveinDate)) 
					|| ad.getMoveInDate().equals(latestMoveinDate) || ad.getMoveInDate().equals(earlyestMoveinDate));
		}
		
		
	}
	
	@Test
	public void getNewestAds() {
		Iterable<Ad> newestdAds = adService.getNewestAds(3, BuyMode.BUY);
		ArrayList<Ad> listNewestAds = (ArrayList) newestdAds;

		assertEquals(listNewestAds.size(), 3);

		
		// Note: 
		assertTrue("Unexpected value for listNewestAds.get(0): " + listNewestAds.get(0),
				listNewestAds.get(0).getTitle().equals("Nice studio") ||
				listNewestAds.get(0).getTitle().equals("Olten Residence") ||
				listNewestAds.get(0).getTitle().equals("Beautiful studio in Aarau") ||
				listNewestAds.get(0).getTitle().equals("Malibu-style Beachhouse"));
		assertTrue("Unexpected value for listNewestAds.get(1): " + listNewestAds.get(1),
				listNewestAds.get(1).getTitle().equals("Nice studio") ||
				listNewestAds.get(1).getTitle().equals("Beautiful studio in Aarau") ||
				listNewestAds.get(1).getTitle().equals("Olten Residence") ||
				listNewestAds.get(1).getTitle().equals("Malibu-style Beachhouse"));
		assertTrue("Unexpected value for listNewestAds.get(2): " + listNewestAds.get(2),
				listNewestAds.get(2).getTitle().equals("Nice studio") ||
				listNewestAds.get(2).getTitle().equals("Beautiful studio in Aarau") ||
				listNewestAds.get(2).getTitle().equals("Olten Residence") ||
				listNewestAds.get(2).getTitle().equals("Malibu-style Beachhouse"));
		
		//assertEquals("Malibu-style Beachhouse", listNewestAds.get(3).getTitle());
	}

	@Test
	public void getAdsByUser() {
		User user = userDao.findByUsername("ese@unibe.ch");

		Iterable<Ad> adsFromService = adService.getAdsByUser(user);
		ArrayList<Ad> listFromService = (ArrayList) adsFromService;

		Iterable<Ad> adsFromDB = adDao.findByUser(user);
		ArrayList<Ad> listFromDB = (ArrayList) adsFromDB;

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

	private User createUser(String email, String password, String firstName, String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
	
	// method to count all iterables
	<T> int countIterable(Iterable<T> iterable) {
		int countMessages = 0;
		for (T element : iterable ) {
			countMessages++;
		}
		return countMessages;
	}

}
