package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team3.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team3.controller.pojos.forms.UpgradeForm;
import ch.unibe.ese.team3.model.CreditcardType;
import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.PremiumChoiceDao;
import ch.unibe.ese.team3.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class UpgradeServiceTest {
	
	@Autowired
	private UpgradeService upgradeService;
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testFindByDuration() {
		
		PremiumChoice choice1 = upgradeService.findPremiumChoiceByDuration(7);	
		assertEquals(7, choice1.getDuration());
	}	
	
	@Test 
	public void testFindById() {
		
		PremiumChoice choice1 = upgradeService.findPremiumChoiceByDuration(7);
		PremiumChoice choice2 = upgradeService.findPremiumChoiceById(choice1.getId());
		
		assertEquals(7, choice2.getDuration());
	}
	
	@Transactional
	@Test
	public void testUpgradeFrom() {
		UpgradeForm form = new UpgradeForm();
		User mark = userDao.findByUsername("mark@knopfler.com");
		PremiumChoice choice = upgradeService.findPremiumChoiceByDuration(7);
		
		String newCreditcardName = "mark knopfler";

		form.setCreditCard("1111-1111-1111-1111");
		form.setCreditcardName(newCreditcardName);
		form.setCreditcardType(CreditcardType.VISA);
		form.setExpirationMonth("11");
		form.setExpirationYear("23");
		form.setSecurityNumber("333");
		
		upgradeService.upgradeFrom(form, mark, choice);
		
		assertEquals("1111111111111111", mark.getCreditCard());// formatted creditcard
		assertEquals(newCreditcardName, mark.getCreditcardName());
		assertEquals(CreditcardType.VISA, mark.getCreditcardType());
		assertEquals("11", mark.getExpirationMonth());
		assertEquals("23", mark.getExpirationYear());
		assertEquals("333", mark.getSecurityNumber());
		assertEquals(true, mark.isPremium());
		assertEquals(7, mark.getPremiumChoice().getDuration());
		
	}
	
	@Test
	public void findAllPremiumChoices() {
		Iterable<PremiumChoice> choices = upgradeService.findAll();
		
		int n=0;
		for (PremiumChoice choice : choices) {
			n += 1;
		}
		assertEquals(4, n);	
	}
}
