package ch.unibe.ese.team3.util;

import ch.unibe.ese.team3.model.User;

public interface IMailSender {

	/**
	 * Sends an e-mail to a certain user
	 * 
	 * @param recipient
	 *            the user who will receive the e-mail
	 * @param subject
	 *            the subject of the message
	 * @param text
	 *            the text of the message
	 */
	void sendEmail(User recipient, String subject, String text);

}