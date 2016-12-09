package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ch.unibe.ese.team3.model.enums.PageMode;

public class PlaceAdControllerTest extends BaseControllerTest {

	
	
	@Test
	public void placeAd() throws Exception{
		this.mockMvc.perform(get("/profile/placeAd")).andExpect(status().isOk())
		.andExpect(view().name("placeAd"))
		.andExpect(model().attributeExists("types", "distances"));
	}
	
	
	@Test
	public void uploadPictures() throws Exception{
		
		MockMultipartFile pictureFile = new MockMultipartFile("HousePicture", "HousePicture.png", "image/png", "picture".getBytes());
        
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/profile/placeAd/uploadPictures")
                .file(pictureFile))
            .andExpect(status().is(200));
	}
	
	
	@Test
	public void createAdNoExistingAdress() throws Exception{
		
		this.mockMvc.perform(post("/profile/placeAd")
				.requestAttr("pageMode", PageMode.BUY).principal(getTestPrincipal("ese@unibe.ch"))
				.param("renovationYear", "0")
				.param("city", "3000 - Bern")
				.param("numberOfRooms", "2")
				.param("buildYear", "0")
				.param("title", "Nice cosy House")
				.param("type", "APARTMENT")
				.param("roomDescription", "this is a beautiful house near the sea")
				.param("squareFootage", "100")
				.param("street", "abc 13")
				.param("price", "1500")
				.param("infrastructureType", "SATELLITE")
				.param("_auction", "on")
				.param("visits[0]", "08-12-2016;13:10;17:00")
				.param("increaseBidPrice", "0")
				.param("_balcony", "on")
				.param("startPrice", "0")
				.param("floorLevel", "0")
				.param("numberOfBath", "1")
				.param("_dishwasher", "on")
				.param("_elevator", "on")
				.param("_parking", "on")
				.param("distanceSchool", "0")
				.param("moveInDate", "21-12-2016")
				.param("distancePublicTransport", "0")
				.param("auctionPrice", "0")
				.param("_garage", "on")
				.param("distanceShopping", "0")
				.param("startDate", "")
				.param("endDate", ""))
				.andExpect(flash().attributeExists("confirmationMessage"))
				.andExpect(flash().attributeExists("warningMessage"));
		
	}
	@Test
	public void createNotValidAd() throws Exception{
		
		this.mockMvc.perform(post("/profile/placeAd")
				.requestAttr("pageMode", PageMode.BUY).principal(getTestPrincipal("ese@unibe.ch")))

				.andExpect(view().name("placeAd"))
				.andExpect(model().attributeExists("types"))
				.andExpect(model().attributeExists("distances"));
		
	}
	
	
	@Test
	public void getUploadedPicture() throws Exception{
		this.mockMvc.perform(post("/profile/placeAd/getUploadedPictures")).andExpect(status().is(200));
	}
	
	
	@Test
	public void deleteUploadedPicture() throws Exception{
	 this.mockMvc.perform(post("/profile/placeAd/deletePicture").param("url", "HousePicture.png")).andExpect(status().is(200));
	}
}

