package ch.unibe.ese.team3.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;

public class EditAdControllerTest extends BaseControllerTest {
	@Test
	public void openEditAdPage() throws Exception {
		this.mockMvc.perform(get("/profile/editAd").param("id", "2").principal(getTestPrincipal("ese@unibe.ch")))
				.andExpect(status().isOk()).andExpect(model().attributeExists("adId", "placeAdForm"));
	}

	@Test
	public void userCantEditForeignAd() throws Exception {
		this.mockMvc.perform(get("/profile/editAd").param("id", "1").principal(getTestPrincipal("ese@unibe.ch")))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void editNonExistingAd() throws Exception {
		this.mockMvc.perform(get("/profile/editAd").param("id", "1011").principal(getTestPrincipal("ese@unibe.ch")))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void editAdInvalidFormParameters() throws Exception {
		this.mockMvc.perform(post("/profile/editAd").param("adId", "2").param("street", "Hochfeldstrasse 55")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().isOk()).andExpect(model().attributeExists("adId", "placeAdForm"));
	}

	@Test
	public void editAdOtherUser() throws Exception {
		this.mockMvc.perform(post("/profile/editAd").param("id", "1").param("adId", "1").param("title", "testTitle")
				.param("street", "Hochfeldstrasse 55").param("city", "3012 - Bern").param("moveInDate", "2017-02-27")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("squareFootage", "100").param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().is4xxClientError());
	}

	@Test
	public void editUserAd() throws Exception {
		this.mockMvc.perform(post("/profile/editAd").param("id", "2").param("adId", "2").param("title", "testTitle")
				.param("street", "Hochfeldstrasse 55").param("city", "3012 - Bern").param("moveInDate", "2017-02-27")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("squareFootage", "100").param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("confirmationMessage"));
	}

	@Test
	public void editAdWithoutPictures() throws Exception {
		this.mockMvc.perform(post("/profile/editAd").param("id", "20").param("adId", "20").param("title", "testTitle") // ad
																														// nr
																														// 20
																														// doesn't
																														// have
																														// pictures
				.param("street", "Hochfeldstrasse 55").param("city", "3012 - Bern").param("moveInDate", "2017-02-27")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("squareFootage", "100").param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("confirmationMessage"));
	}

	@Test
	public void warningMessageWhenInvalidAddressAfterEditing() throws Exception {
		this.mockMvc.perform(post("/profile/editAd").param("id", "2").param("adId", "2").param("title", "testTitle")
				.param("street", "hoceldstras").param("city", "3012 - Bern").param("moveInDate", "2017-02-27")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("squareFootage", "100").param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("warningMessage"));
	}

	@Test
	public void deletePictureFromAd() throws Exception {
		this.mockMvc
				.perform(post("/profile/editAd/deletePictureFromAd").principal(getTestPrincipal("ese@unibe.ch"))
						.param("adId", "2").param("pictureId", "2")) // insert
																		// right
																		// pictureId!

				.andExpect(status().isOk());
	}

	@Test
	public void getListOfPicturesNoPictureUploader() throws Exception {

		MvcResult result = this.mockMvc.perform(post("/profile/editAd/getUploadedPictures")).andExpect(status().isOk())
				.andReturn();

		assertEquals("", result.getResponse().getContentAsString());
	}
	
	@Test
	public void getListOfPicturesFromPictureUploader() throws Exception {
		// initialise pictureUploader
		MockMultipartFile pictureFile = new MockMultipartFile("HousePicture", "HousePicture.png", "image/png",
				"picture".getBytes());
		this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/profile/editAd/uploadPictures").file(pictureFile));
		
		// make sure right list of pictures is returned
		MvcResult result = this.mockMvc.perform(post("/profile/editAd/getUploadedPictures")).andExpect(status().isOk())
				.andReturn();

		assertTrue(result.getResponse().getContentAsString().contains("HousePicture.png"));
	}

	@Test
	public void getUploadedPicture() throws Exception {
		this.mockMvc.perform(post("/profile/editAd/getUploadedPictures")).andExpect(status().is(200));
	}

	@Test
	public void uploadPicture() throws Exception {
		MockMultipartFile pictureFile = new MockMultipartFile("HousePicture", "HousePicture.png", "image/png",
				"picture".getBytes());
		this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/profile/editAd/uploadPictures").file(pictureFile))
				.andExpect(status().isOk());
	}

	@Test
	public void deletePictureNoPictureUploader() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/profile/editAd/deletePicture").param("url", ""))
				.andExpect(status().isOk());
	}

	@Test
	public void deletePicture() throws Exception {
		this.mockMvc.perform(post("/profile/editAd/deletePicture").param("url", "")).andExpect(status().isOk()); // no real url or acctual picture will be deleted
	}
}
