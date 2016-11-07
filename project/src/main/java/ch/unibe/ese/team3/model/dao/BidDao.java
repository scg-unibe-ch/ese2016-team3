package ch.unibe.ese.team3.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Bid;

public interface BidDao extends CrudRepository<Bid, Long>{
	public Bid findTopByAdOrderByAmountDesc(Ad ad);
}
