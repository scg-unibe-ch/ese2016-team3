package ch.unibe.ese.team3.controller.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.UpgradeForm;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.PremiumChoiceDao;
import ch.unibe.ese.team3.model.dao.UserDao;

/** Handles the upgrading of users */
@Service
public class UpgradeService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PremiumChoiceDao premiumChoiceDao;

	@Transactional
	public PremiumChoice findPremiumChoiceByDuration(int duration) {
		return premiumChoiceDao.findByDuration(duration);
	}
	
	@Transactional
	public PremiumChoice findPremiumChoiceById(long id){
		return premiumChoiceDao.findPremiumChoiceById(id);
	}
	
	@Transactional
	public Iterable<PremiumChoice> findAll(){
		return premiumChoiceDao.findAll();
	}

	/** Handles upgrading user in database. */
	@Transactional
	public void upgradeFrom(UpgradeForm upgradeForm, User user, PremiumChoice premiumChoice) {
		user.setCreditCard(upgradeForm.getCreditCard());
		user.setCreditcardType(upgradeForm.getCreditcardType());
		user.setSecurityNumber(upgradeForm.getSecurityNumber());
		user.setExpirationMonth(upgradeForm.getExpirationMonth());
		user.setExpirationYear(upgradeForm.getExpirationYear());
		user.setCreditcardName(upgradeForm.getCreditcardName());
		
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, upgradeForm.getDuration());
		Date expiryDate = calendar.getTime();
		user.setPremiumExpiryDate(expiryDate);
		
		user.setAccountType(AccountType.PREMIUM);
		user.setPremiumChoice(premiumChoice);
		
		userDao.save(user);
	}
	
	
	
}
