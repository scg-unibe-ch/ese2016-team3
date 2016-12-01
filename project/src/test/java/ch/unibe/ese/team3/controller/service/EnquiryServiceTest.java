package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

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
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.VisitEnquiry;
import ch.unibe.ese.team3.model.VisitEnquiryState;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.model.dao.VisitDao;
import ch.unibe.ese.team3.model.dao.VisitEnquiryDao;

/**
 * 
 * Tests both Visit and VisitEnquiry functionality. Since one makes no sense
 * without the other, these tests were grouped into one suite.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class EnquiryServiceTest {
	
	@Autowired
	VisitService visitService;
	
	@Autowired
	EnquiryService enquiryService;
	
	@Autowired
	AdDao adDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	VisitDao visitDao;
	
	@Autowired
	VisitEnquiryDao visitEnquiryDao;
	
	User thomyG;
	
	Ad oltenResidence;
	
	Visit visit1, visit2;
	
	@Before
	public void createVisits() throws Exception {		
		//create user
		thomyG = createUser("thomy@g.ch", "password", "Thomy", "G",
				Gender.MALE);
		thomyG.setAboutMe("Supreme hustler");
		userDao.save(thomyG);
		
		//save an ad
		Date date = new Date();
		oltenResidence= new Ad();
		oltenResidence.setZipcode(4600);
		oltenResidence.setMoveInDate(date);
		oltenResidence.setCreationDate(date);
		oltenResidence.setPrice(1200);
		oltenResidence.setSquareFootage(42);
		//oltenResidence.setStudio(false);
		oltenResidence.setRoomDescription("blah");
		oltenResidence.setUser(thomyG);
		oltenResidence.setTitle("Olten Residence");
		oltenResidence.setStreet("Florastr. 100");
		oltenResidence.setCity("Olten");
		oltenResidence.setBalcony(false);
		oltenResidence.setGarage(false);
		adDao.save(oltenResidence);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		
		//ad two possible visiting times ("visits") to the ad
		visit1 = new Visit();
		oltenResidence.addVisit(visit1);
		visit1.setStartTimestamp(formatter.parse("16.12.2014 10:00"));
		visit1.setEndTimestamp(formatter.parse("16.12.2014 12:00"));
		visitDao.save(visit1);

		visit2 = new Visit();
		oltenResidence.addVisit(visit2);
		visit2.setStartTimestamp(formatter.parse("18.12.2014 10:00"));
		visit2.setEndTimestamp(formatter.parse("18.12.2014 12:00"));
		visitDao.save(visit2);
	}
	
	@Test
	public void createEnquiry(){
		User enquirySender = createUser("enquiry@sender.ch", "password", "Enquiry", "Sender", Gender.MALE);
		enquirySender.setAboutMe("About me");
		userDao.save(enquirySender);
		
		enquiryService.createEnquiry(visit1, enquirySender);
		
		List<VisitEnquiry> enquiries = convertToList(visitEnquiryDao.findBySender(enquirySender));
		assertEquals(1, enquiries.size());		
	}
	
	@Test
	public void createEnquiryAndAccept(){
		User ueliMaurer = createUser("ueli@maurer.ch", "password", "Ueli", "Maurer",
				Gender.MALE);
		ueliMaurer.setAboutMe("Wallis rocks");
		userDao.save(ueliMaurer);
		
		enquiryService.createEnquiry(visit1, ueliMaurer);
		
		List<VisitEnquiry> enquiries = convertToList(visitEnquiryDao.findBySender(ueliMaurer));
		VisitEnquiry enquiry = enquiries.get(0);
		
		enquiryService.acceptEnquiry(enquiry.getId());
		
		assertEquals(VisitEnquiryState.ACCEPTED, visitEnquiryDao.findOne(enquiry.getId()).getState());
		assertEquals(1, visitDao.findOne(visit1.getId()).getVisitors().size());
	}
	
	@Test
	public void createEnquiryAndDecline(){
		User ueliMaurer = createUser("ueli@maurer.ch", "password", "Ueli", "Maurer",
				Gender.MALE);
		ueliMaurer.setAboutMe("Wallis rocks");
		userDao.save(ueliMaurer);
		
		enquiryService.createEnquiry(visit1, ueliMaurer);
		
		List<VisitEnquiry> enquiries = convertToList(visitEnquiryDao.findBySender(ueliMaurer));
		VisitEnquiry enquiry = enquiries.get(0);
		
		enquiryService.declineEnquiry(enquiry.getId());
		
		assertEquals(VisitEnquiryState.DECLINED, visitEnquiryDao.findOne(enquiry.getId()).getState());
		assertEquals(0, visitDao.findOne(visit1.getId()).getVisitors().size());
	}
	
	@Test
	public void createEnquiryDeclineAndReopen(){
		User ueliMaurer = createUser("ueli@maurer.ch", "password", "Ueli", "Maurer",
				Gender.MALE);
		ueliMaurer.setAboutMe("Wallis rocks");
		userDao.save(ueliMaurer);
		
		enquiryService.createEnquiry(visit1, ueliMaurer);
		
		List<VisitEnquiry> enquiries = convertToList(visitEnquiryDao.findBySender(ueliMaurer));
		VisitEnquiry enquiry = enquiries.get(0);
		
		enquiryService.declineEnquiry(enquiry.getId());
		enquiryService.reopenEnquiry(enquiry.getId());
		
		assertEquals(VisitEnquiryState.OPEN, visitEnquiryDao.findOne(enquiry.getId()).getState());
		assertEquals(0, visitDao.findOne(visit1.getId()).getVisitors().size());
	}
	
	//Lean user creating method
	User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		user.addUserRole(role);
		return user;
	}
	
	public <T> List<T> convertToList(Iterable<T> iterable){
		List<T> list = new ArrayList<T>();
		iterable.forEach(x -> list.add(x));
		return list;
	}
}
