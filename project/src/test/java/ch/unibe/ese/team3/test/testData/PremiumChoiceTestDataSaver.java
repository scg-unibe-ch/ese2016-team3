package ch.unibe.ese.team3.test.testData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.dao.PremiumChoiceDao;

/**
 * This inserts some test data about the premium choices into the database.
 */
@Service
public class PremiumChoiceTestDataSaver {

	@Autowired
	private PremiumChoiceDao premiumChoiceDao;

	@Transactional
	public void saveTestData() throws Exception {
		PremiumChoice month = createPremiumChoice(31, 29.0);
		premiumChoiceDao.save(month);
		
		PremiumChoice week = createPremiumChoice(7, 7.0);
		premiumChoiceDao.save(week);
		
		PremiumChoice halfYear = createPremiumChoice(182, 99.9);
		premiumChoiceDao.save(halfYear);
		
		PremiumChoice year = createPremiumChoice(365, 149.9);
		premiumChoiceDao.save(year);
	}

	public PremiumChoice createPremiumChoice(int duration, double price) {
		PremiumChoice premiumChoice = new PremiumChoice();
		premiumChoice.setDuration(duration);
		premiumChoice.setPrice(price);
		return premiumChoice;
	}


}
