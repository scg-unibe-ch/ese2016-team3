package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Alert;
import ch.unibe.ese.team3.model.AlertType;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.InfrastructureType;
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
		User adolfOgi = createUser("adolf@ogi.ch", "password", "Adolf", "Ogi", Gender.MALE, AccountType.BASIC);
		adolfOgi.setAboutMe("Wallis rocks");
		userDao.save(adolfOgi);

		// Create 2 alerts for Adolf Ogi
		Alert alert = new Alert();
		alert.setUser(adolfOgi);
		alert.setBuyMode(BuyMode.BUY);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alert.setAlertTypes(alertTypes);
		
		alertDao.save(alert);

		alert = new Alert();
		alert.setUser(adolfOgi);
		alert.setBuyMode(BuyMode.BUY);
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

		User thomyF = createUser("thomy@f.ch", "password", "Thomy", "F", Gender.MALE, AccountType.BASIC);
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
		alert.setBuyMode(BuyMode.BUY);
		alert.setAlertTypes(alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alertDao.save(alert);

		alert = new Alert();
		alert.setUser(thomyF);
		alert.setBuyMode(BuyMode.BUY);
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
		oltenResidence.setBuyMode(BuyMode.BUY);
		oltenResidence.setMoveInDate(date);
		oltenResidence.setCreationDate(date);
		oltenResidence.setPrice(1200);
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
		User alertMessageReceiver = createUser("alertMessageTest@f.ch", "password", "alertMessageReceiver", "F", Gender.MALE, AccountType.PREMIUM);
		userDao.save(alertMessageReceiver);
		
		User adCreator = createUser("adCreator@f.ch", "password", "adCreator", "F", Gender.MALE, AccountType.BASIC);
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
		alert.setBuyMode(BuyMode.BUY);
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
		oltenResidence.setBuyMode(BuyMode.BUY);
		oltenResidence.setMoveInDate(date);
		oltenResidence.setCreationDate(date);
		oltenResidence.setPrice(1200);
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

		assertEquals(countIterable(messagesBeforeAlert), 0);
		
		// trigger alert
		alertService.triggerAlerts(oltenResidence);
		
		// assert alertMessageReceiver receives a message when alert triggers
		Iterable<Message> messagesAfterAlert = messageDao.findByRecipient(alertMessageReceiver);
		assertEquals(countIterable(messagesAfterAlert), 1);
	}
	
	@Test
	public void noTriggerWhenPriceTooHigh() {
		// create list of AlertTypes
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);
		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);
		
		// create user
		User userNoTrigger = createUser("userNoTrigger@f.ch", "password", "userNoTrigger", "F", Gender.MALE, AccountType.PREMIUM);
		userDao.save(userNoTrigger);
		
		// create Alert
		Alert alert = new Alert();
		alert.setUser(userNoTrigger);
		alert.setBuyMode(BuyMode.BUY);
		alert.setAlertTypes(alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alertDao.save(alert);
		
		// create Ad
		Date date = new Date();
		Ad ad = new Ad();
		ad.setZipcode(3000);
		ad.setBuyMode(BuyMode.BUY);
		ad.setMoveInDate(date);
		ad.setCreationDate(date);
		ad.setPrice(1700);

		ad.setType(Type.LOFT);
		ad.setRoomDescription("blah");
		ad.setUser(userNoTrigger);
		ad.setTitle("tooExpansiveAd");
		ad.setStreet("Florastr. 100");
		ad.setCity("Bern");
		
		ad.setSquareFootage(42);
		ad.setNumberOfBath(3);
		ad.setNumberOfRooms(5);
		ad.setDistancePublicTransport(1000);
		ad.setDistanceSchool(100);
		ad.setDistanceShopping(400);
		ad.setRenovationYear(1990);
		ad.setBuildYear(1940);
		
		//  no mismatches
		assertFalse(alertService.radiusMismatch(ad, alert));
		assertFalse(alertService.typeMismatch(ad, alert));
		
		// trigger alerts and make sure the user gets no message
		Iterable<Message> messagesBefore = messageDao.findByRecipient(userNoTrigger);
		assertEquals(countIterable(messagesBefore), 0);
		
		alertService.triggerAlerts(ad);
		
		Iterable<Message> messagesAfter = messageDao.findByRecipient(userNoTrigger);
		assertEquals(countIterable(messagesAfter), 0);
	}
	
	// Lean user creating method
	User createUser(String email, String password, String firstName, String lastName, Gender gender, AccountType accountType) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		user.setAccountType(accountType);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
	
	@Test
	public void testExtendedAlert() {
		// create list of AlertTypes	
					
				// create user
				User userExtendedAlert1 = createUser("userExtendedAlert1@f.ch", "password", "userExtendedAlert1", "F", Gender.MALE);
				userDao.save(userExtendedAlert1);
				
				User userExtendedAlert2 = createUser("userExtendedAlert2@f.ch", "password", "userExtendedAlert2", "F", Gender.MALE);
				userDao.save(userExtendedAlert2);
				
				User userAdCreator = createUser("userAdCreator@f.ch", "password", "userAdCreator", "F", Gender.MALE);
				userDao.save(userAdCreator);
				
				//-----------------
				// create Alerts to check binary criteria
				List<AlertType>alertTypes = createAlertTypes();
				Alert alert = new Alert();
				alert.setUser(userExtendedAlert1);
				alert.setBuyMode(BuyMode.BUY);
				alert.setAlertTypes(alertTypes);
				alert.setCity("Bern");
				alert.setZipcode(3000);
				alert.setPrice(1500);
				alert.setRadius(100);
				alert.setDishwasher(true);

				Alert alert2 = new Alert();
				List<AlertType>alertTypes2 = createAlertTypes();
				alert2.setUser(userExtendedAlert2);
				alert2.setBuyMode(BuyMode.BUY);
				alert2.setAlertTypes(alertTypes2);
				alert2.setCity("Bern");
				alert2.setZipcode(3000);
				alert2.setPrice(1500);
				alert2.setRadius(100);
				alert2.setElevator(true);
				
				Alert alert3 = new Alert();
				List<AlertType>alertTypes3 = createAlertTypes();
				alert3.setUser(userExtendedAlert2);
				alert3.setBuyMode(BuyMode.BUY);
				alert3.setAlertTypes(alertTypes3);
				alert3.setCity("Bern");
				alert3.setZipcode(3000);
				alert3.setPrice(1500);
				alert3.setRadius(100);
				alert3.setParking(true);
				
				Alert alert4 = new Alert();
				List<AlertType>alertTypes4 = createAlertTypes();
				alert4.setUser(userExtendedAlert2);
				alert4.setBuyMode(BuyMode.BUY);
				alert4.setAlertTypes(alertTypes4);
				alert4.setCity("Bern");
				alert4.setZipcode(3000);
				alert4.setPrice(1500);
				alert4.setRadius(100);
				alert4.setGarage(true);
				
				Alert alert5 = new Alert();
				List<AlertType>alertTypes5 = createAlertTypes();
				alert5.setUser(userExtendedAlert2);
				alert5.setBuyMode(BuyMode.BUY);
				alert5.setAlertTypes(alertTypes5);
				alert5.setCity("Bern");
				alert5.setZipcode(3000);
				alert5.setPrice(1500);
				alert5.setRadius(100);
				alert5.setBalcony(true);
				
				// test Criteria with Numbers
				Alert alert6 = new Alert();
				List<AlertType>alertTypes6 = createAlertTypes();
				alert6.setUser(userExtendedAlert2);
				alert6.setBuyMode(BuyMode.BUY);
				alert6.setAlertTypes(alertTypes6);
				alert6.setCity("Bern");
				alert6.setZipcode(3000);
				alert6.setPrice(1500);
				alert6.setRadius(100);

				alert6.setDistanceSchoolMax(10);
				alert6.setDistanceSchoolMin(0);
				
				Alert alert7 = new Alert();
				List<AlertType>alertTypes7 = createAlertTypes();
				alert7.setUser(userExtendedAlert2);
				alert7.setBuyMode(BuyMode.BUY);
				alert7.setAlertTypes(alertTypes7);
				alert7.setCity("Bern");
				alert7.setZipcode(3000);
				alert7.setPrice(1500);
				alert7.setRadius(100);

				alert7.setNumberOfRoomsMax(4);
				alert7.setNumberOfRoomsMin(1);
				
				Alert alert8 = new Alert();
				List<AlertType>alertTypes8 = createAlertTypes();
				alert8.setUser(userExtendedAlert2);
				alert8.setBuyMode(BuyMode.BUY);
				alert8.setAlertTypes(alertTypes8);
				alert8.setCity("Bern");
				alert8.setZipcode(3000);
				alert8.setPrice(1500);
				alert8.setRadius(100);

				alert8.setRenovationYearMin(2000);
				alert8.setRenovationYearMax(2010); 
				
				Alert alert9 = new Alert();
				List<AlertType>alertTypes9 = createAlertTypes();
				alert9.setUser(userExtendedAlert2);
				alert9.setBuyMode(BuyMode.BUY);
				alert9.setAlertTypes(alertTypes9);
				alert9.setCity("Bern");
				alert9.setZipcode(3000);
				alert9.setPrice(1500);
				alert9.setRadius(100);
				alert9.setInfrastructureType(InfrastructureType.CABLE);
				
				Alert alert10 = new Alert();
				List<AlertType>alertTypes10 = createAlertTypes();
				alert10.setUser(userExtendedAlert2);
				alert10.setBuyMode(BuyMode.BUY);
				alert10.setAlertTypes(alertTypes10);
				alert10.setCity("Bern");
				alert10.setZipcode(3000);
				alert10.setPrice(1500);
				alert10.setRadius(100);
				alert10.setEarliestMoveInDate(convertStringToDate("10-01-2015"));
				alert10.setLatestMoveInDate(convertStringToDate("10-02-2015"));
				
				// save Alerts
				alertDao.save(alert);
				alertDao.save(alert2);
				alertDao.save(alert3);
				alertDao.save(alert4);
				alertDao.save(alert5);
				alertDao.save(alert6);
				alertDao.save(alert7);
				alertDao.save(alert8);
				alertDao.save(alert9);
				alertDao.save(alert10);

				// create Ad
				Date date = new Date();
				Ad ad = new Ad();
				ad.setZipcode(3000);
				ad.setBuyMode(BuyMode.BUY);
				ad.setMoveInDate(convertStringToDate("01-01-2016"));
				ad.setCreationDate(date);
				ad.setPrice(1000);
				ad.setSquareFootage(42);
				ad.setType(Type.LOFT);
				ad.setRoomDescription("blah");
				ad.setUser(userAdCreator);
				ad.setTitle("AdTestAlert");
				ad.setStreet("Florastr. 100");
				ad.setCity("Bern");
				
				// no alert should be triggered, as none of them fulfills the criteria
				ad.setDishwasher(false);
				ad.setElevator(false);
				ad.setGarage(false);
				ad.setBalcony(false);
				ad.setParking(false);
				
				ad.setSquareFootage(42);
				ad.setNumberOfBath(3);
				ad.setNumberOfRooms(5);
				ad.setDistancePublicTransport(1000);
				ad.setDistanceSchool(100);
				ad.setDistanceShopping(400);
				ad.setRenovationYear(1990);
				ad.setBuildYear(1940);
				
				ad.setInfrastructureType(InfrastructureType.SATELLITE);
				
				assertEquals(countIterable(messageDao.findByRecipient(userExtendedAlert1)), 0);
				assertEquals(countIterable(messageDao.findByRecipient(userExtendedAlert2)), 0);
				
				// trigger alerts
				alertService.triggerAlerts(ad);
				
				
				Iterable<Message> messagesAfter = messageDao.findByRecipient(userExtendedAlert1);
				assertEquals(countIterable(messagesAfter), 0);
				
				messagesAfter = messageDao.findByRecipient(userExtendedAlert2);
				assertEquals(countIterable(messagesAfter), 0);
				
				
				
	}
	
	
	
	private List<AlertType> createAlertTypes() {
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);
		//AlertType typeApartment = new AlertType();
		//typeApartment.setType(Type.APARTMENT);
		
		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);
		//alertTypes.add(typeApartment);
		
		return alertTypes;
	}

	// method to count all iterables
	<T> int countIterable(Iterable<T> iterable) {
		int countMessages = 0;
		for (T element : iterable ) {
			countMessages++;
		}
		return countMessages;
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
