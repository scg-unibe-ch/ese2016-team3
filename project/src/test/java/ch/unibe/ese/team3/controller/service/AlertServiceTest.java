package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.AlertForm;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Alert;
import ch.unibe.ese.team3.model.AlertResult;
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
import ch.unibe.ese.team3.model.dao.AlertResultDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.util.ListUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@Transactional
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

	@Autowired
	AlertResultDao alertResultDao;

	// ---------------------------------
	// Test individual Alert Criteria
	
	Alert alert;

	private User basicUserWithAlert;

	private User premiumUserWithAlert;

	private User userPlacingAd;

	private Ad normalAd;

	@Before
	public void setUp() {
		// set up users
		basicUserWithAlert = createUser("basicUserWithAlert@ka.ch", "password", "basicUserWithAlert",
				"basicUserWithAlert", Gender.MALE, AccountType.BASIC);
		userDao.save(basicUserWithAlert);
	
		premiumUserWithAlert = createUser("otherUserWithAlert@ka.ch", "password", "premiumUserWithAlert",
				"premiumUserWithAlert", Gender.MALE, AccountType.PREMIUM);
		userDao.save(premiumUserWithAlert);
	
		userPlacingAd = userDao.findByUsername("user@bern.com");
	
		// ad with all criteria (normal value)
		Date date = new Date();
		normalAd = new Ad();
		normalAd.setZipcode(3012);
		normalAd.setBuyMode(BuyMode.BUY);
		normalAd.setMoveInDate(convertStringToDate("01-01-2016"));
		normalAd.setCreationDate(date);
		normalAd.setPrice(1000000);
		normalAd.setSquareFootage(80);
		normalAd.setType(Type.APARTMENT);
		normalAd.setRoomDescription("test");
		normalAd.setUser(userPlacingAd);
		normalAd.setTitle("AdTestAlertHochfeld");
		normalAd.setStreet("Hochfeldstrasse 44");
		normalAd.setCity("Bern");
	
		normalAd.setDishwasher(false);
		normalAd.setElevator(false);
		normalAd.setGarage(false);
		normalAd.setBalcony(false);
		normalAd.setParking(false);
	
		normalAd.setFloorLevel(3);
		normalAd.setSquareFootage(100);
		normalAd.setNumberOfBath(2);
		normalAd.setNumberOfRooms(5);
		normalAd.setDistancePublicTransport(900);
		normalAd.setDistanceSchool(100);
		normalAd.setDistanceShopping(450);
		normalAd.setRenovationYear(1990);
		normalAd.setBuildYear(1940);
	
		normalAd.setInfrastructureType(InfrastructureType.SATELLITE);
	
		adDao.save(normalAd);
	
		// set basic alert criteria
		alert = new Alert();
		Type[] Types = { Type.LOFT }; // change to APARTMENT -> error
		List<AlertType> alertTypes = createAlertTypes(Types);
	
		alert.setUser(premiumUserWithAlert);
		alert.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert, alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(2000000);
		alert.setRadius(300);
	}

	@Test
	public void safeFromTest() {

		AlertForm alertForm = new AlertForm();

		// alertForm.setZipCode(3000);
		alertForm.setCity("3000 - Bern");
		alertForm.setBuyMode(BuyMode.BUY);
		alertForm.setPrice(5000);
		alertForm.setExtendedAlert(false);
		alertForm.setRadius(5);
		alertForm.setTypes(new ArrayList<Type>());

		// Create user Adolf Ogi
		User adolfOgi = createUser("rudolff@ogi.ch", "password", "Adolf", "Ogi", Gender.MALE, AccountType.BASIC);
		adolfOgi.setAboutMe("Juhuu");
		userDao.save(adolfOgi);

		alertService.saveFrom(alertForm, adolfOgi);

		assertEquals("3000 - Bern", alertForm.getCity());
		// assertEquals(adolfOgi, alertForm.getUser()); User wird nie gesetzt in
		// alters, richtig so? oder BUG?

	}

	@Test
	public void safeFromExtendedAlertsTest() {

		AlertForm alertForm = new AlertForm();

		// alertForm.setZipCode(3000);
		alertForm.setCity("3000 - Bern");
		alertForm.setBuyMode(BuyMode.BUY);
		alertForm.setPrice(5000);
		alertForm.setExtendedAlert(true);
		alertForm.setRadius(5);

		// create list of Types
		List<Type> types = new ArrayList<Type>();
		types.add(Type.APARTMENT);
		types.add(Type.HOUSE);

		alertForm.setTypes(types);

		// Create user Adolf Ogi
		User adolfOgi = createUser("hansJunior@ogi.ch", "password", "Adolf", "Ogi", Gender.MALE, AccountType.BASIC);
		adolfOgi.setAboutMe("Hallo:)");
		userDao.save(adolfOgi);

		// extended Alter criteria
		alertForm.setEarliestMoveInDate("11-10-2016");
		alertForm.setBalcony(true);
		alertForm.setNumberOfRoomsMin(4);

		alertService.saveFrom(alertForm, adolfOgi);

		assertEquals("3000 - Bern", alertForm.getCity());
		assertTrue(alertForm.isBalcony());
		// assertEquals(adolfOgi, alertForm.getUser()); User wird nie gesetzt in
		// alters, richtig so? oder BUG?

	}

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

		// create 2nd list of AlertTypes (necessary, as an alert cannot
		// reference the same alert type)
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
		addAlertTypesToAlert(alert, alertTypes);
		alertDao.save(alert);

		alert = new Alert();
		alert.setUser(adolfOgi);
		alert.setBuyMode(BuyMode.BUY);
		alert.setCity("Bern");
		alert.setZipcode(3002);
		alert.setPrice(1000);
		alert.setRadius(5);
		addAlertTypesToAlert(alert, alertTypes2);
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

		// create 2nd list of AlertTypes (necessary, as an alert cannot
		// reference the same alert type)
		AlertType typeApartment2 = new AlertType();
		typeApartment2.setType(Type.APARTMENT);
		AlertType typeLoft2 = new AlertType();
		typeLoft2.setType(Type.LOFT);

		List<AlertType> alertTypes2 = new ArrayList<>();
		alertTypes2.add(typeLoft2);

		// -------------------------------
		// Create 2 alerts for Thomy F
		Alert alert = new Alert();
		alert.setUser(thomyF);
		alert.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert, alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alertDao.save(alert);

		alert = new Alert();
		alert.setUser(thomyF);
		alert.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert, alertTypes);
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
		assertTrue("Olten radius mismatch second alert", alertService.radiusMismatch(oltenResidence, alertList.get(1)));
		assertFalse("Olten no type mismatch fist alert", alertService.typeMismatch(oltenResidence, alertList.get(0)));
		assertTrue("Olten type mismatch second alert", alertService.typeMismatch(oltenResidence, alertList.get(1))); // is

		adDao.delete(oltenResidence);
	}

	@Test
	public void triggerAlertMessageToUser() {
		User alertMessageReceiver = createUser("alertMessageTest@f.ch", "password", "alertMessageReceiver", "F",
				Gender.MALE, AccountType.PREMIUM);
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
		addAlertTypesToAlert(alert, alertTypes);
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
		oltenResidence.setDistancePublicTransport(500);
		oltenResidence.setInfrastructureType(InfrastructureType.FOC);
		oltenResidence.setType(Type.LOFT);
		oltenResidence.setRoomDescription("blah");
		oltenResidence.setUser(adCreator);
		oltenResidence.setTitle("Olten Residence");
		oltenResidence.setStreet("Florastr. 100");
		oltenResidence.setCity("Olten");
		oltenResidence.setBalcony(false);
		oltenResidence.setGarage(false);

		adDao.save(oltenResidence);

		// inbox of alertMessageReceiver is empty
		Iterable<Message> messagesBeforeAlert = messageDao.findByRecipient(alertMessageReceiver);

		assertEquals(0, ListUtils.countIterable(messagesBeforeAlert));

		Iterable<AlertResult> resultsBefore = alertResultDao.findAll();

		int sizeBefore = ListUtils.countIterable(resultsBefore);
		// trigger alert
		alertService.triggerAlerts(oltenResidence);
		Iterable<AlertResult> results = alertResultDao.findAll();

		int size = ListUtils.countIterable(results);
		assertTrue(sizeBefore < size);

		// assert alertMessageReceiver receives a message when alert triggers
		Iterable<Message> messagesAfterAlert = messageDao.findByRecipient(alertMessageReceiver);
		assertEquals(1, ListUtils.countIterable(messagesAfterAlert));
		adDao.delete(oltenResidence);
	}

	@Test
	public void noTriggerWhenPriceTooHigh() {
		// create list of AlertTypes
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);
		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);

		// create user
		User userNoTrigger = createUser("userNoTrigger@f.ch", "password", "userNoTrigger", "F", Gender.MALE,
				AccountType.PREMIUM);
		userDao.save(userNoTrigger);

		// create Alert
		Alert alert = new Alert();
		alert.setUser(userNoTrigger);
		alert.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert, alertTypes);
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

		// no mismatches
		assertFalse(alertService.radiusMismatch(ad, alert));
		assertFalse(alertService.typeMismatch(ad, alert));

		// trigger alerts and make sure the user gets no message
		Iterable<Message> messagesBefore = messageDao.findByRecipient(userNoTrigger);
		assertEquals(0, ListUtils.countIterable(messagesBefore));
		alertService.triggerAlerts(ad);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(userNoTrigger);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
		adDao.delete(ad);
	}

	@Test
	public void testExtendedAlert() {
		// create list of AlertTypes

		// create user
		User userExtendedAlert1 = createUser("userExtendedAlert1@f.ch", "password", "userExtendedAlert1", "F",
				Gender.MALE, AccountType.BASIC);
		userDao.save(userExtendedAlert1);

		User userExtendedAlert2 = createUser("userExtendedAlert2@f.ch", "password", "userExtendedAlert2", "F",
				Gender.MALE, AccountType.BASIC);
		userDao.save(userExtendedAlert2);

		User userAdCreator = createUser("userAdCreator@f.ch", "password", "userAdCreator", "F", Gender.MALE,
				AccountType.BASIC);
		userDao.save(userAdCreator);

		// -----------------
		// create Alerts to check binary criteria

		Type[] typeLoft = { Type.LOFT };

		List<AlertType> alertTypes = createAlertTypes(typeLoft);
		Alert alert = new Alert();
		alert.setUser(userExtendedAlert1);
		alert.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert, alertTypes);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(100);
		alert.setDishwasher(true);

		Alert alert2 = new Alert();
		List<AlertType> alertTypes2 = createAlertTypes(typeLoft);
		alert2.setUser(userExtendedAlert2);
		alert2.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert2, alertTypes2);
		alert2.setCity("Bern");
		alert2.setZipcode(3000);
		alert2.setPrice(1500);
		alert2.setRadius(100);
		alert2.setElevator(true);

		Alert alert3 = new Alert();
		List<AlertType> alertTypes3 = createAlertTypes(typeLoft);
		alert3.setUser(userExtendedAlert2);
		alert3.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert3, alertTypes3);
		alert3.setCity("Bern");
		alert3.setZipcode(3000);
		alert3.setPrice(1500);
		alert3.setRadius(100);
		alert3.setParking(true);

		Alert alert4 = new Alert();
		List<AlertType> alertTypes4 = createAlertTypes(typeLoft);
		alert4.setUser(userExtendedAlert2);
		alert4.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert4, alertTypes4);
		alert4.setCity("Bern");
		alert4.setZipcode(3000);
		alert4.setPrice(1500);
		alert4.setRadius(100);
		alert4.setGarage(true);

		Alert alert5 = new Alert();
		List<AlertType> alertTypes5 = createAlertTypes(typeLoft);
		alert5.setUser(userExtendedAlert2);
		alert5.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert5, alertTypes5);
		alert5.setCity("Bern");
		alert5.setZipcode(3000);
		alert5.setPrice(1500);
		alert5.setRadius(100);
		alert5.setBalcony(true);

		// test Criteria with Numbers
		Alert alert6 = new Alert();
		List<AlertType> alertTypes6 = createAlertTypes(typeLoft);
		alert6.setUser(userExtendedAlert2);
		alert6.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert6, alertTypes6);
		alert6.setCity("Bern");
		alert6.setZipcode(3000);
		alert6.setPrice(1500);
		alert6.setRadius(100);

		alert6.setDistanceSchoolMax(10);
		alert6.setDistanceSchoolMin(0);

		Alert alert7 = new Alert();
		List<AlertType> alertTypes7 = createAlertTypes(typeLoft);
		alert7.setUser(userExtendedAlert2);
		alert7.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert7, alertTypes7);
		alert7.setCity("Bern");
		alert7.setZipcode(3000);
		alert7.setPrice(1500);
		alert7.setRadius(100);

		alert7.setNumberOfRoomsMax(4);
		alert7.setNumberOfRoomsMin(1);

		Alert alert8 = new Alert();
		List<AlertType> alertTypes8 = createAlertTypes(typeLoft);
		alert8.setUser(userExtendedAlert2);
		alert8.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert8, alertTypes8);
		alert8.setCity("Bern");
		alert8.setZipcode(3000);
		alert8.setPrice(1500);
		alert8.setRadius(100);

		alert8.setRenovationYearMin(2000);
		alert8.setRenovationYearMax(2010);

		Alert alert9 = new Alert();
		List<AlertType> alertTypes9 = createAlertTypes(typeLoft);
		alert9.setUser(userExtendedAlert2);
		alert9.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert9, alertTypes9);
		alert9.setCity("Bern");
		alert9.setZipcode(3000);
		alert9.setPrice(1500);
		alert9.setRadius(100);
		alert9.setInfrastructureType(InfrastructureType.CABLE);

		Alert alert10 = new Alert();
		List<AlertType> alertTypes10 = createAlertTypes(typeLoft);
		alert10.setUser(userExtendedAlert2);
		alert10.setBuyMode(BuyMode.BUY);
		addAlertTypesToAlert(alert10, alertTypes10);
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

		adDao.save(ad);

		assertEquals(0, ListUtils.countIterable(messageDao.findByRecipient(userExtendedAlert1)));
		assertEquals(0, ListUtils.countIterable(messageDao.findByRecipient(userExtendedAlert2)));

		// trigger alerts
		alertService.triggerAlerts(ad);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(userExtendedAlert1);
		assertEquals(0, ListUtils.countIterable(messagesAfter));

		messagesAfter = messageDao.findByRecipient(userExtendedAlert2);
		assertEquals(0, ListUtils.countIterable(messagesAfter));

		adDao.delete(ad);
	}

	// ---------------------------------
	// Test individual Alert Criteria

	@Test
	public void floorlevel() {
		// should trigger if Ad is placed
		alert.setFloorLevelMin(1);
		alert.setFloorLevelMax(3);

		alertDao.save(alert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));

		alertDao.delete(alert);
	}

	// Lean user creating method
	User createUser(String email, String password, String firstName, String lastName, Gender gender,
			AccountType accountType) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		user.setAccountType(accountType);
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		user.addUserRole(role);
		return user;
	}

	// ------------------------------
	// Helper Methods

	// causes data integrety error

	private List<AlertType> createAlertTypes(Type[] types) {
		List<AlertType> alertTypes = new ArrayList<>();

		for (int i = 0; i < types.length; i++) {
			AlertType alertType = new AlertType();
			alertType.setType(types[i]);
			// alertType.setAlert(alert);
			alertTypes.add(alertType);
		}
		return alertTypes;
	}

	private void addAlertTypesToAlert(Alert alert, List<AlertType> alertTypes) {
		for (AlertType alertType : alertTypes) {
			alert.addAlertType(alertType);
		}
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
