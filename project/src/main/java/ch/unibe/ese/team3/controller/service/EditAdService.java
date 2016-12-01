package ch.unibe.ese.team3.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import ch.unibe.ese.team3.base.BaseService;
import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.AdPictureDao;

/** Provides services for editing ads in the database. */
@Service
public class EditAdService extends BaseService {

	@Autowired
	private AdService adService;

	@Autowired
	private AdDao adDao;

	@Autowired
	private AdPictureDao adPictureDao;

	/**
	 * Handles persisting an edited ad to the database.
	 * 
	 * @param placeAdForm
	 *            the form to take the data from
	 * @param a
	 *            list of the file paths the pictures are saved under
	 * @param the
	 *            currently logged in user
	 */
	@Transactional
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths, User user, long adId) {

		Ad ad = adService.getAdById(adId);

		Date now = new Date();
		ad.setCreationDate(now);

		ad.setTitle(placeAdForm.getTitle());

		ad.setStreet(placeAdForm.getStreet());

		// ad.setStudio(placeAdForm.getStudio());

		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));

		String addressString = String.format("%s %s %s", placeAdForm.getStreet(), zip,
				placeAdForm.getCity().substring(7));

		LatLng coordinates = getCoordinates(addressString);
		if (coordinates != null) {
			ad.setLatitude(coordinates.getLat());
			ad.setLongitude(coordinates.getLng());
		}

		
		
		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		try {
			if (placeAdForm.getMoveInDate().length() >= 1) {
				int dayMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(0, 2));
				int monthMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(3, 5));
				int yearMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(6, 10));
				calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn);
				ad.setMoveInDate(calendar.getTime());
			}
		} catch (NumberFormatException e) {
		}

		ad.setPrice(placeAdForm.getPrice());
		ad.setSquareFootage(placeAdForm.getSquareFootage());
		ad.setDistanceSchool(placeAdForm.getDistanceSchool());
		ad.setDistanceShopping(placeAdForm.getDistanceShopping());
		ad.setDistancePublicTransport(placeAdForm.getDistancePublicTransport());
		ad.setBuildYear(placeAdForm.getBuildYear());
		ad.setRenovationYear(placeAdForm.getRenovationYear());
		ad.setNumberOfRooms(placeAdForm.getNumberOfRooms());
		ad.setNumberOfBath(placeAdForm.getNumberOfBath());
		// ad description values
		ad.setParking(placeAdForm.isParking());
		ad.setDishwasher(placeAdForm.getDishwasher());
		ad.setRoomDescription(placeAdForm.getRoomDescription());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setGarage(placeAdForm.getGarage());

		ad.setElevator(placeAdForm.isElevator());
		ad.setInfrastructureType(placeAdForm.getInfrastructureType());
		ad.setFloorLevel(placeAdForm.getFloorLevel());

		/*
		 * Save the paths to the picture files, the pictures are assumed to be
		 * uploaded at this point!
		 */
		for (String filePath : filePaths) {
			AdPicture picture = new AdPicture();
			picture.setFilePath(filePath);
			ad.addPicture(picture);
		}

		// visits
		List<Visit> visits = new LinkedList<>();
		List<String> visitStrings = placeAdForm.getVisits();
		if (visitStrings != null) {
			for (String visitString : visitStrings) {
				Visit visit = new Visit();
				// format is 28-02-2014;10:02;13:14
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String[] parts = visitString.split(";");
				String startTime = parts[0] + " " + parts[1];
				String endTime = parts[0] + " " + parts[2];
				Date startDate = null;
				Date endDate = null;
				try {
					startDate = dateFormat.parse(startTime);
					endDate = dateFormat.parse(endTime);
				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				visit.setStartTimestamp(startDate);
				visit.setEndTimestamp(endDate);
				visit.setAd(ad);
				visits.add(visit);
			}
			
			ad.getVisits().addAll(visits);
		}

		ad.setUser(user);

		adDao.save(ad);

		return ad;
	}

	/**
	 * Removes the picture with the given id from the list of pictures in the ad
	 * with the given id.
	 */
	@Transactional
	public void deletePictureFromAd(long adId, long pictureId) {
		Ad ad = adService.getAdById(adId);
		List<AdPicture> pictures = ad.getPictures();
		AdPicture picture = adPictureDao.findOne(pictureId);
		pictures.remove(picture);
		adDao.save(ad);
	}

	/**
	 * Fills a Form with the data of an ad.
	 */
	public PlaceAdForm fillForm(Ad ad) {
		PlaceAdForm adForm = new PlaceAdForm();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		adForm.setAuction(ad.isAuction());
		adForm.setAuctionPrice(ad.getPrice());
		adForm.setBalcony(ad.getBalcony());
		adForm.setBuildYear(ad.getBuildYear());
		adForm.setCity(String.format("%d - %s", ad.getZipcode(), ad.getCity().replaceAll("/", ";")));
		adForm.setDishwasher(ad.getDishwasher());
		adForm.setDistancePublicTransport(ad.getDistancePublicTransport());
		adForm.setDistanceSchool(ad.getDistanceSchool());
		adForm.setDistanceShopping(ad.getDistanceShopping());
		adForm.setElevator(ad.getElevator());
		if (ad.getEndDate() != null){
			adForm.setEndDate(dateFormat.format(ad.getEndDate()));
		}
		adForm.setFloorLevel(ad.getFloorLevel());
		adForm.setGarage(ad.getGarage());
		adForm.setIncreaseBidPrice(ad.getIncreaseBidPrice());
		adForm.setInfrastructureType(ad.getInfrastructureType());
		if (ad.getMoveInDate() != null){
			adForm.setMoveInDate(dateFormat.format(ad.getMoveInDate()));
		}
		adForm.setNumberOfBath(ad.getNumberOfBath());
		adForm.setNumberOfRooms(ad.getNumberOfRooms());
		adForm.setParking(ad.getParking());
		adForm.setPrice(ad.getPrice());
		adForm.setRenovationYear(ad.getRenovationYear());
		adForm.setRoomDescription(ad.getRoomDescription());
		adForm.setSquareFootage(ad.getSquareFootage());
		if (ad.getStartDate() != null){
			adForm.setStartDate(dateFormat.format(ad.getStartDate()));
		}
		adForm.setStartPrice(ad.getStartPrice());
		adForm.setStreet(ad.getStreet());
		adForm.setTitle(ad.getTitle());
		adForm.setType(ad.getType());

		return adForm;
	}
	
	private LatLng getCoordinates(String address) {
		try {
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest =
					new GeocoderRequestBuilder()
					.setAddress(address)
					.setLanguage("en")
					.getGeocoderRequest();
			
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

			List<GeocoderResult> results = geocoderResponse.getResults();
			if (results != null && !results.isEmpty()) {
				GeocoderResult result = results.get(0);
				if (!result.isPartialMatch()) {
					return result.getGeometry().getLocation();
				}
			}
		} catch (Exception ex) {
			logger.error(String.format("Failed to connect to GoogleService. Exception: %s", ex.getMessage()));
		}

		logger.warn(String.format("Failed to get coordinates from Service. Address: %s", address));
		return null;
	
	}

}
