package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class EditAdServiceTest {
	
	@Autowired
	private EditAdService editadservice;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private AdService adService;
	
	@Test
	public void saveFromTest(){
		//Perparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");
	
		placeAdForm.setBalcony(false);

		placeAdForm.setGarage(true);


		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("hans@flitzt.ch", "password", "hans", "Flitzt",
				Gender.MALE);
		hans.setAboutMe("Wie der Blitz");
		userDao.save(hans);
		
		Ad ad = adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);
		long adId = ad.getId();
		
		// veränderung von placeAdForm
		placeAdForm.setBalcony(true);
		placeAdForm.setGarage(false);
		
		ad = editadservice.saveFrom(placeAdForm, filePaths, hans, adId);
	
		assertTrue(ad.getBalcony());
		assertFalse(ad.getGarage());
		
		
	}
	
	@Test
	public void saveFromTestnewAdress(){
		//Perparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");
	
		placeAdForm.setBalcony(false);

		placeAdForm.setGarage(true);


		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("fritz@flitzt.ch", "password", "Fritz", "Flitzt",
				Gender.MALE);
		hans.setAboutMe("Wie der Blitz");
		userDao.save(hans);
		
		Ad ad = adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);
		long adId = ad.getId();
		
		// veränderung von placeAdForm
		placeAdForm.setStreet("Forelstrasse 22");
		placeAdForm.setCity("3072 - Ostermundigen");
		
		
		ad = editadservice.saveFrom(placeAdForm, filePaths, hans, adId);
	
		
		assertEquals(46.960744,ad.getLatitude().doubleValue(), 0.00001);
		assertEquals(7.483973, ad.getLongitude().doubleValue(), 0.00001);
		
		
		
	}
	
	
	@Test
	public void saveFromTestnewVistitingTime(){
		//Perparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setType(Type.APARTMENT);
		placeAdForm.setRoomDescription("Test Room description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setMoveInDate("27-02-2015");
	
		placeAdForm.setBalcony(false);

		placeAdForm.setGarage(true);


		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("meier@flitzt.ch", "password", "meier", "Flitzt",
				Gender.MALE);
		hans.setAboutMe("Wie der Blitz");
		userDao.save(hans);
		
		Ad ad = adService.saveFrom(placeAdForm, filePaths, hans, BuyMode.BUY);
		long adId = ad.getId();
		
		// veränderung von placeAdForm
		List<String> visits = new ArrayList<String>();
		String visit = "01-11-2016 ; 14:45 ; 15:55";
		visits.add(visit);
		
		
		placeAdForm.setVisits(visits);
		
		
		ad = editadservice.saveFrom(placeAdForm, filePaths, hans, adId);
	
		
		assertEquals(1,ad.getVisits().size());
		
		
		
	}
	@Test
	public void deletePictureFromAdTest() throws ParseException{
		editadservice.deletePictureFromAd(1, 1);
		
		Ad adBern = adDao.findOne(1L);
		
		List<AdPicture> newPics = adBern.getPictures();
		assertEquals(2, newPics.size());
		
		
		
	}
	
	@Test
	public void PlaceAdFormTest() throws ParseException{
		
		Ad ad = new Ad();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ad.setBalcony(true);
		ad.setBuildYear(2010);
		ad.setCity("Bern");
		ad.setZipcode(3000);
		ad.setDishwasher(false);
		ad.setDistancePublicTransport(1000);
		ad.setDistanceSchool(200);
		ad.setDistanceShopping(300);
		ad.setElevator(true);
		ad.setEndDate(dateFormat.parse("2016-10-10"));
		ad.setFloorLevel(3);
		ad.setGarage(true);
		ad.setIncreaseBidPrice(100);
		ad.setInfrastructureType(InfrastructureType.CABLE);
		ad.setMoveInDate(dateFormat.parse("2016-11-10"));
		ad.setNumberOfBath(4);
		ad.setNumberOfRooms(5);
		ad.setParking(false);
		ad.setPrice(2000);
		ad.setRenovationYear(2012);
		ad.setRoomDescription("Hello world!");
		ad.setSquareFootage(120);
		ad.setStartDate(dateFormat.parse("2016-09-10"));
		ad.setStartPrice(2000);
		ad.setStreet("Testweg 14");
		ad.setTitle("Test ad");
		ad.setType(Type.APARTMENT);
				
		PlaceAdForm placeAdForm = editadservice.fillForm(ad);	
		
		
		assertEquals("Hello world!", placeAdForm.getRoomDescription());
		
	}
	
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
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		user.addUserRole(role);
		return user;   
	}
	

}
