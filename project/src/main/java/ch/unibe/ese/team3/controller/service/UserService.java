package ch.unibe.ese.team3.controller.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;

/**
 * Handles all database actions concerning users.
 */
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	/** Gets the user with the given username. */
	@Transactional
	public User findUserByUsername(String username) {
		return userDao.findByUsername(username);
	}
	
	/** Gets the user with the given id. */
	@Transactional
	public User findUserById(long id) {
		return userDao.findUserById(id);
	}
	
	/**
	 * Removes all premium privileges from expired premium users at 1AM.
	 */
	@Scheduled(cron = "0 0 1 * * *")
	public void removeExpiredPremiumUsers(){
		Iterable <User> users = userDao.findAll();
		Date now = new Date();
		
		for (User user : users){
			if (user.isPremium()){
				try{
					if(user.getPremiumExpiryDate().before(now)){
						user.removePremium();
					}
				} 
				catch(Exception e){					
				}
			}
		}
	}
}
