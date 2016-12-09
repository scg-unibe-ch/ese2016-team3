package ch.unibe.ese.team3.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team3.model.User;

/**
 * Helper class for sending email.
 *
 */
public class MailSender implements IMailSender {
	
	@Autowired
	private ConfigReader configReader;
	
	/** 
	 * @see ch.unibe.ese.team3.util.IMailSender#sendEmail(ch.unibe.ese.team3.model.User, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmail(User recipient, String subject, String text) {
		String mailHost = configReader.getConfigValue("mailhost");
		String mailAccount = configReader.getConfigValue("mailaccount");
		String mailPassword = configReader.getConfigValue("mailpassword");
		String mailDebug = configReader.getConfigValue("maildebug");
		String mailAuth = configReader.getConfigValue("mailauth");
		String mailPort = configReader.getConfigValue("mailport");
		String mailEnableTls = configReader.getConfigValue("mailenabletls");

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", mailHost);
		properties.setProperty("mail.smtp.user", mailAccount);
		properties.setProperty("mail.smtp.debug", mailDebug);
		properties.setProperty("mail.smtp.auth", mailAuth);
		properties.setProperty("mail.smtp.port", mailPort);
		properties.setProperty("mail.smtp.starttls.enable", mailEnableTls);

		Session session;
		
		if (mailAuth.equals("true")) {

			session = Session.getInstance(properties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAccount, mailPassword);
				}
			});
		}
		else {
			session = Session.getDefaultInstance(properties);
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ithacaserver@gmail.com"));
			message.addRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipient.getEmail(), false));
			message.setSubject(subject);
			message.setContent(text, "text/html; charset=utf-8");

			Transport.send(message);
			System.out.println("Message sent successfully for " + recipient.getUsername());
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
}
