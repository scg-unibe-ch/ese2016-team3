package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.model.PremiumChoice;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class UpgradeServiceTest {
	
	@Autowired
	private UpgradeService upgradeService;
	
	
	@Test
	public void testFindByDuration(){
		
		PremiumChoice choice1 = upgradeService.findPremiumChoiceByDuration(7);
		
		assertEquals(7, choice1.getDuration());
	}
}
