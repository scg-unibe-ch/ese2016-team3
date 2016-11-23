package ch.unibe.ese.team3.controller.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;

/** Handles updating a user's profile. */
@Service
public class UserUpdateService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserDao userDao;

	/** Handles updating an existing user in the database. */
	@Transactional
	public void updateFrom(EditProfileForm editProfileForm, User user) {
		
		User currentUser = user;
		
		currentUser.setUsername(editProfileForm.getUsername());
		currentUser.setFirstName(editProfileForm.getFirstName());
		currentUser.setLastName(editProfileForm.getLastName());
		currentUser.setPassword(editProfileForm.getPassword());
		currentUser.setAboutMe(editProfileForm.getAboutMe());
		currentUser.setEmail(editProfileForm.getUsername());
		
		userDao.save(currentUser);
	}

	
	
}
