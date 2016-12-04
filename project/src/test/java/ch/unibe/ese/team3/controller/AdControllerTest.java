package ch.unibe.ese.team3.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
@Transactional
public class AdControllerTest {

	private MockMvc mockMvc;

	private Principal getTestPrincipal() {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "ese@unibe.ch";
			}
		};
		return principal;
	}
	
	private Principal getTestPrincipal(String name) {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return name;
			}
		};
		return principal;
	}

	@Autowired
	private WebApplicationContext context;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void getAdNotAuthenticated() throws Exception {
		this.mockMvc.perform(get("/ad").param("id", "1")).andExpect(status().isOk())
				.andExpect(view().name("adDescription"))
				.andExpect(model().attributeExists("shownAd", "messageForm", "loggedInUserEmail", "visits"));
	}

	@Test
	public void getNonExistingAd() throws Exception {
		this.mockMvc.perform(get("/ad").param("id", "9999999")).andExpect(status().isNotFound());
	}

	@Test
	public void getAdAuthenticated() throws Exception {
		this.mockMvc.perform(get("/ad").principal(getTestPrincipal()).param("id", "2")).andExpect(status().isOk())
				.andExpect(view().name("adDescription")).andExpect(model().attributeExists("shownAd", "sentEnquiries",
						"messageForm", "loggedInUserEmail", "visits"));
	}

	@Test
	public void getAuctionAdNotAuthenticated() throws Exception {
		this.mockMvc.perform(get("/ad").param("id", "13")).andExpect(status().isOk())
				.andExpect(view().name("adDescription"))
				.andExpect(model().attributeExists("shownAd", "messageForm", "loggedInUserEmail", "visits", "bids"));
	}

	@Test
	public void getAuctionAdAuthenticated() throws Exception {
		this.mockMvc.perform(get("/ad").principal(getTestPrincipal()).param("id", "13")).andExpect(status().isOk())
				.andExpect(view().name("adDescription")).andExpect(model().attributeExists("shownAd", "sentEnquiries",
						"messageForm", "loggedInUserEmail", "visits", "bids", "sentBuyRequest"));
	}

	@Test
	public void sendMessageNotAuthenticated() throws Exception {
		this.mockMvc.perform(post("/ad").param("id", "1")).andExpect(status().is4xxClientError());
	}

	@Test
	public void sendMessageInvalidRecipient() throws Exception {
		this.mockMvc
				.perform(post("/ad").principal(getTestPrincipal()).param("id", "1")
						.param("recipient", "blabla@ithaca.com").param("subject", "subject").param("text", "Text"))
				.andExpect(status().isOk()).andExpect(view().name("adDescription"))
				.andExpect(model().attribute("errorMessage", "Could not send message. The recipient is invalid"));
	}

	@Test
	public void sendMessageInvalidForm() throws Exception {
		this.mockMvc
				.perform(post("/ad").principal(getTestPrincipal()).param("id", "1")
						.param("recipient", "blabla@ithaca.com").param("subject", "subject"))
				.andExpect(status().isOk()).andExpect(view().name("adDescription"))
				.andExpect(model().attributeHasErrors("messageForm"));
	}

	@Test
	public void sendMessageSuccess() throws Exception {
		this.mockMvc
				.perform(post("/ad").principal(getTestPrincipal()).param("id", "1").param("recipient", "jane@doe.com")
						.param("subject", "subject").param("text", "Text"))
				.andExpect(status().isOk()).andExpect(view().name("adDescription")).andExpect(model().hasNoErrors())
				.andExpect(model().attributeDoesNotExist("errorMessage"));
	}

	@Test
	public void myRoomsNotAuthenticated() throws Exception {
		this.mockMvc
				.perform(get("/profile/myRooms"))
				.andExpect(status().isOk())
				.andExpect(view().name("home"));
	}
	
	@Test
	public void myRoomsAuthenticated() throws Exception {
		this.mockMvc
		.perform(get("/profile/myRooms").principal(getTestPrincipal()))
		.andExpect(status().isOk())
		.andExpect(view().name("myRooms"))
		.andExpect(model().attributeExists("bookmarkedAdvertisements", "ownAdvertisements"));
	}
	
	@Test
	public void isBookmarkedNotAuthenticated() throws Exception{
		MvcResult result = this.mockMvc.perform(post("/bookmark")
				.param("id", "1")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("0", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusInvalidUser() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal("test"))
				.param("id", "1")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("1", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusOwnAd() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal())
				.param("id", "2")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("4", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusIsBookmarked() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal())
				.param("id", "1")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("3", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusIsNotBookmarked() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal())
				.param("id", "13")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("2", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusBookmark() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal())
				.param("id", "14")
				.param("screening", "false")
				.param("bookmarked", "false"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("3", result.getResponse().getContentAsString());
	}
}
