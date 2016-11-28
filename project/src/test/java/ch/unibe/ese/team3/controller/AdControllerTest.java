package ch.unibe.ese.team3.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class AdControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	@Mock
	private AdService adServiceMock;
	
	@Autowired
	private AdController adControllerMock;
	
	@Autowired 
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	//@InjectMocks
	//private ApplicationContext applicationContext;
	
	//private MockHttpServletRequest request;
	//private MockHttpServletResponse response;
	//private HandlerAdapter handlerAdapter;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(adControllerMock).build();
		
		//this.request = new MockHttpServletRequest();
		//this.response = new MockHttpServletResponse();
		
		//this.handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
		User jane = userDao.findByUsername("jane@doe.com");
		PlaceAdForm adForm = new PlaceAdForm();
		adForm.setType(Type.VILLA);
		adForm.setMoveInDate("15.12.2014");
		adForm.setPrice(4500);
		adForm.setSquareFootage(42);
		adForm.setRoomDescription("blabla");
		adForm.setTitle("Malibu-style Beachhouse");
		adForm.setStreet("Via Serafino Balestra 36");
		adForm.setCity("Locarno");
		adForm.setNumberOfBath(1);

		adForm.setBalcony(true);
		adForm.setGarage(false);
		adForm.setDishwasher(true);
		adForm.setElevator(false);
		adForm.setGarage(true);
		adForm.setBuildYear(1800);
		adForm.setRenovationYear(1980);
		adForm.setDistancePublicTransport(300);
		adForm.setDistanceSchool(1000);
		adForm.setDistanceShopping(900);
		adForm.setFloorLevel(4);
		adForm.setNumberOfRooms(6);
		adForm.setInfrastructureType(InfrastructureType.CABLE);
		
		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		adForm.setAuction(false);
		Mockito.when(this.adServiceMock.saveFrom(adForm, filePaths, jane, BuyMode.BUY))
			.thenReturn(adDao.findByTitle("Malibu-style Beachhouse"));
		
	}
	
	
	/*@Test
	public void test() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(this.adControllerMock).build();
		mockMvc.perform(post("/ad").param())
		.andExpect(status().isOk())
		.andExpect(request().sessionAttribute(AdController.))
		.andExpect(redirectedUrl())
		
	}*/


}
