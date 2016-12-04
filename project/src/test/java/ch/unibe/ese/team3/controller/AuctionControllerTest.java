package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import org.junit.Test;

public class AuctionControllerTest extends BaseControllerTest {
	
	@Test
	public void bidNotAuthenticated() throws Exception{
		mockMvc.perform(post("/profile/bidAuction")
				.param("id", "13")
				.param("amount", "500100"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void bidAuthenticatedSuccess() throws Exception{
		mockMvc.perform(post("/profile/bidAuction")
				.principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "13")
				.param("amount", "500100"))
		.andExpect(status().is3xxRedirection())
		.andExpect(flash().attributeExists("confirmationMessage"));
	}
	
	@Test
	public void bidAuthenticatedFail() throws Exception{
		mockMvc.perform(post("/profile/bidAuction")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("id", "13")
				.param("amount", "500100"))
		.andExpect(status().is3xxRedirection())
		.andExpect(flash().attributeExists("errorMessage"));
	}
	
	@Test
	public void buyNotAuthenticated() throws Exception{
		mockMvc.perform(post("/profile/buyAuction")
				.param("id", "13"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void buyAuthenticatedSuccess() throws Exception{
		mockMvc.perform(post("/profile/buyAuction")
				.principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "13"))
		.andExpect(status().is3xxRedirection())
		.andExpect(flash().attributeExists("confirmationMessage"));
	}
	
	@Test
	public void buyAuthenticatedFail() throws Exception{
		mockMvc.perform(post("/profile/buyAuction")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("id", "13"))
		.andExpect(status().is3xxRedirection())
		.andExpect(flash().attributeExists("errorMessage"));
	}
	
	@Test
	public void showAuctionManagementNotAuthenticated() throws Exception{
		mockMvc.perform(get("/profile/auctions"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void showAuctionManagementSuccess() throws Exception{
		mockMvc.perform(get("/profile/auctions").principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionManagement"))
		.andExpect(model().attributeExists("runningAuctions", "stoppedAuctions", "expiredAuctions", "notStartedAuctions", "completedAuctions"));
	}
	
	@Test
	public void showMyAuctionsNotAuthenticated() throws Exception{
		mockMvc.perform(get("/profile/mybids"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void showMyAuctionsSuccess() throws Exception{
		mockMvc.perform(get("/profile/mybids").principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk())
		.andExpect(view().name("MyBids"))
		.andExpect(model().attributeExists("myauctions"));
	}
	
	@Test
	public void showAuctionDetailsNotAuthenticated() throws Exception{
		mockMvc.perform(get("/profile/auction").param("id", "2"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void showAuctionDetailsSuccess() throws Exception{
		mockMvc.perform(get("/profile/auction")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("id", "13"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("ad", "bids", "purchaseRequests"));
	}
	
	@Test
	public void showAuctionDetailsNonExistingAd() throws Exception{
		mockMvc.perform(get("/profile/auction")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("id", "999999"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void showAuctionDetailsNotOwner() throws Exception{
		mockMvc.perform(get("/profile/auction")
				.principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "13"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void showAuctionDetailsNoAuctionAd() throws Exception{
		mockMvc.perform(get("/profile/auction")
				.principal(getTestPrincipal("ese@unibe.ch"))
				.param("id", "2"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void completeAuctionNotAuthenticated() throws Exception{
		mockMvc.perform(post("/profile/auction/complete")
				.param("adIdComplete", "13"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void completeAuctionSuccess() throws Exception{
		mockMvc.perform(post("/profile/auction/complete")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("adIdComplete", "13"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("confirmationMessage"));
	}
	
	@Test
	public void completeAuctionFailure() throws Exception{
		mockMvc.perform(post("/profile/auction/complete")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("adIdComplete", "17"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("errorMessage"));
	}
	
	@Test
	public void resumeAuctionNotAuthenticated() throws Exception{
		mockMvc.perform(post("/profile/auction/resume")
				.param("adIdResume", "15"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void resumeAuctionSuccess() throws Exception {
		mockMvc.perform(post("/profile/auction/resume")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("adIdResume", "15"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("confirmationMessage"));
	}
	
	@Test
	public void resumeAuctionFailure() throws Exception {
		mockMvc.perform(post("/profile/auction/resume")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("adIdResume", "13"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("errorMessage"));
	}
	
	@Test
	public void pauseAuctionNotAuthenticated() throws Exception{
		mockMvc.perform(post("/profile/auction/pause")
				.param("adIdPause", "13"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void pauseAuctionSuccess() throws Exception {
		mockMvc.perform(post("/profile/auction/pause")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("adIdPause", "13"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("confirmationMessage"));
	}
	
	@Test
	public void pauseAuctionFailure() throws Exception {
		mockMvc.perform(post("/profile/auction/pause")
				.principal(getTestPrincipal("jane@doe.com"))
				.param("adIdPause", "15"))
		.andExpect(status().isOk())
		.andExpect(view().name("AuctionDetails"))
		.andExpect(model().attributeExists("errorMessage"));
	}
	
}
