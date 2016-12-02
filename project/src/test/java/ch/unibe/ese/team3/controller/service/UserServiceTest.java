package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
}
