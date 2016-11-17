package ch.unibe.ese.team3.controller.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.EditPremiumChoiceForm;
import ch.unibe.ese.team3.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.User;
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
	
	@Transactional
	public void updateFrom(EditPremiumChoiceForm editPremiumChoiceForm, long id) {
		
		PremiumChoice premiumChoice = this.findPremiumChoiceById(id);
		
		premiumChoice.setDuration(editPremiumChoiceForm.getDuration());
		premiumChoice.setPrice(editPremiumChoiceForm.getPrice());

		
		premiumChoiceDao.save(premiumChoice);
	}
	
}
