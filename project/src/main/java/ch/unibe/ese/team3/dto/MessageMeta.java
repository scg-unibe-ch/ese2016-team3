package ch.unibe.ese.team3.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import ch.unibe.ese.team3.model.MessageState;


public class MessageMeta {
	private long id;

	private MessageState state;

	private String subject;

	private String text;

	@JsonFormat(pattern = "HH:mm, dd.MM.yyyy", timezone = "CET" )
	private Date dateSent;

	private String sender;

	private String recipient;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MessageState getState() {
		return state;
	}

	public void setState(MessageState state) {
		this.state = state;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getTextWithLineBreaks(){
		return this.text.replaceAll("\\r\\n?|\\n", "<br/>");
	}
}
