package ch.unibe.ese.team3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.enums.PageMode;

@Controller
public class MapController {
	/** Returns page, when mapView url is called. */
	@RequestMapping(value = "/mapView")
	public ModelAndView mapView(@RequestAttribute("pageMode") PageMode pageMode) {		
		ModelAndView model = new ModelAndView("mapView");

		return model;
	}
}
