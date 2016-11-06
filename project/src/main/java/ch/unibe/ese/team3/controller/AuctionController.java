package ch.unibe.ese.team3.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.controller.service.BidService;
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
	private BidService bidService;
	@Autowired
	private UserService userService;
	@Autowired
	private AdService adService;

	
	@RequestMapping(value = "/resultsAuction", method = RequestMethod.POST)
	public void bid(Principal principal, int amount , long id){
		
		User bidder = userService.findUserByUsername(principal.getName());
		Ad ad = adService.getAdById(id);
		
		bidService.bid(ad, bidder, amount);
		
	}
	
	
	
	

}
