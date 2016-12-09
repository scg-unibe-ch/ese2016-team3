package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

public class EditAdControllerTest extends BaseControllerTest{
	@Test
	public void openEditAdPage() throws Exception {
		this.mockMvc.perform(get("/profile/editAd")
				.param("id", "2")
				.principal(getTestPrincipal("ese@unibe.ch")) )
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("adId", "placeAdForm"));
	}

	
	@Test
	public void userCantEditForeignAd() throws Exception {
		this.mockMvc.perform(get("/profile/editAd")
				.param("id", "1")
				.principal(getTestPrincipal("ese@unibe.ch")) )
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void editAdOfOtherUserException() throws Exception {
		this.mockMvc.perform(get("/profile/editAd")
				.param("id", "1011")
				.principal(getTestPrincipal("ese@unibe.ch")) )
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void editUserAd() throws Exception {
		this.mockMvc.perform(post("/profile/editAd")
		.param("id", "2")
		.param("street", "Hochfeldstrasse 55")
		.param("city", "3012 - Bern")
		.param("moveInDate", "2017-02-02")
		.param("price", "100000")
		.param("roomDescription", "description")
		.param("type", "Type.APARTMENT")
		.param("squareFootage", "100")
		.param("numberOfRooms", "3")
		.param("infrastructureType", "InfrastructureType.CABLE")
		
		.principal(getTestPrincipal("ese@unibe.ch")) )
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("adId", "placeAdForm"));
	}
	
	@Test
	public void deletePictureFromAd() throws Exception {
		this.mockMvc.perform(post("/profile/editAd/deletePictureFromAd")
		.principal(getTestPrincipal("ese@unibe.ch"))
		.param("adId", "2")
		.param("pictureId", "2") )  // insert right pictureId

		.andExpect(status().isOk());
	}
	/*
	@Test
	public void getListOfPictureDescriptions() throws Exception {
		MvcResult result = this.mockMvc.perform(post("/profile/editAd/getUploadedPictures") )  

		.andExpect(status().isOk())
		.andReturn();
		
		assertEquals("3", result.getResponse().getContentAsString());  // method is called nowhere?
	}
	*/
	
	@Test
	public void uploadPicture() throws Exception {
		this.mockMvc.perform(post("/profile/editAd/uploadPictures")
		.principal(getTestPrincipal("ese@unibe.ch")) ) 
		
		.andExpect(status().isOk());
	}
	
	
}
