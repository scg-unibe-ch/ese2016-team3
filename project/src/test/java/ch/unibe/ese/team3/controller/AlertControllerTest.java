package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

public class AlertControllerTest extends BaseControllerTest {

	@Test
	public void showAlertViewAfterAlertCreation() throws Exception {
		this.mockMvc.perform(post("/profile/alerts")
				.principal(getTestPrincipal("ese@unibe.ch"))
				.param("city", "3012 - Bern")
				.param("radius", "10")
				.param("price", "10") )   // set inputs of alert form. Attributes are set by name (name in alertForm).
				 
				.andExpect(status().isOk())
				.andExpect(view().name("alerts"))
				.andExpect(model().attributeExists("alerts", "types", "alertForm")); // attributes are refering to attributes of alert site. (over id?)
	}
	
	@Test
	public void displayAlertPage() throws Exception {
		this.mockMvc.perform(get("/profile/alerts")
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().isOk())
				.andExpect(view().name("alerts"))
				.andExpect(model().attributeExists("alerts", "types", "alertForm")); 
	}
	
	@Test
	public void deleteAlert() throws Exception {
		this.mockMvc.perform(get("/profile/alerts/deleteAlert")
				.principal(getTestPrincipal("ese@unibe.ch")) 
				.param("id", "1")
				)
				.andExpect(status().isOk());
	}
}