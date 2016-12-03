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
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
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
	
	AlertForm alertForm;

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
	
		premiumUserWithAlert = createUser("premiumUserWithAlert@ka.ch", "password", "premiumUserWithAlert",
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
		alertForm = new AlertForm();
		
		List<Type> types = new ArrayList<Type>();
		types.add(Type.APARTMENT);
		
	
		alertForm.setUser(premiumUserWithAlert);
		alertForm.setBuyMode(BuyMode.BUY);
		alertForm.setTypes(types);
		alertForm.setCity("3001 - Bern");
		//alertForm.setZipCode(3001);
		alertForm.setPrice(2000000);
		alertForm.setRadius(300);
		alertForm.setExtendedAlert(true);
	}

	
	@Test
	public void radiusMismatch() {

		alertForm.setRadius(40);
		alertForm.setCity("6000 - Luzern");

		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void radiusMatch() {

		alertForm.setRadius(200);
		alertForm.setCity("6000 - Luzern");

		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
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
		alertService.saveFrom(alertForm, premiumUserWithAlert);

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

		// trigger alerts and make sure the user gets no message
		Iterable<Message> messagesBefore = messageDao.findByRecipient(userNoTrigger);
		assertEquals(0, ListUtils.countIterable(messagesBefore));
		alertService.triggerAlerts(ad);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(userNoTrigger);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
		adDao.delete(ad);
	}

	
	// ---------------------------------
	// Test individual Alert Criteria
	
	@Test
	public void floorlevelOutOfRange() {
		// should trigger if Ad is placed
		alertForm.setFloorLevelMin(5);
		alertForm.setFloorLevelMax(7);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void floorlevelInRange() {
		// should trigger if Ad is placed
		alertForm.setFloorLevelMin(1);
		alertForm.setFloorLevelMax(3);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);
		
		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));	
	}
		
	@Test
	public void squareFootageOutOfRange() {
		alertForm.setSquareFootageMax(90);
		alertForm.setSquareFootageMin(80);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void numberOfBathInRange() {
		alertForm.setNumberOfBathMax(4);
		alertForm.setNumberOfBathMin(1);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void numberOfBathOutOfRange() {
		alertForm.setNumberOfBathMax(1);
		alertForm.setNumberOfBathMin(0);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void numberOfRoomsInRange() {
		alertForm.setNumberOfRoomsMax(5);
		alertForm.setNumberOfRoomsMin(0);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void numberOfRoomsOutOfRange() {
		alertForm.setNumberOfRoomsMax(4);
		alertForm.setNumberOfRoomsMin(4);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void distancePublicTransportInRange() {
		alertForm.setDistancePublicTransportMax(2000);
		alertForm.setDistancePublicTransportMin(800);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void distancePublicTransportOutOfRange() {
		alertForm.setDistancePublicTransportMax(2000);
		alertForm.setDistancePublicTransportMin(1000);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void distanceSchoolInRange() {
		alertForm.setDistanceSchoolMax(200);
		alertForm.setDistanceSchoolMin(0);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void distanceSchoolOutOfRange() {
		alertForm.setDistanceSchoolMax(2000);
		alertForm.setDistanceSchoolMin(200);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void distanceShoppingInRange() {
		alertForm.setDistanceShoppingMax(0);
		alertForm.setDistanceShoppingMin(400);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void distanceShoppingOutOfRange() {
		alertForm.setDistanceShoppingMax(300);
		alertForm.setDistanceShoppingMin(0);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void renovationYearInRange() {
		alertForm.setRenovationYearMax(0);
		alertForm.setRenovationYearMin(1900);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void renovationYearOutOfRange() {
		alertForm.setRenovationYearMax(1980);
		alertForm.setRenovationYearMin(1900);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void buildYearInRange() {
		alertForm.setBuildYearMax(2015);
		alertForm.setBuildYearMin(1930);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void buildYearOutOfRange() {
		alertForm.setBuildYearMax(0);
		alertForm.setBuildYearMin(1980);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void searchDishwasher() {
		alertForm.setDishwasher(true);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void searchElevator() {
		alertForm.setElevator(true);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void searchGarage() {
		alertForm.setGarage(true);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void searchBalcony() {
		alertForm.setBalcony(true);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void searchParking() {
		alertForm.setParking(true);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void infrastructureTypeMatch() {
		alertForm.setInfrastructureType(InfrastructureType.SATELLITE);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));

		
	}
	@Test
	public void infrastructureTypeNotMatch() {
		alertForm.setInfrastructureType(InfrastructureType.CABLE);
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void flatTypeNotMatch() {
		List<Type> types = new ArrayList<Type>();
		types.add(Type.HOUSE);
		
		alertForm.setTypes(types);
		alertService.saveFrom(alertForm, premiumUserWithAlert);
		
		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));		
	}
	
	@Test
	public void moveInDateInRange() {
		alertForm.setEarliestMoveInDate("01-11-2015");
		alertForm.setLatestMoveInDate("01-02-2016");
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));	
	}
	
	@Test
	public void moveInDateOutOfRange() {
		alertForm.setEarliestMoveInDate("02-01-2016");
		alertForm.setLatestMoveInDate("05-01-2016");
				
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));	
	}
	
	@Test
	public void onlyEarliestMoveDateSet() {
		alertForm.setEarliestMoveInDate("02-11-2015");
		alertService.saveFrom(alertForm, premiumUserWithAlert);

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void onlyLatestMoveDateSet() {
		alertForm.setLatestMoveInDate("05-01-2016");
		alertService.saveFrom(alertForm, premiumUserWithAlert);		
		

		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void noAlertTypeInForm() {
		List<Type> types = new ArrayList<Type>();
		alertForm.setTypes(types);
		
		alertService.saveFrom(alertForm, premiumUserWithAlert);		
		
		Iterable<Message> messagesBefore = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesBefore));

		alertService.triggerAlerts(normalAd);

		Iterable<Message> messagesAfter = messageDao.findByRecipient(premiumUserWithAlert);
		assertEquals(1, ListUtils.countIterable(messagesAfter));
	}
	
	@Test
	public void triggerBasicUser() {
		
		alertService.saveFrom(alertForm, basicUserWithAlert);		
		
		alertService.triggerAlerts(normalAd);
		
		// assert no message is sent to basic user when alert triggers.
		Iterable<Message> messagesAfter = messageDao.findByRecipient(basicUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
		
		// assert attribute notified is set
		Iterable<AlertResult> alertResults = alertResultDao.findByUser(basicUserWithAlert);
		int countAlertResult = 0;
		
		for (AlertResult alertResut: alertResults) {
			countAlertResult++;
			assertFalse(alertResut.getNotified());
		}
		assertEquals(countAlertResult, 1);
	}
	
	@Test
	public void noTriggerBasicUser() {
		alertForm.setExtendedAlert(false);
		alertForm.setPrice(1000);
		
		alertService.saveFrom(alertForm, basicUserWithAlert);		
		
		alertService.triggerAlerts(normalAd);
		
		// assert no message is sent to basic user when alert triggers.
		Iterable<Message> messagesAfter = messageDao.findByRecipient(basicUserWithAlert);
		assertEquals(0, ListUtils.countIterable(messagesAfter));
		
		// assert no alertResult is created
		Iterable<AlertResult> alertResults = alertResultDao.findByUser(basicUserWithAlert);
		int countAlertResult = 0;
		
		for (AlertResult alertResut: alertResults) {
			countAlertResult++;
		}
		assertEquals(countAlertResult, 0);
	}
	
	@Test
	public void saveAndGetAlert() {
		// save alert
		alertService.saveFrom(alertForm, basicUserWithAlert);
		
		// get alert
		Iterable<Alert> alertsBasicUser = alertService.getAlertsByUser(basicUserWithAlert);
		
		int countAlertResult = 0;
		
		for (Alert alert: alertsBasicUser) {
			countAlertResult++;
			assertEquals(alert.getUser(), basicUserWithAlert);
		}
		assertEquals(countAlertResult, 1);
	}
	
	@Test
	public void saveAndDeleteAlert() {
		// save alert
		alertService.saveFrom(alertForm, basicUserWithAlert);
		
		// get id of alert, assert there is basic user has one alert
		long id = 0;
		int countAlertResult = 0;
		Iterable<Alert> alertsBasicUser = alertService.getAlertsByUser(basicUserWithAlert);
		for (Alert alert: alertsBasicUser) {
			id = alert.getId();
			countAlertResult++;
		}
		assertEquals(countAlertResult, 1);
		
		// delete alert
		alertService.deleteAlert(id);
		
		// make sure alert is deleted
		Iterable<Alert> alertsAfterDelete = alertService.getAlertsByUser(basicUserWithAlert);
		
		int countAfterDelete = 0;
		for (Alert alert: alertsAfterDelete) {
			countAfterDelete++;
		}
		assertEquals(countAfterDelete, 0);
	}
	
	@Transactional
	public Iterable<Alert> getAlertsByUser(User user) {
		return alertDao.findByUser(user);
	}

	/** Deletes the alert with the given id. */
	@Transactional
	public void deleteAlert(Long id) {
		alertDao.delete(id);
	}
	
	
	// ------------------------------
	// Helper Methods
	// ------------------------------
	
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
