package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.dao.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class VisitServiceTest {
	
	@Autowired
	private VisitService visitService;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testGetVisitByAd(){
		Ad ad = adDao.findOne(1L);
		
		Iterable<Visit> visits = visitService.getVisitsByAd(ad);
		
		int count = 0;
		
		for (Visit visit : visits){
			count++;
		}
		
		assertEquals(3, count);
	}
	
	@Test
	public void testGetVisitById() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		
		Visit visit = visitService.getVisitById(1L);
		
		Date expectedStart = dateFormat.parse("14:00 26.12.2014");
		Date expectedEnd = dateFormat.parse("16:00 26.12.2014");
		
		assertEquals(expectedStart, visit.getStartTimestamp());
		assertEquals(expectedEnd, visit.getEndTimestamp());
	}
	
	@Test
	public void testGetVisitsForUser(){
		User user = userDao.findByUsername("user@bern.com");
		
		Iterable<Visit> visitsForUser = visitService.getVisitsForUser(user);
		
		int count = 0;
		
		for (Visit visit : visitsForUser){
			count++;
		}
		
		assertEquals(20, count);		
	}
	
	@Test
	public void testGetVisitorsForVisit(){
		Iterable<User> visitors = visitService.getVisitorsForVisit(1L);
		
		int count = 0;
		
		for (User user : visitors){
			count++;
		}
		
		assertEquals(2, count);
	}
}
