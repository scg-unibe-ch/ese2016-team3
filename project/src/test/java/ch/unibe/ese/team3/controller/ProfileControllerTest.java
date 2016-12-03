package ch.unibe.ese.team3.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class ProfileControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	WebApplicationContext context;
	
	@Autowired
	MockHttpServletRequest request;
	
	@Autowired
	UserDao userDao;

	@Before
	public void setUp() throws Exception {
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	
	@Test 
	public void getLoginPage() throws Exception {
		this.mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"));
	}
	
	@Test
	public void getSignupPage() throws Exception {
		this.mockMvc.perform(get("/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"))
			.andExpect(model().attributeExists("signupForm"));
	}
	
	@Test
	public void testInvalidSignup() throws Exception {
		this.mockMvc.perform(post("/signup")
				.param("email", "blu@ithaca.ch")
				.param("password", "")
				.param("firstName", "Hans")
				.param("lastName", "Heiri")
				.param("isPremium", "false")
				.param("gender", "MALE"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"))
			.andExpect(model().attributeHasFieldErrors("signupForm", "password"))
			.andExpect(model().attributeExists("signupForm"));	
	}
	
	@Test
	public void testValidSignup() throws Exception {
		this.mockMvc.perform(post("/signup")
				.param("email", "bla@ithaca.ch")
				.param("password", "halloo")
				.param("firstName", "Hans")
				.param("lastName", "Heiri")
				.param("isPremium", "false")
				.param("gender", "MALE"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"))
			.andExpect(model().attributeExists("confirmationMessage", "googleForm"));
	}
	
	@Test 
	public void getEditProfilePage() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("ese@unibe.ch");
		
		this.mockMvc.perform(get("/profile/editProfile")
				.principal(principal))
			.andExpect(status().isOk())
			.andExpect(view().name("editProfile"))
			.andExpect(model().attributeExists("editProfileForm", "currentUser"));
	}
	
	@Test
	public void testInvalidEditProfile() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(post("/profile/editProfile")
				.principal(principal)
				.param("username", "jane@doe.com")
				.param("password", "yoyoyo")
				.param("firstName", "")
				.param("lastName", "bubli"))
			.andExpect(status().isOk())
			.andExpect(view().name("editProfile"))
			.andExpect(model().attributeExists("editProfileForm"));
	}
	
	@Test
	public void testValidEditProfile() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		User user = userDao.findByUsername(principal.getName());

		this.mockMvc.perform(post("/profile/editProfile")
				.principal(principal)
				.param("username", "jap@blue.ch")
				.param("password", "yoyoyo")
				.param("firstName", "hibli")
				.param("lastName", "bubli"))
			.andExpect(status().is(302)) // URL redirection status
			.andExpect(view().name("redirect:../user?id=" + user.getId()));
	}

}
