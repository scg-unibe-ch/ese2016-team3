package ch.unibe.ese.team3.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.UpgradeForm;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.model.PremiumChoice;

/** Handles the upgrading of users */
@Service
public class UpgradeService {
	
	@Autowired
	private UserDao userDao;

	/** Handles upgrading user in database. */
	@Transactional
	public void upgradeFrom(UpgradeForm upgradeForm, User user, PremiumChoice premiumChoice) {
		user.setCreditCard(upgradeForm.getCreditCard());
		user.setCreditcardType(upgradeForm.getCreditcardType());
		user.setSecurityNumber(upgradeForm.getSecurityNumber());
		user.setExpirationMonth(upgradeForm.getExpirationMonth());
		user.setExpirationYear(upgradeForm.getExpirationYear());
		user.setCreditcardName(upgradeForm.getCreditcardName());
		
		user.setAccountType(AccountType.PREMIUM);
		user.setPremiumChoice(premiumChoice);
		
		userDao.save(user);
	}
	
}
