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

import ch.unibe.ese.team3.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team3.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team3.dto.Location;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.util.PremiumAdComparator;

/** Handles all persistence operations concerning ad placement and retrieval. */
@Service
public class AdService {

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
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths, User user) {

		Ad ad = new Ad();

		Date now = new Date();
		ad.setCreationDate(now);

		ad.setTitle(placeAdForm.getTitle());

		ad.setStreet(placeAdForm.getStreet());

		ad.setType(placeAdForm.getType());

		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));

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
// This causes java.lang.NullPointerException when Ad is placed
		// this is for auction
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		try {
			if (placeAdForm.getStartDate().length() >= 1) {
				int dayStart = Integer.parseInt(placeAdForm.getStartDate().substring(0, 2));
				int monthStart = Integer.parseInt(placeAdForm.getStartDate().substring(3, 5));
				int yearStart = Integer.parseInt(placeAdForm.getStartDate().substring(6, 10));
				calendar.set(yearStart, monthStart - 1, dayStart);
				ad.setStartDate(calendar.getTime());
			}

			if (placeAdForm.getEndDate().length() >= 1) {
				int dayEnd = Integer.parseInt(placeAdForm.getEndDate().substring(0, 2));
				int monthEnd = Integer.parseInt(placeAdForm.getEndDate().substring(3, 5));
				int yearEnd = Integer.parseInt(placeAdForm.getEndDate().substring(6, 10));
				calendar.set(yearEnd, monthEnd - 1, dayEnd);
				ad.setEndDate(calendar.getTime());
			}
		} catch (NumberFormatException e) {
		}

		// for auction
		ad.setStartPrice(placeAdForm.getStartPrice());
		ad.setBuyItNowPrice(placeAdForm.getBuyItNowPrice());
		ad.setIncreaseBidPrice(placeAdForm.getIncreaseBidPrice());
		ad.setcurrentAuctionPrice(placeAdForm.getStartPrice() + placeAdForm.getIncreaseBidPrice());
		ad.setAuction(placeAdForm.getAuction());
		
		

		ad.setPrizePerMonth(placeAdForm.getPrize());
		ad.setSquareFootage(placeAdForm.getSquareFootage());
		ad.setDistanceSchool(placeAdForm.getDistanceSchool());
		ad.setDistanceShopping(placeAdForm.getDistanceShopping());
		ad.setDistancePublicTransport(placeAdForm.getDistancePublicTransport());
		ad.setBuildYear(placeAdForm.getBuildYear());
		ad.setRenovationYear(placeAdForm.getRenovationYear());
		ad.setNumberOfRooms(placeAdForm.getNumberOfRooms());
		ad.setNumberOfBath(placeAdForm.getNumberOfBath());
		ad.setParking(placeAdForm.isParking());
		ad.setDishwasher(placeAdForm.getDishwasher());
		ad.setRoomDescription(placeAdForm.getRoomDescription());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setGarage(placeAdForm.getGarage());

		// new description variables
		ad.setElevator(placeAdForm.isElevator());
		ad.setInfrastructureType(placeAdForm.getInfrastructureType());
		ad.setFloorLevel(placeAdForm.getFloorLevel());

		/*
		 * Save the paths to the picture files, the pictures are assumed to be
		 * uploaded at this point!
		 */
		List<AdPicture> pictures = new ArrayList<>();
		for (String filePath : filePaths) {
			AdPicture picture = new AdPicture();
			picture.setFilePath(filePath);
			pictures.add(picture);
		}
		ad.setPictures(pictures);

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
			ad.setVisits(visits);
		}

		ad.setUser(user);

		adDao.save(ad);

		return ad;
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
	public Iterable<Ad> getNewestAds(int newest) {
		Iterable<Ad> allAds = adDao.findAll();
		List<Ad> ads = new ArrayList<Ad>();
		for (Ad ad : allAds)
			ads.add(ad);
		Collections.sort(ads, new Comparator<Ad>() {
			@Override
			public int compare(Ad ad1, Ad ad2) {
				return ad2.getCreationDate().compareTo(ad1.getCreationDate());
			}
		});
		List<Ad> fourNewest = new ArrayList<Ad>();
		for (int i = 0; i < newest; i++)
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
	public Iterable<Ad> queryResults(SearchForm searchForm) {
		Iterable<Ad> results = null;

		if (searchForm.getTypes() != null && searchForm.getTypes().length > 0) {
			results = adDao.findByPrizePerMonthLessThanAndTypeIn(searchForm.getPrize() + 1, searchForm.getTypes());
		}
		else {
			results = adDao.findByPrizePerMonthLessThan(searchForm.getPrize() + 1);
		}

		// filter out zipcode
		String city = searchForm.getCity().substring(7);

		// get the location that the user searched for and take the one with the
		// lowest zip code
		Location searchedLocation = geoDataService.getLocationsByCity(city).get(0);

		// create a list of the results and of their locations
		List<Ad> locatedResults = new ArrayList<>();
		for (Ad ad : results) {
			locatedResults.add(ad);
		}

		final int earthRadiusKm = 6380;
		List<Location> locations = geoDataService.getAllLocations();
		double radSinLat = Math.sin(Math.toRadians(searchedLocation.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(searchedLocation.getLatitude()));
		double radLong = Math.toRadians(searchedLocation.getLongitude());

		/*
		 * calculate the distances (Java 8) and collect all matching zipcodes.
		 * The distance is calculated using the law of cosines.
		 * http://www.movable-type.co.uk/scripts/latlong.html
		 */
		List<Integer> zipcodes = locations.parallelStream().filter(location -> {
			double radLongitude = Math.toRadians(location.getLongitude());
			double radLatitude = Math.toRadians(location.getLatitude());
			double distance = Math.acos(radSinLat * Math.sin(radLatitude)
					+ radCosLat * Math.cos(radLatitude) * Math.cos(radLong - radLongitude)) * earthRadiusKm;
			return distance < searchForm.getRadius();
		}).map(location -> location.getZip()).collect(Collectors.toList());

		locatedResults = locatedResults.stream().filter(ad -> zipcodes.contains(ad.getZipcode()))
				.collect(Collectors.toList());

		// filter for additional criteria
		if (searchForm.getFiltered()) {
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

			// --------------------
			// added search logic

			// filter based on number of baths. Search Results are removed, if
			// the number of baths of the ad is
			// smaller than the desired number of baths in the searchForm
			Iterator<Ad> iterator = locatedResults.iterator();
			while (iterator.hasNext()) {
				Ad ad = iterator.next();
				if (ad.getNumberOfBath() < searchForm.getNumberOfBathMin()
						|| ad.getNumberOfBath() > searchForm.getNumberOfBathMax())
					iterator.remove();

				// filter for size
				if (ad.getSquareFootage() < searchForm.getSquareFootageMin()
						|| ad.getSquareFootage() > searchForm.getSquareFootageMax())
					iterator.remove();

				// filter for infrastructureType

				// filter for number of Rooms
				if (ad.getNumberOfRooms() < searchForm.getNumberOfBathMin()
						|| ad.getNumberOfRooms() > searchForm.getNumberOfRoomsMax())
					iterator.remove();

				// filter for floorLevel
				if (ad.getFloorLevel() < searchForm.getFloorLevelMin()
						|| ad.getFloorLevel() > searchForm.getFloorLevelMax())
					iterator.remove();

				// filter for distance to school
				if (ad.getDistanceSchool() < searchForm.getDistanceSchoolMin()
						|| ad.getDistanceSchool() > searchForm.getDistanceSchoolMax())
					iterator.remove();

				// filter for distance to shopping center
				if (ad.getDistanceShopping() < searchForm.getDistanceShoppingMin()
						|| ad.getDistanceShopping() > searchForm.getDistanceShoppingMax())
					iterator.remove();

				// filter for distance to public transport
				if (ad.getDistancePublicTransport() < searchForm.getDistancePublicTransportMin()
						|| ad.getDistancePublicTransport() > searchForm.getDistancePublicTransportMax())
					iterator.remove();
				
				// filter for infrastructureType
				if (!ad.getInfrastructureType().equals(searchForm.getInfrastructureType()))
					iterator.remove();
			}
		}
		
		locatedResults.sort(new PremiumAdComparator());
		
		return locatedResults;
	}
	
	private List<Ad> validateDate(List<Ad> ads, boolean inOrOut, Date earliestDate, Date latestDate) {
		if (ads.size() > 0) {
			// Move-in dates
			// Both an earliest AND a latest date to compare to
			if (earliestDate != null) {
				if (latestDate != null) {
					Iterator<Ad> iterator = ads.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getMoveInDate().compareTo(earliestDate) < 0
								|| ad.getMoveInDate().compareTo(latestDate) > 0) {
							iterator.remove();
						}
					}
				}
				// only an earliest date
				else {
					Iterator<Ad> iterator = ads.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getMoveInDate().compareTo(earliestDate) < 0)
							iterator.remove();
					}
				}
			}
			// only a latest date
			else if (latestDate != null && earliestDate == null) {
				Iterator<Ad> iterator = ads.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getMoveInDate().compareTo(latestDate) > 0)
						iterator.remove();
				}
			} else {
			}
		}
		return ads;
	}

	/** Returns all ads that were placed by the given user. */
	public Iterable<Ad> getAdsByUser(User user) {
		return adDao.findByUser(user);
	}

	/**
	 * Checks if the email of a user is already contained in the given string.
	 * 
	 * @param email
	 *            the email string to search for
	 * @param alreadyAdded
	 *            the string of already added emails, which should be searched
	 *            in
	 * 
	 * @return true if the email has been added already, false otherwise
	 */
	public Boolean checkIfAlreadyAdded(String email, String alreadyAdded) {
		email = email.toLowerCase();
		alreadyAdded = alreadyAdded.replaceAll("\\s+", "").toLowerCase();
		String delimiter = "[:;]+";
		String[] toBeTested = alreadyAdded.split(delimiter);
		for (int i = 0; i < toBeTested.length; i++) {
			if (email.equals(toBeTested[i])) {
				return true;
			}
		}
		return false;
	}
}