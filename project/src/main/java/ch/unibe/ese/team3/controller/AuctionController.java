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
 * Handles all actions (like bid, etc) concerning auction
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
	
	@RequestMapping(value = "/profile/bidAuction", method = RequestMethod.POST)
	public ModelAndView bid(Principal principal, @RequestParam int amount, @RequestParam long id, RedirectAttributes redirectAttributes) {
		User bidder = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		if (auctionService.checkAndBid(ad, bidder, amount)) {			
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Your bid was registered successfully. If you win the auction, the advertiser will contact you.");
			return model;
		}
		else{
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("errorMessage",
					"Sorry, someone else bade more");
			return model;
		}

	}
	
	@RequestMapping(value = "/profile/buyAuction", method = RequestMethod.POST)
	public ModelAndView buy(Principal principal, @RequestParam long id, RedirectAttributes redirectAttributes) {

		User purchaser = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		if (auctionService.checkAndBuy(ad, purchaser)) {			
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Your purchase was registered successfully. The advertiser will contact you.");
			return model;
		}
		else{
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("errorMessage",
					"Sorry, someone else bought it already.");
			return model;
		}
	}
	
	@RequestMapping(value = "/profile/auctions", method = RequestMethod.GET)
	public ModelAndView showAuctionManagement(Principal principal){
		User owner = userService.findUserByUsername(principal.getName());		
		
		ModelAndView model = new ModelAndView("AuctionManagement");
		model.addObject("runningAuctions", auctionService.getRunningAuctionsForUser(owner));
		model.addObject("stoppedAuctions", auctionService.getStoppedAuctionsForUser(owner));
		model.addObject("expiredAuctions", auctionService.getExpiredAuctionsForUser(owner));
		model.addObject("notStartedAuctions", auctionService.getNotYetRunningAuctionsForUser(owner));
		model.addObject("completedAuctions", auctionService.getCompletedAuctionsForUser(owner));
		
		return model;
	}
	
	@RequestMapping(value = "/profile/mybids", method = RequestMethod.GET)
	public ModelAndView showMyAuctions(Principal principal){
		User currentUser = userService.findUserByUsername(principal.getName());
		
		ModelAndView model = new ModelAndView("MyBids");
		model.addObject("myauctions", auctionService.getBidsByUser(currentUser));
		
		return model;
	}
	
	@RequestMapping(value ="/profile/auction", method = RequestMethod.GET)
	public ModelAndView showAuctionDetails(Principal principal, @RequestParam("id") int id){
		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		checkPermissions(owner, ad);
		
		return getModelForAuctionDetails(ad);
	}

	@RequestMapping(value ="/profile/auction/complete", method = RequestMethod.POST)
	public ModelAndView completeAuction(Principal principal, @RequestParam("adIdComplete") int id){
		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		checkPermissions(owner, ad);
		
		auctionService.completeAuction(ad);
		
		String message = "Auction has been completed!";		
		ModelAndView model = getModelForAuctionDetails(ad, message);
		
		return model;
	}
	
	@RequestMapping(value ="/profile/auction/resume", method = RequestMethod.POST)
	public ModelAndView resumeAuction(Principal principal, @RequestParam("adIdResume") int id){
		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		checkPermissions(owner, ad);
		
		auctionService.resumeAuction(ad);
		String message = "Auction has been resumed!";
		
		ModelAndView model = getModelForAuctionDetails(ad, message);
		
		return model;
	}
	
	@RequestMapping(value ="/profile/auction/pause", method = RequestMethod.POST)
	public ModelAndView pauseAuction(Principal principal, @RequestParam("adIdPause") int id){
		User owner = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		checkPermissions(owner, ad);
		
		auctionService.stopAuction(ad);
		String message = "Auction has been paused!";
		
		ModelAndView model = getModelForAuctionDetails(ad, message);
		
		return model;
	}
	
	private ModelAndView getModelForAuctionDetails(Ad ad) {
		return getModelForAuctionDetails(ad, null);
	}

	private ModelAndView getModelForAuctionDetails(Ad ad, String message) {
		List<Bid> bids = auctionService.getBidsForAd(ad);
		List<PurchaseRequest> purchaseRequests = auctionService.getPurchaseRequestForAd(ad);
		
		ModelAndView model = new ModelAndView("AuctionDetails");
		model.addObject("ad", ad);
		model.addObject("bids", bids);
		model.addObject("purchaseRequests", purchaseRequests);
		
		if (message != null && !message.isEmpty()){
			model.addObject("confirmationMessage", message);
		}
		
		return model;
	}
	
	private void checkPermissions(User owner, Ad ad) {
		if (ad == null){
			throw new ResourceNotFoundException();
		}
		
		if (!ad.getUser().equals(owner)){
			throw new ForbiddenException();
		}
		
		if (!ad.isAuction()){
			throw new ResourceNotFoundException();
		}
	}

}
