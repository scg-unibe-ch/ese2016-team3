package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import java.security.Principal;

import ch.unibe.ese.team3.controller.pojos.forms.GoogleSignupForm;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class GoogleServiceTest {
	
	@Autowired
	private GoogleService googleService;
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testSaveFrom(){
		GoogleSignupForm googleSignupForm = new GoogleSignupForm();
		googleSignupForm.setGooglePicture("Testurl.com");
		googleSignupForm.setEmail("test@test.com");
		googleSignupForm.setFirstName("John");
		googleSignupForm.setLastName("Test");
		
		googleService.saveFrom(googleSignupForm);
		
		User serviceUser = userDao.findByUsername("test@test.com");
		
		assertEquals(true, serviceUser.getIsGoogleUser());
		assertEquals("test@test.com", serviceUser.getUsername());
		assertEquals("John", serviceUser.getFirstName());
		assertEquals("Test", serviceUser.getLastName());
		assertEquals(Gender.OTHER, serviceUser.getGender());
		assertEquals(AccountType.BASIC, serviceUser.getAccountType());
	}
	
	@Test
	public void testDoesUserWithUsernameExist(){
		boolean exists = googleService.doesUserWithUsernameExist("ese@unibe.ch");
		boolean doesNotExist = googleService.doesUserWithUsernameExist("john@doe.com");
		
		assertEquals(true, exists);
		assertEquals(false, doesNotExist);
	}
	
	@Test
	public void testLoginFrom(){
		GoogleSignupForm googleSignupForm = new GoogleSignupForm();
		googleSignupForm.setGooglePicture("Testurl.com");
		googleSignupForm.setEmail("google@test.com");
		googleSignupForm.setFirstName("Google");
		googleSignupForm.setLastName("Test");
		
		assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
		
		googleService.loginFrom(googleSignupForm);
		
		Principal loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User check = userDao.findByUsername("google@test.com");
		
		assertEquals(loggedInUser.getName(), check.getUsername());
		
	}
}
