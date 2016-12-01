package ch.unibe.ese.team3.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;

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
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	//@InjectMocks
	//private ApplicationContext applicationContext;
	
	//private MockHttpServletRequest request;
	//private MockHttpServletResponse response;
	//private HandlerAdapter handlerAdapter;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();

	
		
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
	
	private void login() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.defaultRequest(get("/").with(user("ese@unibe.ch").roles("USER")))
						.addFilters(springSecurityFilterChain)
						.build();
	}
	
	@Test 
	public void postAd() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAd(ese);
		this.mockMvc.perform(
				post("/ad?id=" + ad.getId()))
				.andExpect(status().is3xxRedirection());
	}
	
	
	/*@Test
	public void test() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(this.adControllerMock).build();
		mockMvc.perform(post("/ad").param())
		.andExpect(status().isOk())
		.andExpect(request().sessionAttribute(AdController.))
		.andExpect(redirectedUrl())
		
	}*/
	
	private Ad generateAd(User user) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		String roomDescription13 = "This is a beautiful Villa near the Sea";
		Date startAuctionDate1 = formatter.parse("02.11.2016");
		Date endAuctionDate1 = formatter.parse("20.01.2017");
		Date moveInDate9 = formatter.parse("11.12.2016");
		Date creationDate9 = formatter.parse("01.11.2016");


		Ad adInterlaken = new Ad();
		adInterlaken.setZipcode(3800);
		adInterlaken.setType(Type.VILLA);
		adInterlaken.setBuyMode(BuyMode.BUY);
		adInterlaken.setMoveInDate(moveInDate9);
		adInterlaken.setCreationDate(creationDate9);
		adInterlaken.setSquareFootage(100);
		adInterlaken.setRoomDescription(roomDescription13);
		adInterlaken.setUser(user);
		adInterlaken.setTitle("Vintage Villa");
		adInterlaken.setStreet("Spühlibachweg 10");
		adInterlaken.setCity("Interlaken");
		adInterlaken.setLatitude(46.68638);
		adInterlaken.setLongitude(7.8729456);
		adInterlaken.setNumberOfBath(5);

		adInterlaken.setBalcony(true);
		adInterlaken.setGarage(true);
		adInterlaken.setDishwasher(true);
		adInterlaken.setElevator(false);
		adInterlaken.setGarage(true);
		adInterlaken.setBuildYear(1999);
		adInterlaken.setRenovationYear(2015);
		adInterlaken.setDistancePublicTransport(1000);
		adInterlaken.setDistanceSchool(2000);
		adInterlaken.setDistanceShopping(800);
		adInterlaken.setFloorLevel(1);
		adInterlaken.setNumberOfRooms(10);
		adInterlaken.setInfrastructureType(InfrastructureType.CABLE);

		adInterlaken.setAuctionPrice(500000);
		adInterlaken.setAuction(true);
		adInterlaken.setStartPrice(150000);
		adInterlaken.setPrice(150000);
		adInterlaken.setIncreaseBidPrice(100);
		adInterlaken.setcurrentAuctionPrice(adInterlaken.getStartPrice() + adInterlaken.getIncreaseBidPrice());
		adInterlaken.setStartDate(startAuctionDate1);
		adInterlaken.setEndDate(endAuctionDate1);

		adDao.save(adInterlaken);
		
		return adInterlaken;
	}


}
