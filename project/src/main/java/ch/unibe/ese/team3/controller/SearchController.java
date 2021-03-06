package ch.unibe.ese.team3.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.unibe.ese.team3.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.dto.AdMeta;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.enums.PageMode;

/** Handles all requests concerning the search for ads. */
@Controller
public class SearchController {

	@Autowired
	private AdService adService;

	private ObjectMapper objectMapper;

	/** Shows the search ad page. */
	@RequestMapping(value = "/searchAd", method = RequestMethod.GET)
	public ModelAndView searchAd() {
		ModelAndView model = new ModelAndView("searchAd");
		model.addObject("types", Type.values());
		return model;
	}

	/**
	 * Gets the results when filtering the ads in the database by the parameters
	 * in the search form.
	 */
	@RequestMapping(value = "/results", method = RequestMethod.POST)
	public ModelAndView results(@Valid SearchForm searchForm, BindingResult result,
			@RequestAttribute("pageMode") PageMode pageMode, Principal principal) {
		if (!result.hasErrors()) {
			ModelAndView model = new ModelAndView("results");
			model.addObject("results", adService.queryResults(searchForm, BuyMode.fromPageMode(pageMode)));
			model.addObject("types", Type.values());
			model.addObject("infrastructureTypes", InfrastructureType.values());

			List<AdMeta> adResults = new ArrayList<>();
			Iterable<Ad> iter = adService.queryResults(searchForm, BuyMode.fromPageMode(pageMode));
			Iterator<Ad> iterator = iter.iterator();

			while (iterator.hasNext()) {
				AdMeta admeta = new AdMeta();
				Ad ad = iterator.next();
				admeta.setCity(ad.getCity());
				admeta.setStreet(ad.getStreet());
				admeta.setZipcode(Integer.toString(ad.getZipcode()));
				admeta.setId(Long.toString(ad.getId()));
				admeta.setName(ad.getTitle());
				admeta.setPrice(Integer.toString(ad.getPrice()));
				if (!ad.getPictures().isEmpty()) {
					admeta.setPicture(ad.getPictures().get(0).getFilePath());
				}

				if(ad.getLatitude() != null && ad.getLongitude() != null){
					admeta.setLat(ad.getLatitude());
					admeta.setLng(ad.getLongitude());
				}
				
					adResults.add(admeta);
				
				
			}

			objectMapper = new ObjectMapper();
			String jsonResponse = "";
			
			try {
				jsonResponse += objectMapper.writeValueAsString(adResults);
			} catch (JsonProcessingException e) {
				
				e.printStackTrace();
			}
			jsonResponse += "";


			model.addObject("resultsInJson", jsonResponse);

			String loggedInUserEmail = (principal == null) ? "" : principal.getName();
			model.addObject("loggedInUserEmail", loggedInUserEmail);
			return model;
		} else {
			// go back
			return searchAd();
		}
	}

	@ModelAttribute
	public SearchForm getSearchForm() {
		return new SearchForm();
	}
}