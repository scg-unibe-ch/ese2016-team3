package ch.unibe.ese.team3.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team3.model.Alert;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.User;

public interface AlertDao extends CrudRepository<Alert, Long>{

	public Iterable<Alert> findByUser(User user);
	
	public Iterable<Alert> findByPriceGreaterThanAndBuyMode(int price, BuyMode buyMode);
}