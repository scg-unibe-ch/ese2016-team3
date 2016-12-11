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
import ch.unibe.ese.team3.model.CreditcardType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.PremiumChoiceDao;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class SignupServiceTest {
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PremiumChoiceDao premiumChoiceDao;
	
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
	
	@Transactional
	@Test
	public void testSaveFromPremiumUser() {
		SignupForm form = new SignupForm();
		
		form.setEmail("test@test.com");
		form.setFirstName("John");
		form.setLastName("Test");
		form.setPassword("123test");
		form.setGender(Gender.MALE);
		form.setCreditCard("1111-1111-1111-1111");
		form.setCreditcardName("john test");
		form.setCreditcardType(CreditcardType.VISA);
		form.setExpirationMonth("11");
		form.setExpirationYear("23");
		form.setSecurityNumber("333");
		form.setIsPremium(true);
		form.setDuration(7);
		
		signupService.saveFrom(form);
		
		User serviceUser = userDao.findByUsername("test@test.com");
		
		
		assertEquals("test@test.com", serviceUser.getUsername());
		assertEquals("123test", serviceUser.getPassword());
		assertEquals("John", serviceUser.getFirstName());
		assertEquals("Test", serviceUser.getLastName());
		assertEquals(Gender.MALE, serviceUser.getGender());
		assertEquals("1111111111111111", serviceUser.getCreditCard());// formatted creditcard
		assertEquals("john test", serviceUser.getCreditcardName());
		assertEquals(CreditcardType.VISA, serviceUser.getCreditcardType());
		assertEquals("11", serviceUser.getExpirationMonth());
		assertEquals("23", serviceUser.getExpirationYear());
		assertEquals("333", serviceUser.getSecurityNumber());
		assertEquals(true, serviceUser.isPremium());
		assertEquals(7, serviceUser.getPremiumChoice().getDuration());
		
	}
		
	@Test(expected=IllegalArgumentException.class)
	public void testSignupExistingUserShouldThrow(){
		SignupForm signupForm = new SignupForm();
		signupForm.setEmail("ese@unibe.ch");
		signupForm.setFirstName("John");
		signupForm.setLastName("Test");
		signupForm.setPassword("123test");
		signupForm.setGender(Gender.MALE);
		
		signupService.saveFrom(signupForm);
	}
}
