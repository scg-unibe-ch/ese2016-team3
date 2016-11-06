package ch.unibe.ese.team3.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team3.model.PremiumChoice;

public interface PremiumChoiceDao extends CrudRepository<PremiumChoice, Long> {
	public PremiumChoice findByDuration(int duration);
	
	public PremiumChoice findPremiumChoiceById(long id);
}
