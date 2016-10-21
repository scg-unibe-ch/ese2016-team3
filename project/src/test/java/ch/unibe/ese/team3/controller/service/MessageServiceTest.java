package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
	MessageService messageService;
	
	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy", timezone = "CET" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateSent;

	@Column(nullable = false)
	private MessageState state;
	
	@Test
	public void sendMessage () {
		
		User recipient = userDao.findByUsername("eric@clapton.com");
		User sender = userDao.findByUsername("jane@doe.com");
				
		String subject = "Your sweet home Alabama";
		String text = "where the skies are so blue";
		
		messageService.sendMessage(sender, recipient, subject, text);
		
		List<Message> messages = new ArrayList<Message>();
		
		for (Message receivedMessage: messageService.getInboxForUser(recipient)){
			messages.add(receivedMessage);
		}
		
		assertEquals(1, messages.size());
		
		Message receivedMessage = messages.get(0);
		
		assertEquals(recipient, receivedMessage.getRecipient());
		assertEquals(sender, receivedMessage.getSender());
		assertEquals(subject, receivedMessage.getSubject());
		assertEquals(text, receivedMessage.getText());
		assertEquals(MessageState.READ, receivedMessage.getState());
		
		messageDao.delete(receivedMessage);
		
	}
}
