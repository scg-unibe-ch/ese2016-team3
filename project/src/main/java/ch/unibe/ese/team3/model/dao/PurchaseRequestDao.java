package ch.unibe.ese.team3.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.PurchaseRequest;
import ch.unibe.ese.team3.model.User;

public interface PurchaseRequestDao extends CrudRepository<PurchaseRequest, Long>  {
	Iterable<PurchaseRequest> findByAd(Ad ad);

	Iterable<PurchaseRequest> findByAdOrderByCreatedAsc(Ad ad);

	Iterable<PurchaseRequest> findByAdAndPurchaser(Ad ad, User purchaser);

}
