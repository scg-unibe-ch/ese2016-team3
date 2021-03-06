package ch.unibe.ese.team3.controller.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.exceptions.InvalidUserException;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AlertResult;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.MessageState;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.AlertResultDao;
import ch.unibe.ese.team3.model.dao.BidDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;
import ch.unibe.ese.team3.util.IMailSender;

/** Handles all persistence operations concerning messaging. */
@Service
@Configuration
@DependsOn("mailSender")
public class MessageService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private AlertResultDao alertResultDao;
	
	@Autowired
	private IMailSender mailSender;

	@Autowired
	private AdDao adDao;

	@Autowired
	private BidDao bidDao;

	/**
	 * Gets all messages in the inbox of the given user, sorted newest to oldest
	 */
	@Transactional
	public Iterable<Message> getInboxForUser(User user) {
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		List<Message> messages = new ArrayList<Message>();
		for (Message message : usersMessages)
			messages.add(message);
		Collections.sort(messages, new Comparator<Message>() {
			@Override
			public int compare(Message message1, Message message2) {
				return message2.getDateSent().compareTo(message1.getDateSent());
			}
		});
		if (!messages.isEmpty()) {
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
	public void saveFrom(MessageForm messageForm, User sender) throws InvalidUserException {
		User recipient = userDao.findByUsername(messageForm.getRecipient());

		if (recipient == null) {
			throw new InvalidUserException();
		}

		String subject = messageForm.getSubject();
		String text = messageForm.getText();

		sendMessage(sender, recipient, subject, text);
	}

	/**
	 * Saves a new message with the given parameters in the DB.
	 * 
	 * @param sender
	 *            the user who sends the message
	 * @param recipient
	 *            the user who should receive the message
	 * @param subject
	 *            the subject of the message
	 * @param text
	 *            the text of the message
	 */
	public void sendMessage(User sender, User recipient, String subject, String text) {
		Message message = new Message();
		message.setDateSent(new Date());
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setSubject(subject);
		message.setText(text);
		message.setState(MessageState.UNREAD);

		messageDao.save(message);
	}

	/**
	 * Sends the daily alert message for the basic users it is scheduled to
	 * midnight of every day and contains exactly the alerts that have been
	 * triggered for every user. A message and an e-mail are sent. The email is also
	 * sent if no alert triggers.
	 */
	@Scheduled(cron = "0 59 23 * * *") // everyday at one minute before midnight 
	// @Scheduled(cron = "1 * * * * *") // every minute
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

		for (User user : users) {
			if (!user.isPremium()) {
				Iterable<AlertResult> alertResults = alertResultDao.findByUser(user);
				for (AlertResult alertResult : alertResults) {
					if (alertResult.getTriggerDate().after(yesterday) && alertResult.getNotified() == false) {
						Ad ad = alertResult.getTriggerAd();
						text += "</a><br><br> <a href=\"http://localhost:8080/buy/ad?id=" + ad.getId() + "\">" + ad.getTitle()
								+ "</a><br><br>" + ad.getRoomDescription() + "\n";
						alertResult.setNotified(true);
					}
				}
				
				if (text.equals("All ads that match your alerts: \n")) {
					text = "There are no new Ads that match your alerts, but why"
							+ " not have a look at the highlights on our page anyway?";
				}
				sendMessage(userDao.findByUsername("System"), user, subject, text);
				mailSender.sendEmail(user, subject, text);

				text = "All ads that match your alerts: \n";
			}
		}
	}
	
	/**
	 * Sends a message to all premium users at 1AM, one day before their premium membership expires,
	 * warning them about it.
	 */
	// Every minute for testing purposes @Scheduled(cron = "1 * * * * *")
	@Scheduled(cron = "0 0 1 * * *")
	public void sendPremiumExpiryMessage(){
		Iterable <User> users = userDao.findAll();
		Date now = new Date();
		Date tomorrow = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		tomorrow = calendar.getTime();
		
		for (User user : users){
			if(user.isPremium()){
				try{
					if(tomorrow.after(user.getPremiumExpiryDate())){
						String subject = "Your Ithaca premium membership will expire tomorrow!";
						String text = "Hi there, " + user.getFirstName()
								+ "<br> <br> It seems as if your premium membership will expire tomorrow! <br>"
								+ "<br> We hope you've enjoyed the benefits, if you still want to be a premium member, "
								+ "please upgrade again after it expires.";
						mailSender.sendEmail(user, subject, text);
						sendMessage(userDao.findByUsername("System"), user, subject, text);
					}
				}
				catch(Exception e){
					
				}
				
			}
		}
	}

	/**
	 * Searches for expired auctions and sends messages to the most higher bidder 
	 * and owner of these expired auctions at midnight.
	 * If no one has bid for the expired auction, the owner will be notified
	 */
	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // everyday on midnight
	public void sendMessageForExpiredAuctions() {

		Iterable<Ad> expiredAds = adDao.findByEndDateLessThanAndAuctionMessageSent(new Date(), false);

		for (Ad ad : expiredAds) {
			ad.setAuctionMessageSent(true);
			adDao.save(ad);

			if (ad.getBids().isEmpty()) {
				sendNoBidsMessageToOwner(ad);
			} else {
				sendSuccessMessages(ad);
			}
		}
	}

	/**
	 * Sends message to user who won the auction and the owner of the property
	 * 
	 * @param ad
	 *            Ad which auction has finished
	 */
	private void sendSuccessMessages(Ad ad) {
		Bid highestBid = bidDao.findTopByAdOrderByAmountDesc(ad);

		User winner = highestBid.getBidder();
		User owner = ad.getUser();

		String subject1 = "You won an auction";
		String text1 = "Dear " + winner.getFirstName() + ",</br></br>" + "Congratulations! You won the auction on the "
				+ "<a href=\"http://localhost:8080/buy/ad?id=" + ad.getId() + "\">" + ad.getTitle() + "</a>.</br>"
				+ owner.getFirstName() + " " + owner.getLastName() + " will contact you with further details.</br>";

		sendMessage(userDao.findByUsername("System"), winner, subject1, text1);
		mailSender.sendEmail(winner, subject1, text1);

		String subject2 = "Your auction was successfully";
		String text2 = "Dear " + owner.getFirstName() + ",</br></br>" + "You just sold the "
				+ "<a href=\"http://localhost:8080/buy/ad?id=" + ad.getId() + "\">" + ad.getTitle() + "</a></br>" + "to "
				+ winner.getFirstName() + " " + winner.getLastName() + " " + winner.getEmail() + " for "
				+ highestBid.getAmount() + " CHF.</br>" + "Please contact the winner as soon as possible.";

		sendMessage(userDao.findByUsername("System"), owner, subject2, text2);
		mailSender.sendEmail(owner, subject2, text2);

	}

	/**
	 * Sends message to user who placed the ad
	 * 
	 * @param ad
	 *            Ad which auction has finished
	 */
	private void sendNoBidsMessageToOwner(Ad ad) {

		User user = userDao.findUserById(ad.getUser().getId());
		String subject = "Auction expired";
		String text = "Dear " + user.getFirstName() + ",</br></br>"
				+ "Unfortunately your auction has expired and no one has placed a bid on your "
				+ "<a href=\"http://localhost:8080/buy/ad?id=" + ad.getId() + "\">" + ad.getTitle() + "</a>.</br>";

		sendMessage(userDao.findByUsername("System"), user, subject, text);
		mailSender.sendEmail(user, subject, text);
	}

	/**
	 * Sets the MessageState of a given Message to "READ".
	 * 
	 * @param id
	 */
	@Transactional
	public void readMessage(Message message) {
		message.setState(MessageState.READ);
		messageDao.save(message);
	}

	/** Returns the number of unread messages a user has. */
	@Transactional
	public int unread(long userId) {
		User user = userDao.findOne(userId);
		Iterable<Message> usersMessages = messageDao.findByRecipient(user);
		int i = 0;
		for (Message message : usersMessages) {
			if (message.getState().equals(MessageState.UNREAD))
				i++;
		}
		return i;
	}

	/**
	 * Sends Message to Premium User who has been over bidden
	 * 
	 * @param ad  Ad to bid on
	 * @param bidder User who overbids the last one
	 */
	public void sendOverbiddenMessage(Ad ad, User bidder) {
		Bid maxbid = bidDao.findTopByAdOrderByAmountDesc(ad);

		if (maxbid != null) {
			User receiver = maxbid.getBidder();
			if (receiver.isPremium()) {
				String subject = "Overbid";
				String text = "Dear " + receiver.getFirstName() + ",</br></br>" + "You have been overbidden by "
						+ bidder.getFirstName() + " " + bidder.getLastName() + " on "
						+ "<a href=\"http://localhost:8080/buy/ad?id=" + ad.getId() + "\">" + ad.getTitle() + "</a>.</br>";

				sendMessage(userDao.findByUsername("System"), receiver, subject, text);
				mailSender.sendEmail(receiver, subject, text);
			}
		}

	}

}
