package ch.unibe.ese.team3.test.testData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Alert;
import ch.unibe.ese.team3.model.AlertResult;
import ch.unibe.ese.team3.model.AlertType;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.AlertDao;
import ch.unibe.ese.team3.model.dao.AlertResultDao;
import ch.unibe.ese.team3.model.dao.UserDao;

/**
 * This inserts some alert test data into the database.
 */
@Service
public class AlertTestDataSaver {

	@Autowired
	private AlertDao alertDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AlertResultDao alertResultDao;
	
	@Autowired 
	private AdDao adDao;


	@Transactional
	public void saveTestData() throws Exception {
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		Ad luzernAd = adDao.findByTitle("Elegant Studio in Lucerne");
		// create list of AlertTypes
		AlertType typeApartment = new AlertType();
		typeApartment.setType(Type.APARTMENT);
		AlertType typeLoft = new AlertType();
		typeLoft.setType(Type.LOFT);
		AlertType typeVilla = new AlertType();
		typeVilla.setType(Type.VILLA);
		
		List<AlertType> alertTypes = new ArrayList<>();
		alertTypes.add(typeLoft);
		alertTypes.add(typeApartment);
		
		// second list for test
		List<AlertType> alertTypes2 = new ArrayList<>();
		AlertType typeHouse = new AlertType();
		typeHouse.setType(Type.HOUSE);
		alertTypes2.add(typeHouse);

		
		// 2 Alerts for the ese test-user
		Alert alert = new Alert();
		alert.setUser(ese);
		alert.setAlertTypes(alertTypes);
		alert.setBuyMode(BuyMode.BUY);
		alert.setCity("Bern");
		alert.setZipcode(3000);
		alert.setPrice(1500);
		alert.setRadius(30);
		alertDao.save(alert);
				
		alertTypes.add(typeVilla);
		
		Alert alert2 = new Alert();
		alert2.setUser(ese);
		alert2.setAlertTypes(alertTypes2);
		alert2.setBuyMode(BuyMode.BUY);
		alert2.setCity("Zürich");
		alert2.setZipcode(8000);
		alert2.setPrice(1000);
		alert2.setRadius(25);
		alertDao.save(alert2);
		
		
		// One alert for Jane Doe
		alert = new Alert();
		alert.setUser(jane);
		alert.setBuyMode(BuyMode.BUY);
		alert.setCity("Luzern");
		alert.setZipcode(6000);
		alert.setPrice(900);
		alert.setRadius(22);
		alertDao.save(alert);
		
		AlertResult alertResult = new AlertResult();
		alertResult.setTriggerAd(luzernAd);
		alertResult.setTriggerDate(new Date());
		alertResult.setUser(jane);
		alertResult.setNotified(false);
		alertResultDao.save(alertResult);
	}

}
