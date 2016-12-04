package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

import ch.unibe.ese.team3.model.enums.PageMode;

public class IndexControllerTest extends BaseControllerTest {
		
	@Test
	public void indexPage() throws Exception{
		this.mockMvc.perform(get("/").requestAttr("pageMode", PageMode.BUY)).andExpect(status().isOk())
		.andExpect(view().name("index"))
		.andExpect(model().attributeExists("newest", "types"));
	}
}
