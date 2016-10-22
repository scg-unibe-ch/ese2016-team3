package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.persistence.Column;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
	UserDao userDao;
	
	@Autowired
	MessageDao messageDao;
	
	@Autowired
	MessageService messageService;
	
	DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

	@Column(nullable = false)
	private MessageState state;
	
	Message message = new Message();
	Message message2 = new Message();
	User sender;
	User recipient;

	
	@Before
	public void setUp() throws ParseException{		
		sender = userDao.findByUsername("mark@knopfler.com");
		recipient = userDao.findByUsername("Kim@kardashian.com");
		message.setSender(sender);
		message.setState(MessageState.UNREAD);
		message.setSubject("Your sweet home Alabama");
		message.setText("where the skies are so blue");
		messageDao.save(message);
		
		message2.setSender(sender);
		message2.setRecipient(recipient);
		message2.setState(MessageState.UNREAD);
		message2.setSubject("Kentucky in yer heart");
		message2.setText("okeli dokeli");
		message2.setDateSent(dateFormat.parse("12:02 24.02.2014"));
		messageDao.save(message2);
		
	}	
		
	@Test
	public void sendNewMessage(){
		
		String subject = "Your sweet home Alabama";
		String text = "where the skies are so blue";
		assertEquals(1, messageDao.findByRecipient(recipient).spliterator().getExactSizeIfKnown());
		messageService.sendMessage(sender, recipient, subject, text);	
		assertEquals(2, messageDao.findByRecipient(recipient).spliterator().getExactSizeIfKnown());
		
		ArrayList<Message> messageList = new ArrayList();
		Iterable<Message> messages = messageService.getInboxForUser(recipient);
		for(Message inboxMessage: messages)
			messageList.add(inboxMessage);
		
		assertEquals(2, messageList.size());
		
		Message receivedMessage = messageList.get(0);
		
		assertEquals(recipient, receivedMessage.getRecipient());
		assertEquals(sender, receivedMessage.getSender());
		assertEquals(subject, receivedMessage.getSubject());
		assertEquals(text, receivedMessage.getText());
		assertEquals(MessageState.READ, receivedMessage.getState());
			
		assertEquals(message.getSubject(), messageList.get(0).getSubject());
		assertEquals(message2.getSubject(), messageList.get(1).getSubject());
		
		messageDao.delete(receivedMessage);
	}
	
	@Test
	public void readMessage(){
		
		assertEquals(1, messageService.unread(recipient.getId()));
		messageService.readMessage(message2.getId());
		assertEquals(MessageState.READ, message2.getState());
		assertEquals(0, messageService.unread(recipient.getId()));	
	}
	
	/*@Test 
	public void saveFromMessageFormTest(){
		
		MessageForm messageForm = new MessageForm();
		messageForm.setRecipient("kim@kardashian.com");
		messageForm.setSubject("Buy");
		messageForm.setText("I really really wanna");
		messageService.saveFrom(messageForm);
	}*/
	
}
