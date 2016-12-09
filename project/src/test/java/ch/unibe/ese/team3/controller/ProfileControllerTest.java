package ch.unibe.ese.team3.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import java.security.Principal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.UserDao;

public class ProfileControllerTest extends BaseControllerTest {
	
	@Autowired
	WebApplicationContext context;
	
	@Autowired
	MockHttpServletRequest request;
	
	@Autowired
	UserDao userDao;
	
	@Test 
	public void getLoginPage() throws Exception {
		this.mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"))
			.andExpect(model().attributeExists("googleForm"));
	}
	
	/*
	@Test
	public void getGoogleLoginPage() throws Exception {
		this.mockMvc.perform(post("/googlelogin"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attributeExists("newest", "types", "searchForm"));
	}*/
	
	@Test
	public void getSignupPage() throws Exception {
		this.mockMvc.perform(get("/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"))
			.andExpect(model().attributeExists("signupForm", "genders", 
					"accountTypes", "creditcardTypes", "years", "months",
					"premiumChoices"));
	}
	
	@Test
	public void testInvalidPasswordSignup() throws Exception {
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
			.andExpect(model().attributeExists("signupForm", "genders", 
					"accountTypes", "creditcardTypes", "years", "months",
					"premiumChoices"));	
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
	public void testInvalidFirstNameEditProfile() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(post("/profile/editProfile")
				.principal(principal)
				.param("username", "")
				.param("password", "yoyoyo")
				.param("firstName", "lalala")
				.param("lastName", "bubli"))
			.andExpect(status().isOk())
			.andExpect(view().name("editProfile"))
			.andExpect(model().attributeHasFieldErrors("editProfileForm", "username"))
			.andExpect(model().attributeExists("editProfileForm", "currentUser"));
	}
	
	@Test
	public void testValidEditProfile() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		User user = userDao.findByUsername(principal.getName());

		this.mockMvc.perform(post("/profile/editProfile")
				.principal(principal)
				.param("username", "jane@doe.com")
				.param("password", "password")
				.param("firstName", "hibli")
				.param("lastName", "bubli"))
			.andExpect(status().is3xxRedirection()) // URL redirection status
			.andExpect(view().name("redirect:../user?id=" + user.getId()))
			.andExpect(flash().attributeExists("confirmationMessage"));
	}
	
	@Test
	public void testChangeEmailToExistingEmailProfile() throws Exception{
		this.mockMvc.perform(post("/profile/editProfile")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("username", "ese@unibe.ch")
				.param("password", "password")
				.param("firstName", "hibli")
				.param("lastName", "bubli"))
		.andExpect(status().isOk())
		.andExpect(view().name("editProfile"))
		.andExpect(model().attributeExists("editProfileForm", "currentUser", "errorMessage"));
	}
	
	@Test public void getUserPage() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(get("/user")
				.principal(principal)
				.param("id", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("user"))
			.andExpect(model().attributeExists("user", "messageForm", "principalID"));
	}

}
