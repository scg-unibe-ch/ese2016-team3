package ch.unibe.ese.team3.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
	private WebApplicationContext context;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void getAd() throws Exception {
		this.mockMvc.perform(get("/ad")
						.param("id", "1"))
					.andExpect(status().isOk())
					.andExpect(view().name("adDescription"))
					.andExpect(model().attributeExists("shownAd", "messageForm", "loggedInUserEmail", "visits"));
	}
	
	@Test
	public void postAd() throws Exception {
		this.mockMvc.perform(post("/ad")
						.param("id", "1")
						.param("recipient", ""))
					.andExpect(status().isOk())
					.andExpect(view().name("adDescription"));
		
	}

}
