package ch.unibe.ese.team3.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.controller.service.MessageService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.dto.MessageMeta;
import ch.unibe.ese.team3.exceptions.ForbiddenException;
import ch.unibe.ese.team3.exceptions.InvalidUserException;
import ch.unibe.ese.team3.exceptions.ResourceNotFoundException;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.User;

/** This controller handles all requests concerning messaging. */
@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	/**
	 * Shows the messages page for the currently logged in user. The inbox of
	 * the user is shown.
	 */
	@RequestMapping(value = "/profile/messages", method = RequestMethod.GET)
	public ModelAndView messages(Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}
		
		ModelAndView model = new ModelAndView("messages");
		User user = userService.findUserByUsername(principal.getName());
		model.addObject("messageForm", new MessageForm());
		model.addObject("messages", convertToMessageMeta(messageService.getInboxForUser(user)));
		return model;
	}

	/**
	 * Gets all messages in the inbox for the currently logged in user.
	 */
	@RequestMapping(value = "/profile/message/inbox", method = RequestMethod.POST)
	public @ResponseBody List<MessageMeta> getInbox(Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}
		
		User user = userService.findUserByUsername(principal.getName());
		return convertToMessageMeta(messageService.getInboxForUser(user));
	}

	/** Gets all messages in the sent folder for the currently logged in user. */
	@RequestMapping(value = "/profile/message/sent", method = RequestMethod.POST)
	public @ResponseBody List<MessageMeta> getSent(Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}
		
		User user = userService.findUserByUsername(principal.getName());
		return convertToMessageMeta(messageService.getSentForUser(user));
	}

	/** Gets the message with the given id */
	@RequestMapping(value = "/profile/messages/getMessage", method = RequestMethod.GET)
	public @ResponseBody MessageMeta getMessage(@RequestParam Long id, Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}
		
		User user = userService.findUserByUsername(principal.getName());
		
		Message message = messageService.getMessage(id);
		
		if (message == null){
			throw new ResourceNotFoundException();
		}
		
		if (!user.equals(message.getSender()) && !user.equals(message.getRecipient())){
			throw new ForbiddenException();
		}
		
		return convertToMessageMeta(message);
	}

	/**
	 * Shows the messages page and validates and/or saves the message passed a
	 * post data.
	 */
	@RequestMapping(value = "/profile/messages", method = RequestMethod.POST)
	public ModelAndView messageSent(@Valid MessageForm messageForm,
			BindingResult bindingResult, Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}		
		
		User sender = userService.findUserByUsername(principal.getName());
		
		ModelAndView model = new ModelAndView("messages");
		if (!bindingResult.hasErrors()) {
			try {
				messageService.saveFrom(messageForm, sender);
				model.addObject("messageForm", new MessageForm());
			}
			catch (InvalidUserException ex){
				model.addObject("errorMessage", "Could not send message. The recipient is invalid");
				model.addObject("messageForm", messageForm);
			}
			User user = userService.findUserByUsername(principal.getName());
			model.addObject("messages", messageService.getInboxForUser(user));
		}
		return model;
	}
	
	/**
	 * Sets the MessageState of a given Message to "READ".
	 */
	@RequestMapping(value="/profile/readMessage", method = RequestMethod.POST)
	public @ResponseBody void readMessage(@RequestParam("id") long id, Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}
		
		User user = userService.findUserByUsername(principal.getName());
		
		Message message = messageService.getMessage(id);
		
		if (message == null){
			throw new ResourceNotFoundException();
		}
		
		if (!user.equals(message.getRecipient())){
			throw new ForbiddenException();
		}
		
		messageService.readMessage(message);
	}
	
	/**
	 * Returns the number of unread messages of the logged in user.
	 */
	@RequestMapping(value="/profile/unread", method = RequestMethod.GET)
	public @ResponseBody int unread(Principal principal) {
		if (principal == null){
			throw new ForbiddenException();
		}
		
		long id = userService.findUserByUsername(principal.getName()).getId();
		return messageService.unread(id);
	}

	/**
	 * Checks if the email passed as post parameter is a valid email. In case it
	 * is valid, the email address is returned. If it is not, a error message is
	 * returned.
	 */
	@RequestMapping(value = "/profile/messages/validateEmail", method = RequestMethod.POST)
	@ResponseBody
	public String validateEmail(@RequestParam String email) {
		User user = userService.findUserByUsername(email);
		if (user == null) {
			return "This user does not exist.";
		} else {
			return user.getEmail();
		}
	}

	/** Sends a message with the passed parameters 
	 * 
	 */
	@RequestMapping(value = "/profile/messages/sendMessage", method = RequestMethod.POST)
	public @ResponseBody void sendMessage(@RequestParam String subject,
			@RequestParam String text, @RequestParam String recipientEmail,
			Principal principal) throws InvalidUserException {
		User recipient = userService.findUserByUsername(recipientEmail);
		
		if (recipient == null){
			throw new InvalidUserException();
		}
		
		User sender = userService.findUserByUsername(principal.getName());
		messageService.sendMessage(sender, recipient, subject, text);
	}
	

	private List<MessageMeta> convertToMessageMeta(Iterable<Message> inboxForUser) {
		ArrayList<MessageMeta> messages = new ArrayList<MessageMeta>();
		inboxForUser.forEach(message -> messages.add(convertToMessageMeta(message)));
		return messages;
	}

	private MessageMeta convertToMessageMeta(Message message) {
		MessageMeta meta = new MessageMeta();
		meta.setId(message.getId());
		meta.setRecipient(message.getRecipient().getEmail());
		meta.setSender(message.getSender().getEmail());
		meta.setSubject(message.getSubject());
		meta.setText(message.getText());
		meta.setDateSent(message.getDateSent());
		
		return meta;
	}

}
