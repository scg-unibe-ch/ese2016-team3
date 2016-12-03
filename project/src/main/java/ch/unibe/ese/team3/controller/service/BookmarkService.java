package ch.unibe.ese.team3.controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.model.enums.BookmarkStatus;

/** Adds or removes bookmarked ads from the user and updates the user accordingly */
@Service
public class BookmarkService {
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * This method adds or removes ads from the ArrayList.
	 * 
	 * @param id
	 *          it's the current ads' id
	 * @param bookmarked
	 *          tells the function the state of of the ad regarding bookmarks
	 * @param bookmarkedAds
	 *          users current list of bookmarked ads
	 * @param user
	 *          current user
	 *          
	 * @return returns an integer 3 bookmark it
	 *                            2 undo the bookmark
	 * 
	 */
	public int getBookmarkStatus(Ad ad, boolean bookmarked, User user) {
		List<Ad> tempAdList = user.getBookmarkedAds();
		if(bookmarked) {
			tempAdList.remove(ad);
			updateUser(tempAdList, user);
			return BookmarkStatus.NotBookmarked.getStatusCode();
		}
		
		if(!bookmarked) {
			tempAdList.add(ad);
			updateUser(tempAdList, user);
			return BookmarkStatus.Bookmarked.getStatusCode();
		}
		
		return BookmarkStatus.NoUserFound.getStatusCode();
	}
	
	// updates effectively the new List into DB
	private void updateUser(List<Ad> bookmarkedAds, User user) {
		userDao.save(user);	
	}
}
