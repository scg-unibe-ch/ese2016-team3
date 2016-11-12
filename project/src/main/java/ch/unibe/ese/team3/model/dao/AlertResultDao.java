package ch.unibe.ese.team3.model.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.VisitEnquiry;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AlertResult;

public interface AlertResultDao extends CrudRepository<AlertResult, Long> {
	public Iterable<AlertResult> findByUser (User user);
	
	public Iterable<AlertResult> findByTriggerDate (Date triggerDate);
}
