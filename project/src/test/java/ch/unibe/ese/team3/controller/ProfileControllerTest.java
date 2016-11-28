package ch.unibe.ese.team3.controller;

import org.junit.Assert;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.unibe.ese.team3.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team3.controller.service.SignupService;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.dao.UserDao;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml" })
@WebAppConfiguration
public class ProfileControllerTest {
	
private MockMvc mockMvc;
	
	@Autowired
	@Mock
	private SignupService signupServiceMock;
	
	@Autowired
	@InjectMocks
	private ProfileController profileControllerMock;
	
	@Autowired 
	private UserDao userDao;
	
	
	//@InjectMocks
	//private ApplicationContext applicationContext;
	
	//private MockHttpServletRequest request;
	//private MockHttpServletResponse response;
	//private HandlerAdapter handlerAdapter;
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(profileControllerMock).build();
		
		//this.request = new MockHttpServletRequest();
		//this.response = new MockHttpServletResponse();
		
		//this.handlerAdapter = applicationContext.getBean(HandlerAdapter.class);
		
		SignupForm signup = new SignupForm();
		signup.setEmail("blabla");
		signup.setFirstName("hans");
		signup.setLastName("kuh");
		signup.setPassword("password");
		signup.setIsPremium(false);
		signup.setGender(Gender.OTHER);
				
			//.thenReturn(user = userDao.findByUsername("jane@doe.com"));
		
	}
	
	
	@Test
	public void test() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
				.andExpect(status().isOk())
				.andExpect(view().name("/signup"))
				.andExpect(forwardedUrl("/signup"))
				.andReturn();
		Assert.assertNotNull(result.getModelAndView());
		
	}

}
