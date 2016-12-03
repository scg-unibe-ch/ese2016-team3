package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class SignupServiceTest {
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testSaveFrom(){
		SignupForm signupForm = new SignupForm();
		signupForm.setEmail("test@test.com");
		signupForm.setFirstName("John");
		signupForm.setLastName("Test");
		signupForm.setPassword("123test");
		signupForm.setGender(Gender.MALE);
		
		signupService.saveFrom(signupForm);
		
		User serviceUser = userDao.findByUsername("test@test.com");
		
		
		assertEquals("test@test.com", serviceUser.getUsername());
		assertEquals("123test", serviceUser.getPassword());
		assertEquals("John", serviceUser.getFirstName());
		assertEquals("Test", serviceUser.getLastName());
		assertEquals(Gender.MALE, serviceUser.getGender());
		
	}
	
	@Test
	public void testDoesUserWithUsernameExist(){
		boolean exists = signupService.doesUserWithUsernameExist("ese@unibe.ch");
		boolean doesNotExist = signupService.doesUserWithUsernameExist("john@doe.com");
		
		assertEquals(true, exists);
		assertEquals(false, doesNotExist);
	}
}
