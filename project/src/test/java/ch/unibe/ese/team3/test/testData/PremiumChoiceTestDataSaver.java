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
		
		PremiumChoice week = createPremiumChoice(7, 7.00);
		premiumChoiceDao.save(week);
		
		PremiumChoice month = createPremiumChoice(30, 29.90);
		premiumChoiceDao.save(month);
		
		PremiumChoice hundred = createPremiumChoice(100, 69.90);
		premiumChoiceDao.save(hundred);
		
		PremiumChoice year = createPremiumChoice(365, 149.90);
		premiumChoiceDao.save(year);
	}

	public PremiumChoice createPremiumChoice(int duration, double price) {
		PremiumChoice premiumChoice = new PremiumChoice();
		premiumChoice.setDuration(duration);
		premiumChoice.setPrice(price);
		return premiumChoice;
	}


}
