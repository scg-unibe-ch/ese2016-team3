package ch.unibe.ese.team3.controller.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.dao.UserDao;

/** Handles updating a user's profile. */
@Service
public class UserUpdateService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	/** Handles updating an existing user in the database. */
	@Transactional
	public void updateFrom(EditProfileForm editProfileForm) {
		
		User currentUser = userService.findUserByUsername(editProfileForm.getUsername());
		
		currentUser.setFirstName(editProfileForm.getFirstName());
		currentUser.setLastName(editProfileForm.getLastName());
		currentUser.setPassword(editProfileForm.getPassword());
		currentUser.setAboutMe(editProfileForm.getAboutMe());
		
		if (editProfileForm.getIsPremium() == true){
			currentUser.setAccountType(AccountType.PREMIUM);
			currentUser.setCreditCard(editProfileForm.getCreditCard());
		}
		else{
			currentUser.setAccountType(AccountType.BASIC);
			currentUser.setCreditCard("0000000000000000");
		}
		

		userDao.save(currentUser);
	}

	
	
}
