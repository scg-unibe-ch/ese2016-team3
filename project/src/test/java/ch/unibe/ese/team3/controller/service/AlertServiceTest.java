package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.AlertForm;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.Alert;
import ch.unibe.ese.team3.model.AlertType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.AlertDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class AlertServiceTest {

	@Autowired
	AdDao adDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	MessageDao messageDao;
	
	@Autowired
	MessageService messageService;

	@Autowired
	AlertDao alertDao;

	@Autowired
	AlertService alertService;

	@Test
	public void createAlerts() {
		// create list of AlertTypes
		AlertType typeApartment = new AlertType();
		typeApartment.setType(Type.APARTMENT);
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);

		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);
		alertTypes.add(typeApartment);

		ArrayList<Alert> alertList = new ArrayList<Alert>();
		
		// create 2nd list of AlertTypes (necessary, as an alert cannot reference the same alert type)
		AlertType typeApartment2 = new AlertType();
		typeApartment2.setType(Type.APARTMENT);
		AlertType typeLoft2 = new AlertType();
		typeLoft2.setType(Type.LOFT);

		List<AlertType> alertTypes2 = new ArrayList<>();
		alertTypes2.add(typeLoft2);
		
		// Create user Adolf Ogi
		User adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi", Gender.MALE);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);

		// Create 2 alerts for Adolf Ogi
		Alert alert = new Alert();
		alert.setUser(adolfOgi);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alert.setAlertTypes(alertTypes);
		
		alertDao.save(alert);

		alert = new Alert();
		alert.setUser(adolfOgi);
		alert.setCity("Bern");
		alert.setZipcode(3002);
		alert.setPrice(1000);
		alert.setRadius(5);
		alert.setAlertTypes(alertTypes2);
		alertDao.save(alert);

		// copy alerts to a list
		Iterable<Alert> alerts = alertService.getAlertsByUser(adolfOgi);
		for (Alert returnedAlert : alerts)
			alertList.add(returnedAlert);

		// begin the actual testing
		assertEquals(2, alertList.size());
		assertEquals(adolfOgi, alertList.get(0).getUser());
		assertEquals("Bern", alertList.get(1).getCity());
		assertTrue(alertList.get(0).getRadius() > alertList.get(1).getRadius());
	}

	@Test
	public void mismatchChecks() {
		ArrayList<Alert> alertList = new ArrayList<Alert>();

		User thomyF = createUser("thomy@f.ch", "password", "Thomy", "F", Gender.MALE);
		thomyF.setAboutMe("Supreme hustler");
		userDao.save(thomyF);

		// create list of AlertTypes
		AlertType typeApartment = new AlertType();
		typeApartment.setType(Type.APARTMENT);
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);

		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);
		alertTypes.add(typeApartment);

		// create 2nd list of AlertTypes (necessary, as an alert cannot reference the same alert type)
		AlertType typeApartment2 = new AlertType();
		typeApartment2.setType(Type.APARTMENT);
		AlertType typeLoft2 = new AlertType();
		typeLoft2.setType(Type.LOFT);

		List<AlertType> alertTypes2 = new ArrayList<>();
		alertTypes2.add(typeLoft2);
		
		//-------------------------------
		// Create 2 alerts for Thomy F
		Alert alert = new Alert();
		alert.setUser(thomyF);
		alert.setAlertTypes(alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alertDao.save(alert);

		alert = new Alert();
		alert.setUser(thomyF);
		alert.setAlertTypes(alertTypes2);
		alert.setCity("Bern");
		alert.setZipcode(3002);
		alert.setPrice(1000);
		alert.setRadius(5);
		alertDao.save(alert);

		Iterable<Alert> alerts = alertService.getAlertsByUser(userDao.findByUsername("thomy@f.ch"));
		for (Alert returnedAlert : alerts)
			alertList.add(returnedAlert);

		// save an ad
		Date date = new Date();
		Ad oltenResidence = new Ad();
		oltenResidence.setZipcode(4600);
		oltenResidence.setMoveInDate(date);
		oltenResidence.setCreationDate(date);
		oltenResidence.setPrizePerMonth(1200);
		oltenResidence.setSquareFootage(42);
		oltenResidence.setType(Type.APARTMENT);
		oltenResidence.setRoomDescription("blah");
		oltenResidence.setUser(thomyF);
		oltenResidence.setTitle("Olten Residence");
		oltenResidence.setStreet("Florastr. 100");
		oltenResidence.setCity("Olten");
		oltenResidence.setBalcony(false);
		oltenResidence.setGarage(false);
		

		adDao.save(oltenResidence);
		
		assertFalse("Olten no radius mismatch first alert",
		alertService.radiusMismatch(oltenResidence, alertList.get(0)));
		assertTrue("Olten radius mismatch second alert",
		alertService.radiusMismatch(oltenResidence, alertList.get(1)));
		assertFalse("Olten no type mismatch fist alert",
		alertService.typeMismatch(oltenResidence, alertList.get(0))); 
		assertTrue("Olten type mismatch second alert",
		alertService.typeMismatch(oltenResidence, alertList.get(1))); // is true as 2nd alert does not contain alert type "appartment" 
	}
	
	@Test
	public void triggerAlertMessageToUser() {
		User alertMessageReceiver = createUser("alertMessageTest@f.ch", "password", "alertMessageReceiver", "F", Gender.MALE);
		userDao.save(alertMessageReceiver);
		
		User adCreator = createUser("adCreator@f.ch", "password", "adCreator", "F", Gender.MALE);
		userDao.save(adCreator);
		
		// create list of AlertTypes
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);
		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);

		
		// create Alert for alertMessageReceiver
		Alert alert = new Alert();
		alert.setUser(alertMessageReceiver);
		alert.setAlertTypes(alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alertDao.save(alert);
		
		// save ad, which triggers alert of alertMessageReceiver
		// save an ad
		Date date = new Date();
		Ad oltenResidence = new Ad();
		oltenResidence.setZipcode(4600);
		oltenResidence.setMoveInDate(date);
		oltenResidence.setCreationDate(date);
		oltenResidence.setPrizePerMonth(1200);
		oltenResidence.setSquareFootage(42);
		oltenResidence.setType(Type.LOFT);
		oltenResidence.setRoomDescription("blah");
		oltenResidence.setUser(adCreator);
		oltenResidence.setTitle("Olten Residence");
		oltenResidence.setStreet("Florastr. 100");
		oltenResidence.setCity("Olten");
		oltenResidence.setBalcony(false);
		oltenResidence.setGarage(false);
		
		// inbox of alertMessageReceiver is empty
		Iterable<Message> messagesBeforeAlert = messageDao.findByRecipient(alertMessageReceiver);
		int countMessagesBeforeAlert = 0;
		
		for (Message message: messagesBeforeAlert) {
			countMessagesBeforeAlert++;
		}
		assertEquals(countMessagesBeforeAlert, 0);
		
		// trigger alert
		alertService.triggerAlerts(oltenResidence);
		
		// assert alertMessageReceiver receives a message when alert triggers
		Iterable<Message> messagesAfterAlert = messageDao.findByRecipient(alertMessageReceiver);
		int countMessagesAfterAlert = 0;
		
		for (Message message: messagesAfterAlert) {
			countMessagesAfterAlert++;
		}
		assertEquals(countMessagesAfterAlert, 1);
	}
	
	// Lean user creating method
	User createUser(String email, String password, String firstName, String lastName, Gender gender) {
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
