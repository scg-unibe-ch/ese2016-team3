package ch.unibe.ese.team3.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.controller.service.AuctionService;
import ch.unibe.ese.team3.controller.service.BookmarkService;
import ch.unibe.ese.team3.controller.service.EnquiryService;
import ch.unibe.ese.team3.controller.service.MessageService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.controller.service.VisitService;
import ch.unibe.ese.team3.exceptions.ForbiddenException;
import ch.unibe.ese.team3.exceptions.InvalidUserException;
import ch.unibe.ese.team3.exceptions.ResourceNotFoundException;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.VisitEnquiry;
import ch.unibe.ese.team3.model.enums.BookmarkStatus;

/**
 * This controller handles all requests concerning displaying ads and
 * bookmarking them.
 */
@Controller
public class AdController {

	@Autowired
	private AdService adService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookmarkService bookmarkService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private VisitService visitService;

	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private EnquiryService enquiryService;

	/** Gets the ad description page for the ad with the given id. */
	@RequestMapping(value = "/ad", method = RequestMethod.GET)
	public ModelAndView ad(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("adDescription");

		Ad ad = adService.getAdById(id);

		User user = principal != null 
				? userService.findUserByUsername(principal.getName()) 
				: null;

		if (ad == null) {
			throw new ResourceNotFoundException();
		}
		
		if (ad.isAuction() && user != null) {
			boolean userSentBuyRequest = auctionService.hasUserSentBuyRequest(ad, user);
			model.addObject("sentBuyRequest", userSentBuyRequest);
		}
		
		if (ad.isAuction()){
			model.addObject("bids", auctionService.getMostRecentBidsForAd(ad));
		}
		
		if (user != null){
			Map<Long, VisitEnquiry> sentEnquiries = enquiryService.getEnquiriesForAdBySender(ad, user);			
			model.addObject("sentEnquiries", sentEnquiries);
		}

		model.addObject("shownAd", ad);
		model.addObject("messageForm", new MessageForm());

		String loggedInUserEmail = (principal == null) ? "" : principal.getName();
		model.addObject("loggedInUserEmail", loggedInUserEmail);

		model.addObject("visits", visitService.getVisitsByAd(ad));

		return model;
	}

	/**
	 * Gets the ad description page for the ad with the given id and also
	 * validates and persists the message passed as post data.
	 */
	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	public ModelAndView messageSent(@RequestParam("id") long id, @Valid MessageForm messageForm,
			BindingResult bindingResult, Principal principal) {
		
		if (principal == null){
			throw new ForbiddenException();
		}
		
		ModelAndView model = new ModelAndView("adDescription");
		Ad ad = adService.getAdById(id);
		model.addObject("shownAd", ad);
		
		User sender = userService.findUserByUsername(principal.getName());

		if (!bindingResult.hasErrors()) {
			try {
				messageService.saveFrom(messageForm, sender);
				model.addObject("messageForm", new MessageForm());
			}
			catch (InvalidUserException ex){
				model.addObject("errorMessage", "Could not send message. The recipient is invalid");
				model.addObject("messageForm", messageForm);
			}		
		}
		return model;
	}

	/**
	 * Checks if the adID passed as post parameter is already inside user's List
	 * bookmarkedAds. In case it is present, true is returned changing the
	 * "Bookmark Ad" button to "Bookmarked". If it is not present it is added to
	 * the List bookmarkedAds.
	 * 
	 * @return 0 and 1 for errors; 3 to update the button to bookmarked 3 and 2
	 *         for bookmarking or undo bookmarking respectively 4 for removing
	 *         button completly (because its the users ad)
	 */
	@RequestMapping(value = "/bookmark", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int isBookmarked(@RequestParam("id") long id, @RequestParam("screening") boolean screening,
			@RequestParam("bookmarked") boolean bookmarked, Principal principal) {
		// should never happen since no bookmark button when not logged in
		if (principal == null) {
			return BookmarkStatus.NotLoggedIn.getStatusCode();
		}
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			// that should not happen...
			return BookmarkStatus.NoUserFound.getStatusCode();
		}
		List<Ad> bookmarkedAdsIterable = user.getBookmarkedAds();
		if (screening) {
			for (Ad ownAdIterable : adService.getAdsByUser(user)) {
				if (ownAdIterable.getId() == id) {
					return BookmarkStatus.OwnAd.getStatusCode();
				}
			}
			for (Ad adIterable : bookmarkedAdsIterable) {
				if (adIterable.getId() == id) {
					return BookmarkStatus.Bookmarked.getStatusCode();
				}
			}
			return BookmarkStatus.NotBookmarked.getStatusCode();
		}

		Ad ad = adService.getAdById(id);

		return bookmarkService.getBookmarkStatus(ad, bookmarked, user);
	}

	/**
	 * Fetches information about bookmarked rooms and own ads and attaches this
	 * information to the myRooms page in order to be displayed.
	 */
	@RequestMapping(value = "/profile/myRooms", method = RequestMethod.GET)
	public ModelAndView myRooms(Principal principal) {
		ModelAndView model;
		User user;
		if (principal != null) {
			model = new ModelAndView("myRooms");
			String username = principal.getName();
			user = userService.findUserByUsername(username);

			Iterable<Ad> ownAds = adService.getAdsByUser(user);

			model.addObject("bookmarkedAdvertisements", user.getBookmarkedAds());
			model.addObject("ownAdvertisements", ownAds);
			return model;
		} else {
			model = new ModelAndView("home");
		}

		return model;
	}

}