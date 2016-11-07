package ch.unibe.ese.team3.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.controller.service.AuctionService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.User;

/**
 * 
 * handels all actions (like bid, etc) concerning auction
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

//	@Autowired
//	private TaskScheduler scheduler;
	
	/**
	 * @param principal				contains user data
	 * @param amount				amount, which the user bids (should already be higher than currentAuctionPrice)
	 * @param id					Ad id
	 * @param redirectAttributes	
	 */
	@RequestMapping(value = "/profile/bidAuction", method = RequestMethod.POST)
	public ModelAndView bid(Principal principal, @RequestParam int amount, @RequestParam long id, RedirectAttributes redirectAttributes) {

		User bidder = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		if (auctionService.checkAndBid(ad, bidder, amount)) {			
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Your bid was registered successfully.");
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
					"Your purchase was registered successfully.");
			return model;
		}
		else{
			ModelAndView model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("errorMessage",
					"Sorry, someone else bought it already.");
			return model;
		}

	}
	
	
	@Scheduled(fixedRate=1000)
	public void testSchedule(){

	}
	
	/*
	 * Define scheduled execution of method checkAuctionsStillRunning
	 * 	according to: http://stackoverflow.com/questions/8584876/spring-mvc-3-time-scheduled-task-starting-at-a-specific-time
	 */
	// who calls init? - how to add scheduler to configuration file
//	public void init() {
//		scheduler.scheduleAtFixedRate(new Runnable() {
//			public void run() {
//				checkAuctionsStillRunning();
//			}
//		}, new Date(), 1000 * 60 * 60 * 2);
//	}
//
//	public void checkAuctionsStillRunning() {
//		// method, which should be scheduled
//	}

}
