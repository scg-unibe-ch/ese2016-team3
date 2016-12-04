package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.CreditcardType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testFindUserByUsername(){
		
		User serviceUser = userService.findUserByUsername("ese@unibe.ch");
		
		assertEquals("ese@unibe.ch", serviceUser.getUsername());
		assertEquals("ese", serviceUser.getPassword());
		assertEquals("John", serviceUser.getFirstName());
		assertEquals("Wayne", serviceUser.getLastName());
		assertEquals(Gender.MALE, serviceUser.getGender());
	}
	
	@Test
	public void testFindUserById(){
		User serviceUser = userService.findUserById(2L);
		
		assertEquals("ese@unibe.ch", serviceUser.getUsername());
		assertEquals("ese", serviceUser.getPassword());
		assertEquals("John", serviceUser.getFirstName());
		assertEquals("Wayne", serviceUser.getLastName());
		assertEquals(Gender.MALE, serviceUser.getGender());
	}
	
	@Test
	public void removeExpiredPremiumUsersTest(){
		User testUser = userService.findUserByUsername("ithacatest2@trash-mail.com");
		
		assertEquals(testUser.getAccountType(), AccountType.PREMIUM);
		assertEquals(testUser.getCreditCard(), "1111222233334444");
		assertEquals(testUser.getCreditcardType(), CreditcardType.VISA);
		assertEquals(testUser.getCreditcardName(), "Email Test");
		assertEquals(testUser.getSecurityNumber(), "123");
		assertEquals(testUser.getExpirationMonth(), "10");
		assertEquals(testUser.getExpirationYear(), "2020");
		
		Date expiryBefore = new Date();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(expiryBefore);
		calendar2.add(Calendar.HOUR_OF_DAY, -2);
		expiryBefore = calendar2.getTime();
		testUser.setPremiumExpiryDate(expiryBefore);
		userDao.save(testUser);
		
		userService.removeExpiredPremiumUsers();
		
		User testUser2 = userService.findUserByUsername("ithacatest2@trash-mail.com");
		assertEquals(testUser2.getAccountType(), AccountType.BASIC);
		assertEquals(testUser2.getCreditCard(), null);
		assertEquals(testUser2.getCreditcardType(), null);
		assertEquals(testUser2.getCreditcardName(), null);
		assertEquals(testUser2.getSecurityNumber(), null);
		assertEquals(testUser2.getExpirationMonth(), null);
		assertEquals(testUser2.getExpirationYear(), null);
		
	}
}
