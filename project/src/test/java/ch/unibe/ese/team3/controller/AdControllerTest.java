package ch.unibe.ese.team3.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

public class AdControllerTest extends BaseControllerTest {

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
		this.mockMvc.perform(get("/ad").principal(getTestPrincipal("ese@unibe.ch")).param("id", "2")).andExpect(status().isOk())
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
		this.mockMvc.perform(get("/ad").principal(getTestPrincipal("ese@unibe.ch")).param("id", "13")).andExpect(status().isOk())
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
				.perform(post("/ad").principal(getTestPrincipal("ese@unibe.ch")).param("id", "1")
						.param("recipient", "blabla@ithaca.com").param("subject", "subject").param("text", "Text"))
				.andExpect(status().isOk()).andExpect(view().name("adDescription"))
				.andExpect(model().attribute("errorMessage", "Could not send message. The recipient is invalid"));
	}

	@Test
	public void sendMessageInvalidForm() throws Exception {
		this.mockMvc
				.perform(post("/ad").principal(getTestPrincipal("ese@unibe.ch")).param("id", "1")
						.param("recipient", "blabla@ithaca.com").param("subject", "subject"))
				.andExpect(status().isOk()).andExpect(view().name("adDescription"))
				.andExpect(model().attributeHasErrors("messageForm"));
	}

	@Test
	public void sendMessageSuccess() throws Exception {
		this.mockMvc
				.perform(post("/ad").principal(getTestPrincipal("ese@unibe.ch")).param("id", "1").param("recipient", "jane@doe.com")
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
		.perform(get("/profile/myRooms").principal(getTestPrincipal("ese@unibe.ch")))
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
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "2")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("4", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusIsBookmarked() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "1")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("3", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusIsNotBookmarked() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "13")
				.param("screening", "true")
				.param("bookmarked", "true"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("2", result.getResponse().getContentAsString());
	}
	
	@Test
	public void isBookmarkStatusBookmark() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/bookmark").principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "14")
				.param("screening", "false")
				.param("bookmarked", "false"))
		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("3", result.getResponse().getContentAsString());
	}
}
