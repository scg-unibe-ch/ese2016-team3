package ch.unibe.ese.team3.controller.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.dao.PremiumChoiceDao;

/**
 * Handles all database actions concerning premium choices.
 */
@Service
public class PremiumChoiceService {
	
	@Autowired
	private PremiumChoiceDao premiumChoiceDao;

	@Transactional
	public PremiumChoice findPremiumChoiceByDuration(int duration) {
		return premiumChoiceDao.findByDuration(duration);
	}
	
	@Transactional
	public PremiumChoice findPremiumChoiceById(long id){
		return premiumChoiceDao.findPremiumChoiceById(id);
	}
	
	@Transactional
	public Iterable<PremiumChoice> findAll(){
		return premiumChoiceDao.findAll();
	}
	
	public List<Integer> getDurations(){
		Iterable<PremiumChoice> choices = findAll();
		ArrayList<Integer> durations = new ArrayList<Integer>();
		for(PremiumChoice obj : choices){
			durations.add(obj.getDuration());
		}
		return durations;
	}
	
}
