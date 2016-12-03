package ch.unibe.ese.team3.test.testData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;

/**
 * This inserts some bookmark test data into the database.
 */
@Service
public class BookmarkTestDataSaver{

	@Autowired
	private UserDao userDao;
	@Autowired
	private AdService adService;

	@Transactional
	public void saveTestData() throws Exception {
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User oprah = userDao.findByUsername("oprah@ithaca.com");

		// 5 bookmarks for Ese test-user
		ese.addBookmark(adService.getAdById(1));
		ese.addBookmark(adService.getAdById(3));
		ese.addBookmark(adService.getAdById(5));
		ese.addBookmark(adService.getAdById(7));
		ese.addBookmark(adService.getAdById(8));
		
		userDao.save(ese);

		// 4 bookmarks for Jane Doe
		jane.addBookmark(adService.getAdById(6));
		jane.addBookmark(adService.getAdById(9));
		jane.addBookmark(adService.getAdById(10));
		jane.addBookmark(adService.getAdById(11));
		userDao.save(jane);

		// 5 bookmarks for user berner bear
		bernerBaer.addBookmark(adService.getAdById(2));
		bernerBaer.addBookmark(adService.getAdById(4));
		bernerBaer.addBookmark(adService.getAdById(6));
		bernerBaer.addBookmark(adService.getAdById(8));
		bernerBaer.addBookmark(adService.getAdById(12));
		userDao.save(bernerBaer);

		// 4 bookmarks for Oprah
		oprah.addBookmark(adService.getAdById(2));
		oprah.addBookmark(adService.getAdById(3));
		oprah.addBookmark(adService.getAdById(6));
		oprah.addBookmark(adService.getAdById(12));
		userDao.save(oprah);
	}

}
