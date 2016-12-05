package ch.unibe.ese.team3.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Test;


import ch.unibe.ese.team3.model.enums.PageMode;

public class MessageControllerTest extends BaseControllerTest {
	
	@Test
	public void messagesNotAuthenticated() throws Exception{
		mockMvc.perform(get("/profile/messages")).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void messages() throws Exception {
		mockMvc.perform(get("/profile/messages").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(view().name("messages"))
		.andExpect(model().attributeExists("messageForm", "messages"));
	}
	
	@Test
	public void getInboxNotAuthenticated() throws Exception {
		mockMvc.perform(post("/profile/message/inbox")).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getInbox() throws Exception {
		mockMvc.perform(post("/profile/message/inbox").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	public void getSentNotAuthenticated() throws Exception {
		mockMvc.perform(post("/profile/message/sent")).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getSent() throws Exception {
		mockMvc.perform(post("/profile/message/sent").principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isArray());
	}
	
	@Test
	public void getMessageNotAuthenticated() throws Exception {
		mockMvc.perform(get("/profile/messages/getMessage").param("id", "1")).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getMessageNotExisting() throws Exception {
		mockMvc.perform(get("/profile/messages/getMessage").param("id", "99999").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void getMessageForeign() throws Exception {
		mockMvc.perform(get("/profile/messages/getMessage").param("id", "1").principal(getTestPrincipal("eric@clapton.com")))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getMessage() throws Exception {
		mockMvc.perform(get("/profile/messages/getMessage").param("id", "1").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").isNumber())
		.andExpect(jsonPath("$.sender").isString())
		.andExpect(jsonPath("$.recipient").isString())
		.andExpect(jsonPath("$.subject").isString())
		.andExpect(jsonPath("$.text").isString());
	}
	
	@Test
	public void sendMessageNotAuthenticated() throws Exception {
		mockMvc.perform(post("/profile/messages")
				.param("recipient", "ese@unibe.ch")
				.param("subject", "Subject")
				.param("text", "Text"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void sendMessageSuccess() throws Exception {
		mockMvc.perform(post("/profile/messages")
				.param("recipient", "ese@unibe.ch")
				.param("subject", "Subject")
				.param("text", "Text")
				.principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk())
		.andExpect(view().name("messages"))
		.andExpect(model().attributeExists("messages", "messageForm"))
		.andExpect(model().attributeDoesNotExist("errorMessage"));
	}
	
	@Test
	public void sendMessageInvalidUser() throws Exception {
		mockMvc.perform(post("/profile/messages")
				.param("recipient", "asdfg")
				.param("subject", "Subject")
				.param("text", "Text")
				.principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk())
		.andExpect(view().name("messages"))
		.andExpect(model().attributeExists("messages", "messageForm", "errorMessage"));
	}
	
	@Test
	public void sendMessageNotValid() throws Exception {
		mockMvc.perform(post("/profile/messages")
				.param("recipient", "ese@unibe.ch")
				.param("text", "Text")
				.principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk())
		.andExpect(view().name("messages"))
		.andExpect(model().attributeExists("messageForm"))
		.andExpect(model().attributeDoesNotExist("messages", "errorMessage"));
	}
	
	@Test
	public void readMessageNotAuthenticated() throws Exception {
		mockMvc.perform(post("/profile/readMessage").param("id", "1"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void readMessageNotExisting() throws Exception {
		mockMvc.perform(post("/profile/readMessage").param("id", "9999").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void readMessageForeign() throws Exception {
		mockMvc.perform(post("/profile/readMessage").param("id", "1").principal(getTestPrincipal("eric@clapton.com")))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void readMessage() throws Exception {
		mockMvc.perform(post("/profile/readMessage").param("id", "1").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getUnreadNotAuthenticated() throws Exception {
		mockMvc.perform(get("/profile/unread")).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void getUnread() throws Exception {
		mockMvc.perform(get("/profile/unread").principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isNumber());
	}
	
	@Test
	public void validateEmailValid() throws Exception {
		mockMvc.perform(post("/profile/messages/validateEmail")
				.param("email", "ese@unibe.ch")
				.principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", "ese@unibe.ch").isString());
	}
	
	@Test
	public void validateEmailInvalid() throws Exception {
		mockMvc.perform(post("/profile/messages/validateEmail")
				.param("email", "asdfg")
				.principal(getTestPrincipal("ese@unibe.ch")))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", "This user does not exist.").isString());
	}
	
	@Test
	public void sendMessage() throws Exception {
		mockMvc.perform(post("/profile/messages/sendMessage")
				.param("subject", "Subject")
				.param("text", "Text")
				.param("recipientEmail", "ese@unibe.ch")
				.principal(getTestPrincipal("jane@doe.com")))
		.andExpect(status().isOk());
	}
}
