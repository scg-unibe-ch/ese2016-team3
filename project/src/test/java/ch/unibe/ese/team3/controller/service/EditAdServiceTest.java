package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.Gender;
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
		
		Ad ad = adService.saveFrom(placeAdForm, filePaths, hans);
		long adId = ad.getId();
		
		// veränderung von placeAdForm
		placeAdForm.setAnimals(true);
		placeAdForm.setSmokers(false);
		
		ad = editadservice.saveFrom(placeAdForm, filePaths, hans, adId);
	
		assertTrue(ad.getAnimals());
		assertFalse(ad.getSmokers());
		
		
	}
	
	@Test
	public void deletePictureFromAdTest() throws ParseException{
		User hans = userDao.findByUsername("user@bern.com");
		
		
		editadservice.deletePictureFromAd(1, 1);
		
		Ad adBern = adDao.findOne(1L);
		
		List<AdPicture> newPics = adBern.getPictures();
		assertEquals(2, newPics.size());
		
		
		
	}
	
	@Test
	public void PlaceAdFormTest(){
		
		Ad ad = new Ad();
		ad.setRoomDescription("This is a wonderful flat");
		ad.setPreferences("no preferences");
		
		PlaceAdForm placeAdForm = editadservice.fillForm(ad);
		
		
		
		assertEquals("This is a wonderful flat", placeAdForm.getRoomDescription());
		assertEquals("no preferences", placeAdForm.getPreferences());
		
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
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
	

}
