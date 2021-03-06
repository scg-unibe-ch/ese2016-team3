package ch.unibe.ese.team3.model.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;

public interface AdDao extends CrudRepository<Ad, Long> {
	
	public Iterable<Ad> findByUser(User user);

	public Iterable<Ad> findByPriceLessThanAndTypeInAndBuyMode(int price, Type[] types, BuyMode buyMode);

	public Iterable<Ad> findByPriceLessThanAndBuyMode(int price, BuyMode buyMode);

	public Ad findByTitle(String title);

	public Iterable<Ad> findByUserAndAuction(User user, boolean isAuction);
	
	public Iterable<Ad> findByEndDate(Date date);
	
	public Iterable<Ad> findByEndDateLessThanAndAuctionMessageSent(Date date, boolean auctionMessageSent);
	

}