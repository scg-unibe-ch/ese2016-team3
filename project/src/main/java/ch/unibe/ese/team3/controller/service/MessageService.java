package ch.unibe.ese.team3.controller.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.exceptions.InvalidUserException;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AlertResult;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.MessageState;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AlertResultDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.util.ConfigReader;


/** Handles all persistence operations concerning messaging. */
@Service
@Configuration
public class MessageService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private AlertResultDao alertResultDao;

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
	 * @throws InvalidUserException 
	 */
	@Transactional
	public void saveFrom(MessageForm messageForm) throws InvalidUserException {
		User recipient = userDao.findByUsername(messageForm.getRecipient());
		
		if (recipient == null){
			throw new InvalidUserException();
		}
		
		String subject = messageForm.getSubject();
		String text = messageForm.getText();
		
		// get logged in user as the sender
		org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		User sender = userDao.findByUsername(securityUser.getUsername());
		sendMessage(sender, recipient, subject, text);
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
	
	/** Sends an e-mail to a certain user
	 * 
	 * @param recipient the user who will receive the e-mail
	 * @param subject the subject of the message
	 * @param text the text of the message
	 */
	public void sendEmail(User recipient, String subject, String text) {
		String mailAccount = ConfigReader.getInstance().getConfigValue("mailaccount");
		String mailPassword = ConfigReader.getInstance().getConfigValue("mailpassword");
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.user", mailAccount);
		properties.setProperty("mail.smtp.debug", "true");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.starttls.enable", "true");

		
		Session session = Session.getInstance(properties, new Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailAccount, mailPassword);
		}
		});
		
		/*properties.put("mail.smtp.auth", "false");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "localhost");
		properties.put("mail.smtp.port", "2525");
		
		Session session = Session.getDefaultInstance(properties);*/
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ithacaserver@gmail.com"));
			message.addRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipient.getEmail(), false));
			message.setSubject(subject);
			message.setText(text);
			
			Transport.send(message);
			System.out.println("Message sent successfully for " + recipient.getUsername());
		}catch (MessagingException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/** Sends the daily alert message for the basic users
	 * it is scheduled to midnight of every day and contains exactly
	 * the alerts that have been triggered for every user.
	 * A message and an e-mail are sent.
	 */
	@Scheduled(cron = "0 59 23 * * *") // everyday at one minute before midnight
	//@Scheduled(cron = "1 * * * * *") // every minute
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
				for (AlertResult alertResult : alertResultDao.findByUser(user)) {
					if(alertResult.getTriggerDate().after(yesterday) || alertResult.getNotified() == false) {
						Ad ad = alertResult.getTriggerAd();
						text += "</a><br><br> <a class=\"link\" href=/ad?id="
								+ ad.getId() + ">" + ad.getTitle() + "</a><br><br>"
								+ ad.getRoomDescription() + "\n";
						alertResult.setNotified(true);
					}
					
		
				}			
				if (text.equals("All ads that match your alerts: \n")) {
						text = "There are no new Ads that match your alerts, but why"
								+ " not have a look at the highlights on our page anyway?";
				}
				sendMessage(userDao.findByUsername("System"), user, subject, text);
				sendEmail(user, subject, text);
				
				text = "All ads that match your alerts: \n";
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
	public int unread(long userId) {
		User user = userDao.findOne(userId);
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		int i = 0;
		for(Message message: usersMessages) {
			if(message.getState().equals(MessageState.UNREAD))
				i++;
		}
		return i;
	}

}
