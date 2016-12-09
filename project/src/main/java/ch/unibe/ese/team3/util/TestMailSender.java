package ch.unibe.ese.team3.util;

import ch.unibe.ese.team3.model.User;

/**
 * Test IMailSender interface implementation which
 * doesn't do anything.
 *
 */
public class TestMailSender implements IMailSender {

	@Override
	public void sendEmail(User recipient, String subject, String text) {
		
	}

}
