package ch.unibe.ese.team3.controller;

import java.security.Principal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.unibe.ese.team3.controller.pojos.PictureUploader;
import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.controller.service.AlertService;
import ch.unibe.ese.team3.controller.service.EditAdService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.dto.PictureMeta;
import ch.unibe.ese.team3.exceptions.ForbiddenException;
import ch.unibe.ese.team3.exceptions.ResourceNotFoundException;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.enums.Distance;

/**
 * This controller handles all requests concerning editing ads.
 */
@Controller
public class EditAdController {

	private final static String IMAGE_DIRECTORY = PlaceAdController.IMAGE_DIRECTORY;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AdService adService;

	@Autowired
	private EditAdService editAdService;

	@Autowired
	private UserService userService;

	@Autowired
	private AlertService alertService;

	private PictureUploader pictureUploader;

	private ObjectMapper objectMapper;

	/**
	 * Serves the page that allows the user to edit the ad with the given id.
	 */
	@RequestMapping(value = "/profile/editAd", method = RequestMethod.GET)
	public ModelAndView editAdPage(@RequestParam long id, Principal principal, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("editAd");
		Ad ad = adService.getAdById(id);
		
		if (ad == null){
			throw new ResourceNotFoundException();
		}
		
		if (!userCanEditAd(principal, ad) ){
			throw new ForbiddenException();
		}
		
		PlaceAdForm form = editAdService.fillForm(ad);	
		
		model.addObject("adId", ad.getId());
		model.addObject("existingPictures", ad.getPictures());
		model.addObject("placeAdForm", form);
		model.addObject("types", Type.values());
		model.addObject("distances", Distance.values());
		model.addObject("infrastructureTypes", InfrastructureType.values());

		String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
		if (pictureUploader == null) {
			pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
		}

		return model;
	}

	private boolean userCanEditAd(Principal principal, Ad ad) {
		return ad.getUser().getEmail().equals(principal.getName());
	}

	/**
	 * Processes the edit ad form and displays the result page to the user.
	 */
	@RequestMapping(value = "/profile/editAd", method = RequestMethod.POST)
	public ModelAndView editAdPageWithForm(@Valid PlaceAdForm placeAdForm,
			BindingResult result, Principal principal,
			RedirectAttributes redirectAttributes, @RequestParam long adId) {
		ModelAndView model = new ModelAndView("editAd");
		
		if (!result.hasErrors()) {
			String username = principal.getName();
			User user = userService.findUserByUsername(username);
			Ad existingAd = adService.getAdById(adId);
			
			if (existingAd == null){
				throw new ResourceNotFoundException();
			}
			
			if (!userCanEditAd(principal, existingAd)){
				throw new ForbiddenException();
			}			

			String realPath = servletContext.getRealPath(IMAGE_DIRECTORY);
			if (pictureUploader == null) {
				pictureUploader = new PictureUploader(realPath, IMAGE_DIRECTORY);
			}
			List<String> fileNames = pictureUploader.getFileNames();
			Ad ad = editAdService.saveFrom(placeAdForm, fileNames, user, adId);

			// triggers all alerts that match the placed ad
			alertService.triggerAlerts(ad);

			// reset the picture uploader
			this.pictureUploader = null;

			model = new ModelAndView("redirect:../ad?id=" + ad.getId());
			redirectAttributes.addFlashAttribute("confirmationMessage",
					"Ad edited successfully. You can take a look at it below.");
			if(ad.getLatitude() == null && ad.getLongitude() == null){
				redirectAttributes.addFlashAttribute("warningMessage", "Please reconsider the adress, there were no coordinates found");
			}
		}
		else {
			Ad ad = adService.getAdById(adId);
			model.addObject("adId", ad.getId());
			model.addObject("existingPictures", ad.getPictures());
			model.addObject("distances", Distance.values());
			model.addObject("placeAdForm", placeAdForm);
			model.addObject("types", Type.values());
			model.addObject("infrastructureTypes", InfrastructureType.values());
		}

		return model;
	}

	/**
	 * Deletes the ad picture with the given id from the list of pictures from
	 * the ad, but not from the server.
	 */
	@RequestMapping(value = "/profile/editAd/deletePictureFromAd", method = RequestMethod.POST)
	public @ResponseBody void deletePictureFromAd(@RequestParam long adId,
			@RequestParam long pictureId) {
		editAdService.deletePictureFromAd(adId, pictureId);
	}

	/**
	 * Gets the descriptions for the pictures that were uploaded with the
	 * current picture uploader.
	 * 
	 * @return a list of picture descriptions or null if no pictures were
	 *         uploaded
	 */
	/*
	@RequestMapping(value = "/profile/editAd/getUploadedPictures", method = RequestMethod.POST)
	public @ResponseBody List<PictureMeta> getUploadedPictures() {
		if (pictureUploader == null) {
			return null;
		}
		return pictureUploader.getUploadedPictureMetas();
	}
	*/
	/**
	 * Uploads the pictures that are attached as multipart files to the request.
	 * The JSON representation, that is returned, is generated manually because
	 * the jQuery Fileupload plugin requires this special format.
	 * 
	 * @return A JSON representation of the uploaded files
	 */
	@RequestMapping(value = "/profile/editAd/uploadPictures", method = RequestMethod.POST)
	public @ResponseBody String uploadPictures(
			MultipartHttpServletRequest request) {
		List<MultipartFile> pictures = new LinkedList<>();
		Iterator<String> iter = request.getFileNames();

		while (iter.hasNext()) {
			pictures.add(request.getFile(iter.next()));
		}

		List<PictureMeta> uploadedPicturesMeta = pictureUploader
				.upload(pictures);

		objectMapper = new ObjectMapper();
		String jsonResponse = "{\"files\": ";
		try {
			jsonResponse += objectMapper
					.writeValueAsString(uploadedPicturesMeta);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		jsonResponse += "}";
		return jsonResponse;
	}

	/**
	 * Deletes the uploaded picture at the given relative url (relative to the
	 * webapp folder).
	 */
	@RequestMapping(value = "/profile/editAd/deletePicture", method = RequestMethod.POST)
	public @ResponseBody void deleteUploadedPicture(@RequestParam String url) {
		if (pictureUploader != null) {
			String realPath = servletContext.getRealPath(url);
			pictureUploader.deletePicture(url, realPath);
		}
	}
}
