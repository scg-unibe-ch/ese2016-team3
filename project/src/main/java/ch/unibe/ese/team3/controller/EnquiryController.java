package ch.unibe.ese.team3.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.controller.service.EnquiryService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.controller.service.VisitService;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.VisitEnquiry;

/**
 * Handles all requests concerning enquiries of type
 * {@link ch.unibe.ese.team3.model.VisitEnquiry VisitEnquiry} between users.
 */
@Controller
public class EnquiryController {

	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private UserService userService;

	@Autowired
	private VisitService visitService;

	/** Serves the page that displays the enquiries for the logged in user. */
	@RequestMapping(value = "/profile/enquiries")
	public ModelAndView enquiriesPage(Principal principal) {
		ModelAndView model = new ModelAndView("enquiries");
		User user = userService.findUserByUsername(principal.getName());
		Iterable<VisitEnquiry> usersEnquiries = enquiryService
				.getEnquiriesByRecipient(user);
		model.addObject("enquiries", usersEnquiries);
		return model;
	}

	/**
	 * Sends an enquiry for the visit with the given id. The sender of the
	 * enquiry will be the currently logged in user.
	 */
	@RequestMapping(value = "/profile/enquiries/sendEnquiryForVisit")
	public @ResponseBody void sendEnquiryForVisit(@RequestParam("id") long id,
			Principal principal) {
		Visit visit = visitService.getVisitById(id);
		User user = userService.findUserByUsername(principal.getName());

		enquiryService.createEnquiry(visit, user);
	}

	/** Sets the state of the enquiry with the given id to accepted. */
	@RequestMapping(value = "/profile/enquiries/acceptEnquiry", method = RequestMethod.GET)
	public @ResponseBody void acceptEnquiry(@RequestParam("id") long id) {
		enquiryService.acceptEnquiry(id);
	}

	/** Sets the state of the enquiry with the given id to declined. */
	@RequestMapping(value = "/profile/enquiries/declineEnquiry", method = RequestMethod.GET)
	public @ResponseBody void declineEnquiry(@RequestParam("id") long id) {
		enquiryService.declineEnquiry(id);
	}

	/**
	 * Reopens the enquiry with the given id, meaning that its state is set to
	 * open again.
	 */
	@RequestMapping(value = "/profile/enquiries/reopenEnquiry", method = RequestMethod.GET)
	public @ResponseBody void reopenEnquiry(@RequestParam("id") long id) {
		enquiryService.reopenEnquiry(id);
	}

}
