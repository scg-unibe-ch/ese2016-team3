package ch.unibe.ese.team3.controller.service;



import java.util.Date;

import org.springframework.stereotype.Service;

import ch.unibe.ese.team3.model.Ad;

@Service
public class AuctionService {
	public boolean checkIfAuctionisStillRunning(Ad ad){
		boolean isStillRunning;
		Date endDate = ad.getEndDate();
		Date currentDate = new Date();
		
		if (currentDate.after(endDate)) 
			isStillRunning = false;
		
		else
			isStillRunning = true;
		
		return isStillRunning;
	}

}
