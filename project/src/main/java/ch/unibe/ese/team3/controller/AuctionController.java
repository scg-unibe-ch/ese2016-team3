package ch.unibe.ese.team3.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.controller.service.AuctionService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.exceptions.ForbiddenException;
import ch.unibe.ese.team3.exceptions.ResourceNotFoundException;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;
import ch.unibe.ese.team3.model.PurchaseRequest;
import ch.unibe.ese.team3.model.User;

/**
 * 
 * Handles all actions (bid, place purchase request, pause etc.) concerning auctions
 *
 */
@Controller
public class AuctionController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdService adService;
	@Autowired
	private AuctionService auctionService;

	/**
	 * Bid for a specific auction
	 * 
	 * @param principal
	 *            the logged in user
	 * @param amount
	 *            the amount to bid
	 * @param id
	 *            the id of the ad under auction
	 * @return
	 */
	@RequestMapping(value = "/profile/bidAuction", method = RequestMethod.POST)
	public ModelAndView bid(Principal principal, @RequestParam int amount, @RequestParam long id,
			RedirectAttributes redirectAttributes) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User bidder = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);

		if (auctionService.checkAndBid(ad, bidder, amount)) {

			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Your bid was registered successfully. If you win the auction, the advertiser will contact you.");
			return model;
		} else {
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("errorMessage", "Sorry, someone else bade more");
			return model;
		}

	}

	/**
	 * Place a purchase request for an auction
	 * 
	 * @param principal
	 *            the logged in user
	 * @param id
	 *            the id of the ad under auction
	 * @return
	 */
	@RequestMapping(value = "/profile/buyAuction", method = RequestMethod.POST)
	public ModelAndView buy(Principal principal, @RequestParam long id, RedirectAttributes redirectAttributes) {

		if (principal == null) {
			throw new ForbiddenException();
		}

		User purchaser = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);

		if (auctionService.checkAndBuy(ad, purchaser)) {
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Your purchase was registered successfully. The advertiser will contact you.");
			return model;
		} else {
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("errorMessage", "Sorry, someone else bought it already.");
			return model;
		}
	}

	/**
	 * Show the auction management page of the current user. The user must be
	 * logged in. Returns a page containing an overview of all auctions of the
	 * user.
	 * 
	 * @param principal
	 *            the logged in user
	 * @return
	 */
	@RequestMapping(value = "/profile/auctions", method = RequestMethod.GET)
	public ModelAndView showAuctionManagement(Principal principal) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User owner = userService.findUserByUsername(principal.getName());

		ModelAndView model = new ModelAndView("AuctionManagement");
		model.addObject("runningAuctions", auctionService.getRunningAuctionsForUser(owner));
		model.addObject("stoppedAuctions", auctionService.getStoppedAuctionsForUser(owner));
		model.addObject("expiredAuctions", auctionService.getExpiredAuctionsForUser(owner));
		model.addObject("notStartedAuctions", auctionService.getNotYetRunningAuctionsForUser(owner));
		model.addObject("completedAuctions", auctionService.getCompletedAuctionsForUser(owner));

		return model;
	}

	/**
	 * Show an overview of all bids of the current user
	 * 
	 * @param principal
	 *            the logged in user
	 * @return
	 */
	@RequestMapping(value = "/profile/mybids", method = RequestMethod.GET)
	public ModelAndView showMyAuctions(Principal principal) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User currentUser = userService.findUserByUsername(principal.getName());

		ModelAndView model = new ModelAndView("MyBids");
		model.addObject("myauctions", auctionService.getBidsByUser(currentUser));

		return model;
	}

	/**
	 * Show the auction details (like startdate, enddate, state) of a specific
	 * auction.
	 * 
	 * @param principal
	 *            the logged in user
	 * @param id
	 *            the id of the ad under auction, must belong to the current
	 *            user
	 * @return
	 */
	@RequestMapping(value = "/profile/auction", method = RequestMethod.GET)
	public ModelAndView showAuctionDetails(Principal principal, @RequestParam("id") int id) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);

		checkPermissions(owner, ad);

		return getModelForAuctionDetails(ad);
	}

	/**
	 * Complete a specific auction
	 * 
	 * @param principal
	 *            the logged in user
	 * @param id
	 *            the id of the ad under auction, must belong to the current
	 *            user
	 * @return
	 */
	@RequestMapping(value = "/profile/auction/complete", method = RequestMethod.POST)
	public ModelAndView completeAuction(Principal principal, @RequestParam("adIdComplete") int id) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);

		checkPermissions(owner, ad);

		ModelAndView model;

		if (auctionService.completeAuction(ad)) {
			String message = "Auction has been completed!";
			model = getModelForAuctionDetails(ad, message, true);
		} else {
			String message = "Couldn't complete auction!";
			model = getModelForAuctionDetails(ad, message, false);
		}

		return model;
	}

	/**
	 * Resume a paused auction
	 * 
	 * @param principal
	 * @param id
	 *            the id of the ad under auction, must belong to the current
	 *            user
	 * @return
	 */
	@RequestMapping(value = "/profile/auction/resume", method = RequestMethod.POST)
	public ModelAndView resumeAuction(Principal principal, @RequestParam("adIdResume") int id) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);

		checkPermissions(owner, ad);

		ModelAndView model;

		if (auctionService.resumeAuction(ad)) {
			String message = "Auction has been completed!";
			model = getModelForAuctionDetails(ad, message, true);
		} else {
			String message = "Couldn't resume auction!";
			model = getModelForAuctionDetails(ad, message, false);
		}

		return model;
	}

	/**
	 * Pause a specific auction
	 * 
	 * @param principal
	 *            the logged in user
	 * @param id
	 *            the id of the ad under auction, must belong to the current
	 *            user
	 * @return
	 */
	@RequestMapping(value = "/profile/auction/pause", method = RequestMethod.POST)
	public ModelAndView pauseAuction(Principal principal, @RequestParam("adIdPause") int id) {
		if (principal == null) {
			throw new ForbiddenException();
		}

		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);

		checkPermissions(owner, ad);

		ModelAndView model;

		if (auctionService.stopAuction(ad)) {
			String message = "Auction has been completed!";
			model = getModelForAuctionDetails(ad, message, true);
		} else {
			String message = "Couldn't stop pause auction!";
			model = getModelForAuctionDetails(ad, message, false);
		}

		return model;
	}

	private ModelAndView getModelForAuctionDetails(Ad ad) {
		return getModelForAuctionDetails(ad, null, true);
	}

	/**
	 * Returns an auction details model for the specified ad with a specified
	 * message.
	 * 
	 * @param ad
	 *            the ad under auction
	 * @param message
	 *            the message to pass along with the model
	 * @param success
	 *            true if the message is a success message,
	 *            false if the message is a failure message
	 * @return
	 */
	private ModelAndView getModelForAuctionDetails(Ad ad, String message, boolean success) {
		List<Bid> bids = auctionService.getBidsForAd(ad);
		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(ad);

		ModelAndView model = new ModelAndView("AuctionDetails");
		model.addObject("ad", ad);
		model.addObject("bids", bids);
		model.addObject("purchaseRequests", purchaseRequests);

		if (message != null && !message.isEmpty()) {
			model.addObject(success ? "confirmationMessage" : "errorMessage", message);
		}

		return model;
	}

	/**
	 * Check whether the owner is allowed to view or modify the auction status
	 * of the specified ad under auction. Throws an exception if: the ad does
	 * not exist or the user is not the owner of the ad or the ad is not an
	 * auction
	 * 
	 * @param owner
	 * @param ad
	 */
	private void checkPermissions(User owner, Ad ad) {
		if (ad == null) {
			throw new ResourceNotFoundException();
		}

		if (!ad.getUser().equals(owner)) {
			throw new ForbiddenException();
		}

		if (!ad.isAuction()) {
			throw new ResourceNotFoundException();
		}
	}

}
