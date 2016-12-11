package ch.unibe.ese.team3.controller;

import static org.junit.Assert.assertEquals;
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
	public void editUserAdErrorInvalidParameters() throws Exception {
		this.mockMvc.perform(post("/profile/editAd").param("adId", "2").param("street", "Hochfeldstrasse 55")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().isOk()).andExpect(model().attributeExists("adId", "placeAdForm"));
	}

	@Test
	public void editUserAd() throws Exception {

		this.mockMvc.perform(post("/profile/editAd").param("id", "2").param("adId", "2").param("title", "testTitle")
				.param("street", "Hochfeldstrasse 55").param("city", "3012 - Bern").param("moveInDate", "2017-02-02")
				.param("price", "100000").param("roomDescription", "description")
				.param("type", Type.APARTMENT.toString()).param("squareFootage", "100").param("numberOfRooms", "3")

				.param("infrastructureType", InfrastructureType.CABLE.toString())
				.principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(status().is3xxRedirection()) // redirects after
														// editing of ad
				.andExpect(flash().attributeExists("confirmationMessage"));
	}

	@Test
	public void deletePictureFromAd() throws Exception {
		this.mockMvc
				.perform(post("/profile/editAd/deletePictureFromAd").principal(getTestPrincipal("ese@unibe.ch"))
						.param("adId", "2").param("pictureId", "2")) // insert right pictureId!

				.andExpect(status().isOk());
	}

	@Test
	public void getListOfPictureDescriptions() throws Exception {

		MvcResult result = this.mockMvc.perform(post("/profile/editAd/getUploadedPictures"))

				.andExpect(status().isOk()).andReturn();

		assertEquals("3", result.getResponse().getContentAsString());
	}

	@Test
	public void uploadPicture() throws Exception {
		MockMultipartFile pictureFile = new MockMultipartFile("HousePicture", "HousePicture.png", "image/png",
				"picture".getBytes());
		this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/profile/editAd/uploadPictures").file(pictureFile))
				.andExpect(status().isOk());
	}

	@Test
	public void deletePicture() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/profile/editAd/deletePicture").param("url", "/img/test/ad2_2.jpg"))
				.andExpect(status().isOk());
	}
}
