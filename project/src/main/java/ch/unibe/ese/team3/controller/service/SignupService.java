package ch.unibe.ese.team3.controller.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.model.PremiumChoice;

/** Handles the persisting of new users */
@Service
public class SignupService {
	
	private static final String DEFAULT_ROLE = "ROLE_USER";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PremiumChoiceService premiumChoiceService;

	/** Handles persisting a new user to the database. */
	@Transactional
	public User saveFrom(SignupForm signupForm) {
		User user = new User();
		user.setUsername(signupForm.getEmail());
		user.setEmail(signupForm.getEmail());
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setPassword(signupForm.getPassword());
		user.setEnabled(true);
		user.setGender(signupForm.getGender());
		
		if(signupForm.getIsPremium() == true){
			user.setAccountType(AccountType.PREMIUM);
			user.setCreditCard(signupForm.getCreditCard());
			
			user.setCreditcardType(signupForm.getCreditcardType());
			user.setSecurityNumber(signupForm.getSecurityNumber());
			user.setExpirationMonth(signupForm.getExpirationMonth());
			user.setExpirationYear(signupForm.getExpirationYear());
			user.setCreditcardName(signupForm.getCreditcardName());
			
			int duration = signupForm.getDuration();
			PremiumChoice premiumChoice = premiumChoiceService.findPremiumChoiceByDuration(duration);
			user.setPremiumChoice(premiumChoice);
		}
		else {
			user.setAccountType(AccountType.BASIC);
			user.setCreditCard("0000000000000000");
		}
		
		
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole(DEFAULT_ROLE);
		role.setUser(user);
		userRoles.add(role);
		
		user.setUserRoles(userRoles);
		
		userDao.save(user);
		
		return user;
	}
	
	/**
	 * Returns whether a user with the given username already exists.
	 * @param username the username to check for
	 * @return true if the user already exists, false otherwise
	 */
	@Transactional
	public boolean doesUserWithUsernameExist(String username){
		return userDao.findByUsername(username) != null;
	}
}
