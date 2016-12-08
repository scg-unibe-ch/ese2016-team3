package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.exceptions.InvalidUserException;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.MessageState;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.util.ListUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
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
	
	User sender;
	User recipient;

	@Before
	public void setUp() throws ParseException{		
		sender = userDao.findByUsername("mark@knopfler.com");
		recipient = userDao.findByUsername("Kim@kardashian.com");		
	}	
	
	@Test
	public void sendNewMessage(){
		
		String subject = "Your sweet home Alabama";
		String text = "where the skies are so blue";
		assertEquals(0, messageDao.findByRecipient(recipient).spliterator().getExactSizeIfKnown());
		messageService.sendMessage(sender, recipient, subject, text);	
		assertEquals(1, messageDao.findByRecipient(recipient).spliterator().getExactSizeIfKnown());
		
				
	}
	
	@Test
	public void getInboxWithTwoMessagesSent() throws InterruptedException{
		messageService.sendMessage(sender, recipient, "Message1", "Text Message1");
		Thread.sleep(1000); //To ensure message 1 arrives before message 2
		messageService.sendMessage(sender, recipient, "Message2", "Text Message2");
		
		ArrayList<Message> messageList = new ArrayList<Message>();
		Iterable<Message> messages = messageService.getInboxForUser(recipient);
		for(Message inboxMessage: messages)
			messageList.add(inboxMessage);
		
		assertEquals(2, messageList.size());
		
		Message receivedMessage1 = messageList.get(0);
		
		assertEquals(recipient, receivedMessage1.getRecipient());
		assertEquals(sender, receivedMessage1.getSender());
		assertEquals("Message2", receivedMessage1.getSubject());
		assertEquals("Text Message2", receivedMessage1.getText());
	}
	
	@Test
	public void getInboxWithTwoMessagesSentReadState() throws InterruptedException{
		messageService.sendMessage(sender, recipient, "Message1", "Text Message1");
		Thread.sleep(1000); //To ensure message 1 arrives before message 2
		messageService.sendMessage(sender, recipient, "Message2", "Text Message2");
		
		ArrayList<Message> messageList = new ArrayList<Message>();
		Iterable<Message> messages = messageService.getInboxForUser(recipient);
		for(Message inboxMessage: messages)
			messageList.add(inboxMessage);
		
		assertEquals(2, messageList.size());
		
		Message receivedMessage1 = messageList.get(0);
		Message receivedMessage2 = messageList.get(1);
		
		assertEquals("Most recent message should be read", MessageState.READ, receivedMessage1.getState());
		assertEquals("Older message should be unread", MessageState.UNREAD, receivedMessage2.getState());
	}
	
	@Test
	public void noUnreadMessage(){
		
		assertEquals(0, messageService.unread(recipient.getId()));	
	}
	
	@Test
	public void singleUnreadMessage(){
		messageService.sendMessage(sender, recipient, "Test singleUnreadMessage", "singleUnreadMessage");
		assertEquals(1, messageService.unread(recipient.getId()));
	}
	
	@Test
	public void readMessage(){
		messageService.sendMessage(sender, recipient, "Message1", "Text Message1");
		messageService.sendMessage(sender, recipient, "Message2", "Text Message2");
		
		ArrayList<Message> messageList = new ArrayList<Message>();
		Iterable<Message> messages = messageService.getInboxForUser(recipient);
		for(Message inboxMessage: messages)
			messageList.add(inboxMessage);
		
		assertEquals(2, messageList.size());
		
		Message receivedMessage = messageList.get(1);
		assertEquals(MessageState.UNREAD, receivedMessage.getState());
		
		messageService.readMessage(receivedMessage);
		Message message = messageService.getMessage(receivedMessage.getId());
		assertEquals(MessageState.READ, message.getState());
	}
	
	@Test
	public void getInboxNoMessages(){
		Iterable<Message> inbox = messageService.getInboxForUser(recipient);
		assertEquals(0, ListUtils.countIterable(inbox));
	}
	
	@Test
	public void getSentNoMessage(){
		Iterable<Message> sent = messageService.getSentForUser(sender);
		assertEquals(0, ListUtils.countIterable(sent));
	}
	
	@Test
	public void getSentSingleMessage(){
		messageService.sendMessage(sender, recipient, "Test", "Test");
		
		Iterable<Message> sent = messageService.getSentForUser(sender);
		assertEquals(1, ListUtils.countIterable(sent));
	}
	
	@Test
	public void getSentMultipleMessages(){
		messageService.sendMessage(sender, recipient, "Test1", "Test1");
		messageService.sendMessage(sender, recipient, "Test2", "Test2");
		
		Iterable<Message> sent = messageService.getSentForUser(sender);
		assertEquals(2, ListUtils.countIterable(sent));
	}
	
	@Test 
	public void saveFromMessageFormTest() throws InvalidUserException{		
		MessageForm messageForm = new MessageForm();
		messageForm.setRecipient("kim@kardashian.com");
		messageForm.setSubject("Buy");
		messageForm.setText("I really really wanna");
		messageService.saveFrom(messageForm, sender);
		
		assertEquals(1, messageService.unread(recipient.getId()));
	}
	
	@Test(expected=InvalidUserException.class)
	public void saveFromMessageFormInvalidRecipient() throws InvalidUserException{
		MessageForm messageForm = new MessageForm();
		messageForm.setRecipient("thisisnovaliduser");
		messageForm.setSubject("Buy");
		messageForm.setText("I really really wanna");
		messageService.saveFrom(messageForm, sender);
	}
	
	@Test
	public void unreadMultipleReadMessages(){
		for (int i = 0; i < 10; i++){
			messageService.sendMessage(sender, recipient, "Subject " + i, "Text " + i);
		}
		
		assertEquals(10, messageService.unread(recipient.getId()));
		messageService.getInboxForUser(recipient);
		assertEquals(9, messageService.unread(recipient.getId()));
	}
	
	@Test
	public void sendPremiumExpiryMessageTest(){
		//Manual test done by checking "ithacatest@trash-mail.com"
		//Commented out because sending emails does not work in the test.
		//However, message was received when sendEmail was commented out.
		//messageService.sendPremiumExpiryMessage();
		//User messageTest = userDao.findByUsername("ithacatest@trash-mail.com");
		//assertEquals(1, messageService.unread(messageTest.getId()));
	}
	
}
