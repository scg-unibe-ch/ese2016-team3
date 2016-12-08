package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

import ch.unibe.ese.team3.model.enums.PageMode;

public class SearchControllerTest extends BaseControllerTest {
	
	@Test
	public void searchAdTest() throws Exception{
		mockMvc.perform(get("/searchAd"))
			.andExpect(status().isOk())
			.andExpect(view().name("searchAd"))
			.andExpect(model().attributeExists("types"));
			
	}
	
	@Test
	public void resultsSuccessNotAuthenticated() throws Exception{
		mockMvc.perform(post("/results")
				.param("city", "3000 - Bern")
				.param("radius", "5")
				.param("price", "1200000")
				.requestAttr("pageMode", PageMode.BUY))
		.andExpect(status().isOk())
		.andExpect(view().name("results"))
		.andExpect(model().attributeExists("results", "types", "infrastructureTypes", "resultsInJson"))
		.andExpect(model().attribute("loggedInUserEmail", ""));
	}
	
	@Test
	public void resultsSuccessAuthenticated() throws Exception{
		mockMvc.perform(post("/results")
				.param("city", "3000 - Bern")
				.param("radius", "500")
				.param("price", "1200000")
				.requestAttr("pageMode", PageMode.RENT)
				.principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(view().name("results"))
		.andExpect(model().attributeExists("results", "types", "infrastructureTypes", "resultsInJson"))
		.andExpect(model().attribute("loggedInUserEmail", "ese@unibe.ch"));
	}
	
	@Test
	public void resultsInvalidForm() throws Exception {
		mockMvc.perform(post("/results")
				.param("city", "3000000 - Bern")
				.param("radius", "500")
				.param("price", "1200000")
				.requestAttr("pageMode", PageMode.RENT)
				.principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(view().name("searchAd"))
		.andExpect(model().attributeDoesNotExist("results", "infrastructureTypes", "resultsInJson", "loggedInUserEmail"));
	}
}
