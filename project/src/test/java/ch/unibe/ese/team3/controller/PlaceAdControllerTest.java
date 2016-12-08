package ch.unibe.ese.team3.controller;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.enums.PageMode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

public class PlaceAdControllerTest extends BaseControllerTest {

	
	
	@Test
	public void placeAd() throws Exception{
		this.mockMvc.perform(get("/profile/placeAd")).andExpect(status().isOk())
		.andExpect(view().name("placeAd"))
		.andExpect(model().attributeExists("types", "distances"));
	}
	
	
	@Test
	public void uploadPictures(){
		//this.mockMvc.perform(post("/profile/placeAd/uploadPictures").param(name, values))
	/*	
		MockMultipartFile pictureFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"json\": \"someValue\"}".getBytes());
		
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/profile/placeAd/uploadPictures")
                .file(pictureFile))
            .andExpect(status().is(200))
           .andExpect(content().string("success"));
	*/ 	
		
		
		
		/*
		 * 
		 * 
		 * Path path = Paths.get("/Users/sarahmorillo/Desktop/Derby_Osbornedale_house.jpg");
		 * byte[] data = Files.readAllBytes(path);
		 *  MockMultipartFile file = new MockMultipartFile("Derby_Osbornedale_house.jpg", data);
		 *  
		 * 
		 * 
		 Path path = Paths.get("c:\\temp\\test.zip");
            byte[] data = Files.readAllBytes(path);
            MockMultipartFile file = new MockMultipartFile("test.zip", "test.zip",
                    "application/zip", data);
            MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
            String boundary = "q1w2e3r4t5y6u7i8o9";
            mockRequest.setContentType("multipart/form-data; boundary="+boundary);
            mockRequest.setContent(createFileContent(data,boundary,"application/zip","test.zip"));
            mockRequest.addFile(file);
		  
		  
		
	
		 */
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
		this.mockMvc.perform(post("/profile/placeAd/getUploadedPictures")).andExpect(null);
	}
	
	
	@Test
	public void deleteUploadedPicture(){
	//	this.mockMvc.perform(post("/profile/placeAd/deletePicture").param("url", "...?"));
	}
}

