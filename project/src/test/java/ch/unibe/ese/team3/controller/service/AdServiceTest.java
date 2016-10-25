package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AdServiceTest {

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	/**
	 * In order to test the saved ad, I need to get it back from the DB again, so these
	 * two methods need to be tested together, normally we want to test things isolated of
	 * course. Testing just the returned ad from saveFrom() wouldn't answer the question 
	 * whether the ad has been saved correctly to the db.
	 * @throws ParseException 
	 */
	@Test
	public void saveFromAndGetById() throws ParseException {
		//Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setPreferences("Test preferences");
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrize(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setStudio(true);
		placeAdForm.setMoveInDate("27-02-2015");
		placeAdForm.setMoveOutDate("27-04-2015");
		
		placeAdForm.setSmokers(true);
		placeAdForm.setAnimals(false);
		placeAdForm.setGarden(true);
		placeAdForm.setBalcony(false);
		placeAdForm.setCellar(true);
		placeAdForm.setFurnished(false);
		placeAdForm.setCable(false);
		placeAdForm.setGarage(true);
		placeAdForm.setInternet(false);
		
		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("hans@kanns.ch", "password", "Hans", "Kanns",
				Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);
		
		adService.saveFrom(placeAdForm, filePaths, hans);
		
		Ad ad = new Ad();
		Iterable<Ad> ads = adService.getAllAds();
		Iterator<Ad> iterator = ads.iterator();
		
		while (iterator.hasNext()) {
			ad = iterator.next();
		}
		
		//Testing
		assertTrue(ad.getSmokers());
		assertFalse(ad.getAnimals());
		assertEquals("Bern", ad.getCity());
		assertEquals(3018, ad.getZipcode());
		assertEquals("Test preferences", ad.getPreferences());
		assertEquals("Test Room description", ad.getRoomDescription());
		assertEquals(600, ad.getPrizePerMonth());
		assertEquals(50, ad.getSquareFootage());
		assertEquals("title", ad.getTitle());
		assertEquals("Hauptstrasse 13", ad.getStreet());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date result =  df.parse("2015-02-27");
		
		assertEquals(0, result.compareTo(ad.getMoveInDate()));
	}
	
	@Test
	public void getAdById() {
		Ad ad = adService.getAdById(4);
		assertEquals(ad.getId(), 4);
	}
	
	@Test
	public void getAllAds() {
		int adsInDb = 13;
		Iterable<Ad> ads = adService.getAllAds();
		int adsCount = 0;
		
		// convert to List
		ArrayList<Ad> adList = (ArrayList) ads;
		
		// assert number of returned ads equals number in DB
		assertEquals(adsInDb, adList.size());		
	}
	
	@Test
	public void queryResults() {
		SearchForm searchForm = new SearchForm();	
		searchForm.setCity("3001 - Bern");
		searchForm.setPrize(500);
		searchForm.setRadius(5);
		Type[] types = {Type.APARTMENT}; 
		searchForm.setTypes(types); 
		Iterable<Ad> queryedAds = adService.queryResults(searchForm);
		ArrayList<Ad> adList = (ArrayList) queryedAds;
		
		assertEquals(adList.size(), 1);
		assertEquals(adList.get(0).getId(), 1);
	}
	
	@Test
	public void extendedQuery() {
		SearchForm searchForm = new SearchForm();	
		searchForm.setCity("3001 - Bern");
		searchForm.setPrize(600);
		searchForm.setRadius(80);
		searchForm.setCable(true);
		searchForm.setBalcony(true);
		searchForm.setAnimals(false);
		searchForm.setBalcony(true);
		searchForm.setGarage(true);
		Type[] types = {Type.APARTMENT}; // why changes the the Id of the existing Elements in the database ? 
		searchForm.setTypes(types); 
		Iterable<Ad> queryedAds = adService.queryResults(searchForm);
		ArrayList<Ad> adList = (ArrayList) queryedAds;
		
		assertEquals(adList.size(), 4);     // in the flatfindr app, only 3 ads are displayed
		assertEquals(adList.get(0).getId(), 1);
		assertEquals(adList.get(1).getId(), 6);
		assertEquals(adList.get(2).getId(), 11);
		//assertEquals(adList.get(3).getId(), 13); // where does 4th element come from?
		
		// assert right ads are returned
		
		assertEquals(adList.get(0).getId(), 1);
		assertEquals(adList.get(1).getId(), 1);
		assertEquals(adList.get(2).getId(), 1);
	}
	@Test
	public void getNewestAds() {
		Iterable<Ad> newestdAds = adService.getNewestAds(3);
		ArrayList<Ad> listNewestAds = (ArrayList) newestdAds;
		
		assertEquals(listNewestAds.size(), 3);
		
		assertEquals(listNewestAds.get(0).getId(), 13);
		assertEquals(listNewestAds.get(1).getId(), 6);
		assertEquals(listNewestAds.get(2).getId(), 12);
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
	@Test
	public void checkIfAlreadyAdded() {
		assertTrue(adService.checkIfAlreadyAdded("ese@unibe.ch", alreadyAdded));
	}
	*/
		
	
	
	private User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
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
