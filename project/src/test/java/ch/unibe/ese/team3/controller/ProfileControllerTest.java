package ch.unibe.ese.team3.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;

public class ProfileControllerTest extends BaseControllerTest {
	
	@Autowired
	WebApplicationContext context;
	
	@Autowired
	MockHttpServletRequest request;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	AdDao adDao;

	@Test 
	public void getLoginPage() throws Exception {
		this.mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"))
			.andExpect(model().attributeExists("googleForm"));
	}
	
	// cannot test google login in mockMvc
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
	public void testDuplicateUsernameSignup() throws Exception {
		this.mockMvc.perform(post("/signup")
				.param("email", "ese@unibe.ch")
				.param("password", "123123")
				.param("firstName", "Hans")
				.param("lastName", "Heiri")
				.param("isPremium", "false")
				.param("gender", "MALE"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"))
			.andExpect(model().attributeExists("signupForm", "genders", 
					"accountTypes", "creditcardTypes", "years", "months",
					"premiumChoices", "errorMessage"));	
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
	public void testUsernameTakenSignup() throws Exception {
		this.mockMvc.perform(post("/signup")
				.param("email", "ese@unibe.ch")
				.param("password", "halloo")
				.param("firstName", "Hans")
				.param("lastName", "Heiri")
				.param("isPremium", "false")
				.param("gender", "MALE"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"))
			.andExpect(model().attributeExists("signupForm", "genders", 
					"accountTypes", "creditcardTypes", "years", "months",
					"premiumChoices", "errorMessage"));
	}
	
	@Test
	public void testValidPremiumSignup() throws Exception {
		this.mockMvc.perform(post("/signup")
				.param("email", "kolleg@ithaca.ch")
				.param("password", "halloo")
				.param("firstName", "Hans")
				.param("lastName", "Heiri")
				.param("isPremium", "false")
				.param("gender", "MALE")
				.param("_isPremium", "on")
				.param("isPremium", "true")
				.param("creditCard", "1111-1111-1111-1111")
				.param("creditcardType", "VISA")
				.param("securityNumber", "333")
				.param("expirationMonth", "12")
				.param("expirationYear", "2020")
				.param("creditcardName", "hans")
				.param("duration", "7"))
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
	public void testInvalidUsernameEditProfile() throws Exception {
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
	
	// cannot test authentication manager in mockMvc (e.g. change of username or password)
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
	
	@Test 
	public void getUserPage() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(get("/user")
				.principal(principal)
				.param("id", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("user"))
			.andExpect(model().attributeExists("user", "messageForm", "principalID"));
	}
	
	@Test 
	public void getUserPageNotLoggedIn() throws Exception {
		this.mockMvc.perform(get("/user")
				.param("id", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("user"))
			.andExpect(model().attributeExists("user", "messageForm"));
	}
	
	@Test
	public void getUpgradePage() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(get("/profile/upgrade")
				.principal(principal))
			.andExpect(status().isOk())
			.andExpect(view().name("upgrade"))
			.andExpect(model().attributeExists("upgradeForm", "creditcardTypes", "accountTypes",
					"currentUser", "years", "months", "premiumChoices"));
	}
	
	@Test 
	public void testSuccessfulUpgrade() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		User user = userDao.findByUsername(principal.getName());
		
		this.mockMvc.perform(post("/profile/upgrade")
				.principal(principal)
				.param("creditCard", "1111-1111-1111-1111")
				.param("creditcardType", "VISA")
				.param("securityNumber", "333")
				.param("expirationMonth", "12")
				.param("expirationYear", "2020")
				.param("creditcardName", "hans")
				.param("duration", "7"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:../user?id=" + user.getId()))
		.andExpect(flash().attributeExists("confirmationMessage"));
	}
	
	@Test 
	public void testInvalidCreditCardUpgrade() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
				
		this.mockMvc.perform(post("/profile/upgrade")
				.principal(principal)
				.param("creditCard", "-1111-1111-1111")
				.param("creditcardType", "VISA")
				.param("securityNumber", "333")
				.param("expirationMonth", "12")
				.param("expirationYear", "2020")
				.param("creditcardName", "hans")
				.param("duration", "7"))
		.andExpect(status().isOk())
		.andExpect(view().name("upgrade"))
		.andExpect(model().attributeHasFieldErrors("upgradeForm", "creditCard"))
		.andExpect(model().attributeExists("upgradeForm", "creditcardTypes", "accountTypes",
					"currentUser", "years", "months", "premiumChoices"));
	}
	
	@Test
	public void getSchedulePage() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(get("/profile/schedule")
				.principal(principal))
		.andExpect(status().isOk())
		.andExpect(view().name("schedule"))
		.andExpect(model().attributeExists("visits", "presentations"));
	}
	
	@Test
	public void getSchedulePageNoPresentations() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("System");
		
		this.mockMvc.perform(get("/profile/schedule")
				.principal(principal))
		.andExpect(status().isOk())
		.andExpect(view().name("schedule"))
		.andExpect(model().attributeExists("visits", "presentations"));
	}
	
	@Test
	public void getVisitorPage() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("jane@doe.com");
		
		this.mockMvc.perform(get("/profile/visitors")
				.principal(principal)
				.param("visit", "45"))
		.andExpect(status().isOk())
		.andExpect(view().name("visitors"))
		.andExpect(model().attributeExists("visitors", "ad"));
	}
	
	@Test
	public void getVisitorPageNoVisits() throws Exception {
		Principal principal = mock(Principal.class);
		when(principal.getName()).thenReturn("ese@unibe.ch");
		
		this.mockMvc.perform(get("/profile/visitors")
				.principal(principal)
				.param("visit", "21"))
		.andExpect(status().isOk())
		.andExpect(view().name("visitors"))
		.andExpect(model().attributeExists("visitors", "ad"));
	}
	
	@Test
	public void doesEmailExistNonExistingMail() throws Exception{
		MvcResult result = this.mockMvc.perform(post("/signup/doesEmailExist").param("email", "notexisting@test.com"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("false", result.getResponse().getContentAsString());
	}
	
	@Test
	public void doesEmailExistExistingMail() throws Exception{
		MvcResult result = this.mockMvc.perform(post("/signup/doesEmailExist").param("email", "ese@unibe.ch"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("true", result.getResponse().getContentAsString());
	}

}
