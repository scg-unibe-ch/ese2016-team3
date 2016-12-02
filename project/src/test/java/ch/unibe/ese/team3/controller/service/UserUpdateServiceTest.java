package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class UserUpdateServiceTest {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserUpdateService userUpdateService;
	
	@Test
	public void updateExistingUser(){
		EditProfileForm form = new EditProfileForm();
		User mark = userDao.findByUsername("mark@knopfler.com");
		String newUserName = "hallo@knopfler.com";
		String newFirstName = "Mark D.";
		String newLastName = "Knopfler Jr.";
		String newPassword = "sultans";
		String newAboutMe = "I'm the world's greatest guitar player!";
		
		form.setUsername(newUserName);
		form.setLastName(newLastName);
		form.setFirstName(newFirstName);
		form.setPassword(newPassword);
		form.setAboutMe(newAboutMe);
		
		userUpdateService.updateFrom(form, mark);
		
		mark = userDao.findByUsername(newUserName);

		assertEquals(newUserName, mark.getUsername());
		assertEquals(newFirstName, mark.getFirstName());
		assertEquals(newLastName, mark.getLastName());
		assertEquals(newPassword, mark.getPassword());
		assertEquals(newAboutMe, mark.getAboutMe());
	}
	
	@Test(expected=NullPointerException.class)
	public void updateNonExistingUser(){
		EditProfileForm form = new EditProfileForm();
		form.setUsername("nonexisting@user.com");
		User user = userDao.findByUsername("nonexisting@user.com");
		userUpdateService.updateFrom(form, user);
	}
	
}
