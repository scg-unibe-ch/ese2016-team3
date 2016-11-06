package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
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
		placeAdForm.setPrize(600);
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
		assertEquals(600, ad.getPrizePerMonth());
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
		Iterable<Ad> adsInDB = adDao.findByPrizePerMonthLessThanAndBuyMode(2147483647, BuyMode.BUY);
		int countAds = 0;
		
		for (Ad ad : adsInDB) {
			countAds++;
		}
		
		Iterable<Ad> ads = adService.getAllAds();

		// convert to List
		ArrayList<Ad> adList = (ArrayList) ads;

		// assert number of returned ads equals number in DB
		assertEquals(countAds, adList.size());
	}

	@Test
	public void queryResults() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrize(500);
		searchForm.setRadius(5);
		searchForm.setBalcony(true);
		Type[] types = { Type.APARTMENT };
		searchForm.setTypes(types);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList) queryedAds;

		assertEquals(adList.size(), 1);
		assertEquals(adList.get(0).getId(), 1);
	}
	
	@Test
	public void testFilterBalcony() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrize(500);
		searchForm.setRadius(5);
		searchForm.setBalcony(true);
		Type[] types = { Type.APARTMENT };
		searchForm.setTypes(types);
		
		SearchForm searchForm2 = new SearchForm();
		searchForm2.setCity("3001 - Bern");
		searchForm2.setPrize(500);
		searchForm2.setRadius(5);
		searchForm2.setBalcony(false);
		searchForm2.setTypes(types);
				
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList) queryedAds;
		
		Iterable<Ad> queryedAds2 = adService.queryResults(searchForm2, BuyMode.BUY);
		ArrayList<Ad> adList2 = (ArrayList) queryedAds2;

		assertNotEquals(adList.get(0).getId(), adList2.get(0).getId());

	}

	@Test
	public void querryWithMultipleResults() {
		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrize(600);
		searchForm.setRadius(80);
		searchForm.setBalcony(true);
		searchForm.setBalcony(true);
		searchForm.setGarage(true);

		Type[] types = { Type.APARTMENT }; 
		
		searchForm.setTypes(types);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList) queryedAds;

	
		assertEquals(adList.size(), 3); 
		// assert right ads are returned
		
		assertEquals(adList.get(0).getTitle(), "Direkt am Quai: hübsches Studio");
		assertEquals(adList.get(1).getTitle(), "Roommate wanted in Bern");
		assertEquals(adList.get(2).getTitle(), "title");
	}

	@Test
	public void testAddedSearchCriteria() {
		Type[] types = { Type.APARTMENT };

		SearchForm searchForm = new SearchForm();
		searchForm.setCity("3001 - Bern");
		searchForm.setPrize(600);
		searchForm.setRadius(80);
		searchForm.setBalcony(false);
		searchForm.setGarage(true); 
		
		// no ad should be returned if search works correctly (no test ad has that extreme values)
		searchForm.setTypes(types);
		searchForm.setNumberOfBathMax(100);
		searchForm.setNumberOfRoomsMax(100);
		Iterable<Ad> queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		ArrayList<Ad> adList = (ArrayList) queryedAds;

		assertEquals(adList.size(), 0);
		
		searchForm.setNumberOfRoomsMax(100);
		queryedAds = adService.queryResults(searchForm, BuyMode.BUY);
		adList = (ArrayList) queryedAds;
		
		assertEquals(adList.size(), 0);		
	}
	
	@Test
	public void testCheckboxes() {
		assertFalse(true);
	}

	@Test
	public void getNewestAds() {
		Iterable<Ad> newestdAds = adService.getNewestAds(3, BuyMode.BUY);
		ArrayList<Ad> listNewestAds = (ArrayList) newestdAds;

		assertEquals(listNewestAds.size(), 3);
		
		assertEquals(listNewestAds.get(0).getTitle(), "title");
		assertEquals(listNewestAds.get(1).getTitle(), "Direkt am Quai: hübsches Studio");
		assertEquals(listNewestAds.get(2).getTitle(), "Malibu-style Beachhouse");
		

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

}
