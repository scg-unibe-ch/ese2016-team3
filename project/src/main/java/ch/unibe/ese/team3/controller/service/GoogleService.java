package ch.unibe.ese.team3.controller.service;

import java.util.HashSet;
import java.util.Set;
import java.security.SecureRandom;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.GoogleSignupForm;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.UserDao;

/** Handles the persisting of new users signing up with google */
@Service
public class GoogleService {
	
	private static final String DEFAULT_ROLE = "ROLE_USER";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	@Qualifier("org.springframework.security.authenticationManager")
	private AuthenticationManager authenticationManager;

	/** Handles persisting a new user to the database. */
	@Transactional
	public void saveFrom(GoogleSignupForm googleForm) {
		User user = new User();
		user.setUsername(googleForm.getEmail());
		user.setEmail(googleForm.getEmail());
		user.setFirstName(googleForm.getFirstName());
		user.setLastName(googleForm.getLastName());
		
		final SecureRandom rndm = new SecureRandom();
		String randomPassword = new BigInteger(130, rndm).toString(32);
		user.setPassword(randomPassword); //sets a strong random password
		
		user.setEnabled(true);
		user.setGender(Gender.OTHER);
		user.setIsGoogleUser(true);
		user.setGooglePicture(googleForm.getGooglePicture());
		
		user.setAccountType(AccountType.BASIC);
		user.setCreditCard("0000000000000000");
		
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole(DEFAULT_ROLE);
		role.setUser(user);
		userRoles.add(role);
		
		user.setUserRoles(userRoles);
		
		userDao.save(user);
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

	/** Handles login of google user. */
	@Transactional
	public void loginFrom(GoogleSignupForm googleForm) {
		User user = userDao.findByUsername(googleForm.getEmail());
		Authentication request = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}
}
