package ch.unibe.ese.team3.controller.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.MessageState;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.AlertDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;


/** Handles all persistence operations concerning messaging. */
@Service
@Configuration
public class MessageService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private MessageDao messageDao;
	
	@Autowired 
	private AdDao adDao;
	
	@Autowired
	private AlertDao alertDao;

	/** Gets all messages in the inbox of the given user, sorted newest to oldest */
	@Transactional
	public Iterable<Message> getInboxForUser(User user) {
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		List<Message> messages = new ArrayList<Message>();
		for(Message message: usersMessages)
			messages.add(message);
		Collections.sort(messages, new Comparator<Message>(){
			@Override
			public int compare(Message message1, Message message2) {
				return message2.getDateSent().compareTo(message1.getDateSent());
			}
		});
		if (!messages.isEmpty()){
			Message firstMessage = messages.get(0);
			firstMessage.setState(MessageState.READ);
			messageDao.save(firstMessage);
		}
		return messages;
	}

	/** Gets all messages in the sent folder for the given user. */
	@Transactional
	public Iterable<Message> getSentForUser(User user) {
		return messageDao.findBySender(user);
	}

	/** Gets the message with the given id. */
	@Transactional
	public Message getMessage(long id) {
		return messageDao.findOne(id);
	}

	/**
	 * Handles persisting a new message to the database.
	 * 
	 * @param messageForm
	 *            the form to take the data from
	 */
	@Transactional
	public Message saveFrom(MessageForm messageForm) {
		Message message = new Message();

		message.setRecipient(userDao.findByUsername(messageForm.getRecipient()));
		message.setSubject(messageForm.getSubject());
		message.setText(messageForm.getText());
		message.setState(MessageState.UNREAD);
		
		// get logged in user as the sender
		org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		User loggedInUser = userDao.findByUsername(securityUser.getUsername());
		
		message.setSender(loggedInUser);

		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		calendar.setTimeInMillis(System.currentTimeMillis());
		message.setDateSent(calendar.getTime());

		messageDao.save(message);

		return message;
	}

	/** Saves a new message with the given parameters in the DB.
	 * 
	 * @param sender the user who sends the message
	 * @param recipient the user who should receive the message
	 * @param subject the subject of the message
	 * @param text the text of the message
	 */
	public void sendMessage(User sender, User recipient, String subject,
			String text) {
		Message message = new Message();
		message.setDateSent(new Date());
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setSubject(subject);
		message.setText(text);
		message.setState(MessageState.UNREAD);
		
		messageDao.save(message);
	}
	
	public void sendEmail(User recipient, String subject, String text) {
		
		Properties properties = System.getProperties();
		/*properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.user", "ithacaserver@gmail.com");
		properties.setProperty("mail.smtp.debug", "true");
		properties.setProperty("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.starttls.enable", "true");

		
		Session session = Session.getInstance(properties, new Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("ithacaserver@gmail.com", "flatfindr");
		}
		});*/
		
		properties.put("mail.smtp.auth", "false");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "localhost");
		properties.put("mail.smtp.port", "2525");
		
		Session session = Session.getDefaultInstance(properties);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ithacaserver@gmail.com"));
			message.addRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipient.getEmail(), false));
			message.setSubject(subject);
			message.setText(text);
			
			Transport.send(message);
			System.out.println("Message sent successfully");
		}catch (MessagingException e)
		{
			e.printStackTrace();
		}
		
	}
	
	//@Scheduled(cron = "0 0 17 1/1 * *") // everyday at 5 pm
	@Scheduled(cron = "0,30 * * * * *") // every 30 seconds
	public void alertMessageForBasicUser() {
		
		String subject = "Your daily alerts";
		String text = "All ads that match your alerts: \n";
		
		Date now = new Date();
		Date yesterday = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		yesterday = calendar.getTime();
		
		Iterable<User> users = userDao.findAll();
		
		for (User user: users) {
			if (!user.isPremium()){
				for (Ad ad: adDao.findAll()) {
					if(ad.getCreationDate().after(yesterday)) {
						//if(alertDao.findByUser(user))
						text += "</a><br><br> <a class=\"link\" href=/ad?id="
								+ ad.getId() + ">" + ad.getTitle() + "</a><br><br>"
								+ ad.getRoomDescription() + "\n";
					}
					
		
				}			
				if (text.equals("All ads that match your alerts: \n")) {
						text = "There are no new Ads that match your alerts, but"
								+ " have a look at our highlights on our page.";
				}
				sendMessage(userDao.findByUsername("System"), user, subject, text);
				sendEmail(user, subject, text);
			}
		}
	}
	

	/**
	 * Sets the MessageState of a given Message to "READ".
	 * @param id
	 */
	@Transactional
	public void readMessage(long id) {
		Message message = messageDao.findOne(id);
		message.setState(MessageState.READ);
		messageDao.save(message);
	}
	
	/** Returns the number of unread messages a user has. */
	@Transactional
	public int unread(long id) {
		User user = userDao.findOne(id);
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		int i = 0;
		for(Message message: usersMessages) {
			if(message.getState().equals(MessageState.UNREAD))
				i++;
		}
		return i;
	}

}
