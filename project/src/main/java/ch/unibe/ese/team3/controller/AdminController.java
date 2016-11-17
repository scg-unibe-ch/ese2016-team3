package ch.unibe.ese.team3.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.controller.pojos.forms.EditPremiumChoiceForm;
import ch.unibe.ese.team3.controller.service.PremiumChoiceService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.PremiumChoiceDao;

@Controller
public class AdminController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	PremiumChoiceService premiumChoiceService;
	
	@Autowired
	PremiumChoiceDao premiumChoiceDao;
	
	@RequestMapping(value = "/profile/adminManagement", method = RequestMethod.GET)
	public ModelAndView managePage(Principal principal) {
		ModelAndView model = new ModelAndView("adminManagement");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		Iterable<PremiumChoice> choices = premiumChoiceDao.findAll();
		model.addObject("choices", choices);
		for (PremiumChoice choice : choices) {
		model.addObject("editPremiumChoiceForm" + choice.getId(), new EditPremiumChoiceForm());
		}
		model.addObject("currentUser", user);
		return model;
	}
	
	@RequestMapping(value = "/profile/adminManagement", method = RequestMethod.POST)
	public ModelAndView manageResultPage(
			@Valid EditPremiumChoiceForm editPremiumChoiceForm,
			BindingResult bindingResult, Principal principal) {

			String username = principal.getName();
			User user = userService.findUserByUsername(username);
			if (!bindingResult.hasErrors()) {
				Iterable<PremiumChoice> choices = premiumChoiceDao.findAll();
				for (PremiumChoice choice : choices) {
					premiumChoiceService.updateFrom(editPremiumChoiceForm, choice.getId());
				}
				ModelAndView model = new ModelAndView("adminManagement");
				model.addObject("confirmationMessage", "Update complete!");
				return model;
			} else {
				ModelAndView model = new ModelAndView("adminManagement");
				Iterable<PremiumChoice> choices = premiumChoiceDao.findAll();
				for (PremiumChoice choice : choices) {
					model.addObject("editPremiumChoiceForm" + choice.getId(), new EditPremiumChoiceForm());
				}
				model.addObject("currentUser", user);
				return model;
			}
	}

}
