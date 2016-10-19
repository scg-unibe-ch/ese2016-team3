package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.annotation.JsonFormat;

import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.MessageState;
import ch.unibe.ese.team3.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class MessageServiceTest {

	@Autowired
	AdDao adDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	MessageDao messageDao;
	
	@Autowired
	User recipient;
	
	@Autowired
	User sender;
	
	@Autowired
	Message message;
	
	@Autowired
	MessageService messageService;
	
	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy", timezone = "CET" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateSent;

	@Column(nullable = false)
	private MessageState state;
	
	@Test
	public void sendMessage () {
		
		Date date = new Date(1993, 3, 20, 6, 20);
		message.setId(1);
		message.setRecipient(recipient);
		message.setSender(sender);
		message.setState(MessageState.UNREAD);
		message.setDateSent(date);
		message.setSubject("Your sweet home Alabama");
		message.setText("where the skies are so blue");
		
		Iterable<Message> recipientList = messageService.getInboxForUser(recipient);
	
		messageService.readMessage(1);
		assertTrue(message.getState() == MessageState.READ);
		
		messageService.sendMessage(sender, recipient, "Your sweet home Alabama", "where the skies are so blue");
		
		
	}
}
