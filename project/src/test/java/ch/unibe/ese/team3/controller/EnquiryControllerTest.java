package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

public class EnquiryControllerTest extends BaseControllerTest {
	
	@Test
	public void getEnquiriesPage() throws Exception{
		this.mockMvc.perform(get("/profile/enquiries")
				.principal(getTestPrincipal("ese@unibe.ch")))
			.andExpect(status().isOk())
			.andExpect(view().name("enquiries"))
			.andExpect(model().attributeExists("enquiries"));
	}
	
	@Test
	public void testSendEnquiryForVisitNotAuthenticated() throws Exception{
		mockMvc.perform(get("/profile/enquiries/sendEnquiryForVisit"))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testSendEnquiryForVisitNoId() throws Exception{
		mockMvc.perform(get("/profile/enquiries/sendEnquiryForVisit")
			.principal(getTestPrincipal("ese@unibe.ch")))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testSendEnquiryForVisit() throws Exception{
		mockMvc.perform(get("/profile/enquiries/sendEnquiryForVisit")
			.principal(getTestPrincipal("ese@unibe.ch"))
			.param("id", "2"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testAcceptEnquiryNoId() throws Exception{
		mockMvc.perform(get("/profile/enquiries/acceptEnquiry"))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testAcceptEnquiry() throws Exception{
		mockMvc.perform(get("/profile/enquiries/acceptEnquiry")
				.param("id", "2"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testDeclineEnquiryNoId() throws Exception{
		mockMvc.perform(get("/profile/enquiries/declineEnquiry"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testDeclineEnquiry() throws Exception{
		mockMvc.perform(get("/profile/enquiries/declineEnquiry")
				.param("id", "2"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testReopenEnquiryNoId() throws Exception{
		mockMvc.perform(get("/profile/enquiries/reopenEnquiry"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testReopenEnquiry() throws Exception{
		mockMvc.perform(get("/profile/enquiries/reopenEnquiry")
				.param("id", "2"))
		.andExpect(status().isOk());
	}
}
