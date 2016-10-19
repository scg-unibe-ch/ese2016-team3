package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class BookmarkServiceTest {
	
	@Autowired
	private BookmarkService bookmarkService;
	
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
//	@Before
//	public void setUp(){
//		hans = createUser("hans@kanns.ch", "password", "Hans", "Kanns",
//				Gender.MALE);
//		userDao.save(hans);
//	}
	
	@Test
	public void testgetBookmarkStatusTrue(){
		
		assertEquals(2, bookmarkService.getBookmarkStatus(new Ad(), true, userDao.findByUsername("user@bern.com"))); 
		
	}
	
	@Test
	public void testgetBookmarkStatusFalse(){
		
		assertEquals(3, bookmarkService.getBookmarkStatus(adDao.findOne(1L), false, userDao.findByUsername("user@bern.com"))); 
	
	}

	@Test
	public void testgetBookmarkStatusTrue2(){
		
		assertNotEquals(1,bookmarkService.getBookmarkStatus(new Ad(), true, userDao.findByUsername("user@bern.com")) );;
	}


}
