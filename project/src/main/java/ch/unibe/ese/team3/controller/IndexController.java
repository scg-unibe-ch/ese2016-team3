package ch.unibe.ese.team3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.enums.PageMode;

/**
 * This controller handles request concerning the home page and several other
 * simple pages.
 */


@Controller
public class IndexController {

	private SearchForm searchForm;
	
	@Autowired
	private AdService adService;

	/** Displays the home page. */
	@RequestMapping(value = "/")
	public ModelAndView index(@RequestAttribute("pageMode") PageMode pageMode) {		
		ModelAndView model = new ModelAndView("index");
		model.addObject("newest", adService.getNewestAds(4, BuyMode.fromPageMode(pageMode)));
		model.addObject("types", Type.values());
		return model;
	}
	
	@ModelAttribute
	public SearchForm getSearchForm() {
		if (searchForm == null) {
			searchForm = new SearchForm();
		}
		return searchForm;
	}
}