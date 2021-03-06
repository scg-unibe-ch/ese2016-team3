package ch.unibe.ese.team3.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.geocoder.model.LatLng;

import ch.unibe.ese.team3.base.BaseService;
import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team3.dto.Location;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.util.PremiumAdComparator;

/** Handles all persistence operations concerning ad placement and retrieval. */
@Service
public class AdService extends BaseService {

	@Autowired
	private AdDao adDao;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Handles persisting a new ad to the database.
	 * 
	 * @param placeAdForm
	 *            the form to take the data from
	 * @param a
	 *            list of the file paths the pictures are saved under
	 * @param the
	 *            currently logged in user
	 */
	@Transactional
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths, User user, BuyMode buyMode) {

		Ad ad = new Ad();
		ad.setBuyMode(buyMode);
		
		Date now = new Date();
		ad.setCreationDate(now);

		
		ad.setTitle(placeAdForm.getTitle());
		ad.setPrice(placeAdForm.getPriceForAd());
		ad.setSquareFootage(placeAdForm.getSquareFootage());
		ad.setNumberOfRooms(placeAdForm.getNumberOfRooms());
		ad.setType(placeAdForm.getType());
		
		ad.setStreet(placeAdForm.getStreet());
		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));
		String addressString = String.format("%s %s %s", placeAdForm.getStreet(), zip,
				placeAdForm.getCity().substring(7));

		LatLng coordinates = geoDataService.getCoordinates(addressString);
		if (coordinates != null) {
			ad.setLatitude(coordinates.getLat());
			ad.setLongitude(coordinates.getLng());
		}

		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		if (placeAdForm.getMoveInDate().length() >= 1) {
			int dayMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(0, 2));
			int monthMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(3, 5));
			int yearMoveIn = Integer.parseInt(placeAdForm.getMoveInDate().substring(6, 10));
			calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn, 0, 0, 0);
			ad.setMoveInDate(calendar.getTime());
		}
			
		ad.setDistanceSchool(placeAdForm.getDistanceSchool());
		ad.setDistanceShopping(placeAdForm.getDistanceShopping());
		ad.setDistancePublicTransport(placeAdForm.getDistancePublicTransport());
		ad.setBuildYear(placeAdForm.getBuildYear());
		ad.setRenovationYear(placeAdForm.getRenovationYear());
		ad.setNumberOfBath(placeAdForm.getNumberOfBath());
		ad.setParking(placeAdForm.isParking());
		ad.setDishwasher(placeAdForm.getDishwasher());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setGarage(placeAdForm.getGarage());
		ad.setElevator(placeAdForm.isElevator());
		ad.setInfrastructureType(placeAdForm.getInfrastructureType());
		ad.setFloorLevel(placeAdForm.getFloorLevel());
		
		setAuctionFields(placeAdForm, ad);
		
		ad.setRoomDescription(placeAdForm.getRoomDescription());
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



	private void setAuctionFields(PlaceAdForm placeAdForm, Ad ad) {
		Calendar calendar = Calendar.getInstance();
		String startDateAuction = placeAdForm.getStartDate();
		if (startDateAuction != null && startDateAuction.length() >= 1) {
			int dayStart = Integer.parseInt(startDateAuction.substring(0, 2));
			int monthStart = Integer.parseInt(startDateAuction.substring(3, 5));
			int yearStart = Integer.parseInt(startDateAuction.substring(6, 10));
			calendar.set(yearStart, monthStart - 1, dayStart);
			ad.setStartDate(calendar.getTime());	
		}

		String endDateAuction = placeAdForm.getEndDate();
		if (endDateAuction != null && endDateAuction.length() >= 1) {
			int dayEnd = Integer.parseInt(endDateAuction.substring(0, 2));
			int monthEnd = Integer.parseInt(endDateAuction.substring(3, 5));
			int yearEnd = Integer.parseInt(endDateAuction.substring(6, 10));
			calendar.set(yearEnd, monthEnd - 1, dayEnd);
			ad.setEndDate(calendar.getTime());
			}

		ad.setStartPrice(placeAdForm.getStartPrice());
		ad.setIncreaseBidPrice(placeAdForm.getIncreaseBidPrice());
		ad.setcurrentAuctionPrice(placeAdForm.getStartPrice() + placeAdForm.getIncreaseBidPrice());
		ad.setAuction(placeAdForm.getAuction());
		ad.setAuctionPrice(placeAdForm.getAuctionPrice());
	}
	


	/**
	 * Gets the ad that has the given id.
	 * 
	 * @param id
	 *            the id that should be searched for
	 * @return the found ad or null, if no ad with this id exists
	 */
	@Transactional
	public Ad getAdById(long id) {
		return adDao.findOne(id);
	}

	/** Returns all ads in the database */
	@Transactional
	public Iterable<Ad> getAllAds() {
		return adDao.findAll();
	}

	/**
	 * Returns the newest ads in the database. Parameter 'newest' says how many.
	 */
	@Transactional
	public Iterable<Ad> getNewestAds(int newest, BuyMode buyMode) {
		Iterable<Ad> allAds = adDao.findAll();
		List<Ad> ads = new ArrayList<Ad>();
		for (Ad ad : allAds) {
			if (ad.getBuyMode() == buyMode ) {
					ads.add(ad);
			}
		}
		
		ads = removeExpiredAuctions(ads);
		
		Collections.sort(ads, new Comparator<Ad>() {
			@Override
			public int compare(Ad ad1, Ad ad2) {
				return ad2.getCreationDate().compareTo(ad1.getCreationDate());
			}
		});
		List<Ad> fourNewest = new ArrayList<Ad>();

		int limit = newest <= ads.size() ? newest : ads.size();

		for (int i = 0; i < limit; i++)
			fourNewest.add(ads.get(i));
		return fourNewest;
	}
	

	/**
	 * Returns all ads that match the parameters given by the form. This list
	 * can possibly be empty.
	 * 
	 * @param searchForm
	 *            the form to take the search parameters from
	 * @return an Iterable of all search results
	 */
	@Transactional
	public Iterable<Ad> queryResults(SearchForm searchForm, BuyMode buyMode) {
		Iterable<Ad> results = null;
		if (searchForm.getTypes() != null && searchForm.getTypes().length > 0) {
			results = adDao.findByPriceLessThanAndTypeInAndBuyMode(searchForm.getPrice() + 1, searchForm.getTypes(),
					buyMode);
		} else {
			results = adDao.findByPriceLessThanAndBuyMode(searchForm.getPrice() + 1, buyMode);
		}

		// filter out zipcode
		String city = searchForm.getCity().substring(7);

		// create a list of the results and of their locations
		List<Ad> locatedResults = new ArrayList<>();
		for (Ad ad : results) {
			locatedResults.add(ad);
		}

		// get the location that the user searched for and take the one with the
		// lowest zip code
		List<Location> locations = geoDataService.getLocationsByCity(city);
		if (!locations.isEmpty()) {
			Location searchedLocation = locations.get(0);

			List<Integer> zipcodes = getZipCodesWithinRadius(searchedLocation, searchForm.getRadius());
			if (!zipcodes.isEmpty()) {
				locatedResults = locatedResults.stream().filter(ad -> zipcodes.contains(ad.getZipcode()))
						.collect(Collectors.toList());
			}
		}

		// filter for additional criteria

		// prepare date filtering - by far the most difficult filter
		Date earliestInDate = null;
		Date latestInDate = null;

		// parse move-in and move-out dates
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			earliestInDate = formatter.parse(searchForm.getEarliestMoveInDate());
		} catch (Exception e) {
		}
		try {
			latestInDate = formatter.parse(searchForm.getLatestMoveInDate());
		} catch (Exception e) {
		}

		// filtering by dates
		locatedResults = validateDate(locatedResults, true, earliestInDate, latestInDate);

		// filtering for the rest
		// dishwasher
		if (searchForm.getDishwasher()) {
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!ad.getDishwasher())
					iterator.remove();
			}
		}
		// elevator
		if (searchForm.getElevator()) {
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!ad.getElevator())
					iterator.remove();
			}
		}
		// parking
		if (searchForm.getParking()) {
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!ad.getParking())
					iterator.remove();
			}
		}

		// balcony
		if (searchForm.getBalcony()) {
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!ad.getBalcony())
					iterator.remove();
			}
		}

		// garage
		if (searchForm.getGarage()) {
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!ad.getGarage())
					iterator.remove();
			}
		}

		InfrastructureType infraType = searchForm.getInfrastructureType();
		if (infraType != null) {
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!ad.getInfrastructureType().equals(searchForm.getInfrastructureType())) {
					iterator.remove();
				}
			}
		}

		// --------------------
		// added search logic

		// filter based on number of baths. Search Results are removed, if
		// the number of baths of the ad is
		// smaller than the desired number of baths in the searchForm
		Iterator<Ad> iterator = locatedResults.iterator();
		while (iterator.hasNext()) {
			Ad ad = iterator.next();

			Integer minBath = convertToNullableInt(searchForm.getNumberOfBathMin());
			Integer maxBath = convertToNullableInt(searchForm.getNumberOfBathMax());
			Integer minSquareFootage = convertToNullableInt(searchForm.getSquareFootageMin());
			Integer maxSquareFootage = convertToNullableInt(searchForm.getSquareFootageMax());
			Integer minNumberOfRooms = convertToNullableInt(searchForm.getNumberOfRoomsMin());
			Integer maxNumberOfRooms = convertToNullableInt(searchForm.getNumberOfRoomsMax());
			Integer minFloorLevel = convertToNullableInt(searchForm.getFloorLevelMin());
			Integer maxFloorLevel = convertToNullableInt(searchForm.getFloorLevelMax());
			Integer minDistanceSchool = convertToNullableInt(searchForm.getDistanceSchoolMin());
			Integer maxDistanceSchool = convertToNullableInt(searchForm.getDistanceSchoolMax());
			Integer minDistanceShopping = convertToNullableInt(searchForm.getDistanceShoppingMin());
			Integer maxDistanceShopping = convertToNullableInt(searchForm.getDistanceShoppingMax());
			Integer minDistancePublicTransport = convertToNullableInt(searchForm.getDistancePublicTransportMin());
			Integer maxDistancePublicTransport = convertToNullableInt(searchForm.getDistancePublicTransportMax());

			Integer minBuildYear = convertToNullableInt(searchForm.getBuildYearMin());
			Integer maxBuildYear = convertToNullableInt(searchForm.getBuildYearMax());
			Integer minRenovationYear = convertToNullableInt(searchForm.getRenovationYearMin());
			Integer maxRenovationYear = convertToNullableInt(searchForm.getRenovationYearMax());
			
			if (!inRange(minBath, maxBath, ad.getNumberOfBath())
					|| !inRange(minSquareFootage, maxSquareFootage, ad.getSquareFootage())
					|| !inRange(minNumberOfRooms, maxNumberOfRooms, ad.getNumberOfRooms())
					|| !inRange(minFloorLevel, maxFloorLevel, ad.getFloorLevel())

					|| !inRange(minBuildYear, maxBuildYear, ad.getBuildYear())
					|| !inRange(minRenovationYear, maxRenovationYear, ad.getRenovationYear())

					|| !inRange(minDistanceSchool, maxDistanceSchool, ad.getDistanceSchool())
					|| !inRange(minDistanceShopping, maxDistanceShopping, ad.getDistanceShopping())
					|| !inRange(minDistancePublicTransport, maxDistancePublicTransport,
							ad.getDistancePublicTransport())) {
				iterator.remove();
			}
		}

		//removes all expired auctions
		locatedResults = removeExpiredAuctions(locatedResults);
		
		locatedResults.sort(new PremiumAdComparator());

		return locatedResults;
	}

	private List<Ad> removeExpiredAuctions(List<Ad> ads) {
		Iterator<Ad> itr = ads.iterator();
		while (itr.hasNext()) {
			Ad ad = itr.next();
			if(ad.isAuction()){
				if(ad.isAuctionCompleted())
					itr.remove();
				if(ad.hasAuctionExpired())
					itr.remove();
				
			}
		}	
		return ads;
	}
	private Integer convertToNullableInt(int value) {
		return value > 0 ? value : null;
	}

	private List<Integer> getZipCodesWithinRadius(Location searchedLocation, int radius) {
		final int earthRadiusKm = 6380;
		List<Location> allLocations = geoDataService.getAllLocations();
		double radSinLat = Math.sin(Math.toRadians(searchedLocation.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(searchedLocation.getLatitude()));
		double radLong = Math.toRadians(searchedLocation.getLongitude());

		/*
		 * calculate the distances (Java 8) and collect all matching zipcodes.
		 * The distance is calculated using the law of cosines.
		 * http://www.movable-type.co.uk/scripts/latlong.html
		 */
		List<Integer> zipcodes = allLocations.parallelStream().filter(location -> {
			double radLongitude = Math.toRadians(location.getLongitude());
			double radLatitude = Math.toRadians(location.getLatitude());
			double distance = Math.acos(radSinLat * Math.sin(radLatitude)
					+ radCosLat * Math.cos(radLatitude) * Math.cos(radLong - radLongitude)) * earthRadiusKm;
			return distance < radius;
		}).map(location -> location.getZip()).collect(Collectors.toList());

		return zipcodes;
	}

	private List<Ad> validateDate(List<Ad> ads, boolean inOrOut, Date earliestDate, Date latestDate) {
		if (ads.size() > 0) {

			Iterator<Ad> iterator = ads.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (!((earliestDate == null || earliestDate.compareTo(ad.getMoveInDate()) <= 0)
						&& (latestDate == null || latestDate.compareTo(ad.getMoveInDate()) >= 0))) {
					iterator.remove();
				}
			}
		}
		return ads;
	}

	private boolean inRange(Integer min, Integer max, int value) {
		return (min == null || value >= min) && (max == null || value <= max);
	}

	/** Returns all ads that were placed by the given user. */
	public Iterable<Ad> getAdsByUser(User user) {
		return adDao.findByUser(user);
	}
}