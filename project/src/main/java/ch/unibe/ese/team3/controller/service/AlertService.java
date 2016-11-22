package ch.unibe.ese.team3.controller.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.controller.pojos.forms.AlertForm;
import ch.unibe.ese.team3.dto.Location;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.Alert;
import ch.unibe.ese.team3.model.AlertResult;
import ch.unibe.ese.team3.model.AlertType;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Message;
import ch.unibe.ese.team3.model.MessageState;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AlertDao;
import ch.unibe.ese.team3.model.dao.AlertResultDao;
import ch.unibe.ese.team3.model.dao.MessageDao;
import ch.unibe.ese.team3.model.dao.UserDao;

/**
 * Provides and handles persistence operations for adding, editing and deleting
 * alerts.
 */
@Service
public class AlertService {

	@Autowired
	UserDao userDao;

	@Autowired
	AlertDao alertDao;

	@Autowired
	MessageDao messageDao;

	@Autowired
	AlertResultDao alertResultDao;

	@Autowired
	private GeoDataService geoDataService;

	@Autowired
	private MessageService messageService;

	/**
	 * Persists a new alert with the data from the alert form to the database.
	 * 
	 * @param alertForm
	 *            the form to take the data from
	 * @param user
	 *            the user to associate the new alert to
	 */
	@Transactional
	public void saveFrom(AlertForm alertForm, User user) {
		Alert alert = new Alert();

		String zip = alertForm.getCity().substring(0, 4);
		alert.setZipcode(Integer.parseInt(zip));
		alert.setCity(alertForm.getCity().substring(7));
		alert.setBuyMode(alertForm.getBuyMode());
		alert.setPrice(alertForm.getPrice());
		alert.setExtendedAlert(alertForm.isExtendedAlert());

		alert.setRadius(alertForm.getRadius());

		int countAlertTypes = 0;
		List<AlertType> alertTypes = new ArrayList<AlertType>();
		for (Type type : alertForm.getTypes()) {
			AlertType alertType = new AlertType();
			alertType.setType(type);
			alertType.setAlert(alert);
			alertTypes.add(alertType);
			countAlertTypes++;
		}

		// if there are no alertTypes specified in the form, the alert searches for all alertTypes
		if (countAlertTypes == 0) {
			setAllAlertTypes(alert);
		} else {
			alert.setAlertTypes(alertTypes);
		}

		alert.setUser(user);

		// Add extended Alert criteria only if extendedAlert = true
		if (alert.isExtendedAlert()) {
			Date earliestDate = convertStringToDate(alertForm.getEarliestMoveInDate());
			Date latestDate = convertStringToDate(alertForm.getLatestMoveInDate());
			alert.setEarliestMoveInDate(earliestDate);
			alert.setLatestMoveInDate(latestDate);

			alert.setBalcony(alertForm.isBalcony());
			alert.setParking(alertForm.isParking());
			alert.setElevator(alertForm.isElevator());
			alert.setGarage(alertForm.isGarage());
			alert.setDishwasher(alertForm.isDishwasher());

			alert.setInfrastructureType(alertForm.getInfrastructureType());
			alert.setSquareFootageMin(alertForm.getSquareFootageMin());
			alert.setSquareFootageMax(alertForm.getSquareFootageMax());

			alert.setBuildYearMin(alertForm.getBuildYearMin());
			alert.setBuildYearMax(alertForm.getBuildYearMax());

			alert.setRenovationYearMin(alertForm.getRenovationYearMin());
			alert.setRenovationYearMax(alertForm.getRenovationYearMax());

			alert.setNumberOfRoomsMin(alertForm.getNumberOfRoomsMin());
			alert.setNumberOfRoomsMax(alertForm.getNumberOfRoomsMax());

			alert.setNumberOfBathMin(alertForm.getNumberOfBathMin());
			alert.setNumberOfBathMax(alertForm.getNumberOfBathMax());

			alert.setDistanceSchoolMin(alertForm.getDistanceSchoolMin());
			alert.setDistanceSchoolMax(alertForm.getDistanceSchoolMax());

			alert.setDistanceShoppingMin(alertForm.getDistanceShoppingMin());
			alert.setDistanceShoppingMax(alertForm.getDistanceShoppingMax());

			alert.setDistancePublicTransportMin(alertForm.getDistancePublicTransportMin());
			alert.setDistancePublicTransportMax(alertForm.getDistancePublicTransportMax());

			alert.setFloorLevelMin(alertForm.getFloorLevelMin());
			alert.setFloorLevelMax(alertForm.getFloorLevelMax());
		}
		alertDao.save(alert);
	}
	
	

	private Date convertStringToDate(String date) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date earliestMoveInDate = formatter.parse(date);
			return earliestMoveInDate;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Returns all alerts that belong to the given user.
	 */
	@Transactional
	public Iterable<Alert> getAlertsByUser(User user) {
		return alertDao.findByUser(user);
	}

	/** Deletes the alert with the given id. */
	@Transactional
	public void deleteAlert(Long id) {
		alertDao.delete(id);
	}

	/**
	 * Triggers all alerts that match the given ad. For every user, only one
	 * message is sent.
	 */
	@Transactional
	public void triggerAlerts(Ad ad) {
		int adPrice = ad.getPrice();
		BuyMode buyMode = ad.getBuyMode();
		Iterable<Alert> alerts = alertDao.findByPriceGreaterThanAndBuyMode(adPrice - 1, buyMode);

		// loop through all ads with matching city and price range, throw out
		// mismatches
		Iterator<Alert> alertIterator = alerts.iterator();
		while (alertIterator.hasNext()) {
			Alert alert = alertIterator.next();
			if (typeMismatchWith(ad, alert) || radiusMismatchWith(ad, alert) || ad.getUser().equals(alert.getUser()))
				alertIterator.remove(); // this also changes the number of
										// elements in "alerts"
		}

		filterWithExtendedCriteria(ad, alerts);

		// send only one message per user, no matter how many alerts were
		// triggered
		List<User> users = new ArrayList<User>();

		for (Alert alert : alerts) {
			User user = alert.getUser();
			if (!users.contains(user)) {
				users.add(user);
			}
		}

		// save triggered alerts into database
		for (User user : users) {
			Date now = new Date();
			AlertResult alertResult = new AlertResult();
			if (user.isPremium()) {
				alertResult.setNotified(true);
			} else {
				alertResult.setNotified(false);
			}
			alertResult.setTriggerAd(ad);
			alertResult.setTriggerDate(now);
			alertResult.setUser(user);
			alertResultDao.save(alertResult);
		}
		// send messages to all users with matching alerts
		for (User user : users) {
			if (user.isPremium()) {
				Date now = new Date();
				Message message = new Message();
				message.setSubject("It's a match!");
				message.setText(getAlertText(ad));
				message.setSender(userDao.findByUsername("System"));
				message.setRecipient(user);
				message.setState(MessageState.UNREAD);
				message.setDateSent(now);
				messageDao.save(message);
				messageService.sendEmail(user, "It's a match!", getAlertText(ad));
			}
		}
	}

	/**
	 * Returns the text for an alert message with the properties of the given
	 * ad.
	 */
	private String getAlertText(Ad ad) {
		return "Dear user,<br>good news. A new ad matching one of your alerts has been "
				+ "entered into our system. You can visit it here:<br><br>" + "<a class=\"link\" href=/ad?id="
				+ ad.getId() + ">" + ad.getTitle() + "</a><br><br>" + "Good luck and enjoy,<br>"
				+ "Your FlatFindr crew";
	}

	/**
	 * Checks if an ad is conforming to the criteria in an alert. Return false
	 * if the type of Ad is within the types within Alert list
	 */
	private boolean typeMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = true;
		List<AlertType> alertTypes = alert.getAlertTypes();

		// iterates over each alertType and compares the type to the ad's type
		// if Ad type equals a type in alert, mismatch is false
		for (AlertType alertType : alertTypes) {
			if (ad.getType().equals(alertType.getType()))
				mismatch = false;
		}
		return mismatch;
	}

	/**
	 * Removes the alerts, which do not match with the placed ad, based on the
	 * extended alert criteria
	 * 
	 * @param alerts
	 */
	private void filterWithExtendedCriteria(Ad ad, Iterable<Alert> alerts) {

		filterByDate(alerts, ad);
		filterBinaryCriteria(ad, alerts);
		filterNumberCriteria(ad, alerts);
		filterInfrastructureType(ad, alerts);
	}

	private void filterNumberCriteria(Ad ad, Iterable<Alert> alerts) {
		Iterator<Alert> iterator = alerts.iterator(); // a new iterator has to
														// be created, when the
														// iteration should
														// start anew (this
														// resets the cursor)
		while (iterator.hasNext()) {
			Alert alert = iterator.next();
			Integer minBath = convertToNullableInt(alert.getNumberOfBathMin());
			Integer maxBath = convertToNullableInt(alert.getNumberOfBathMax());
			Integer minSquareFootage = convertToNullableInt(alert.getSquareFootageMin());
			Integer maxSquareFootage = convertToNullableInt(alert.getSquareFootageMax());
			Integer minNumberOfRooms = convertToNullableInt(alert.getNumberOfRoomsMin());
			Integer maxNumberOfRooms = convertToNullableInt(alert.getNumberOfRoomsMax());
			Integer minFloorLevel = convertToNullableInt(alert.getFloorLevelMin());
			Integer maxFloorLevel = convertToNullableInt(alert.getFloorLevelMax());
			Integer minDistanceSchool = convertToNullableInt(alert.getDistanceSchoolMin());
			Integer maxDistanceSchool = convertToNullableInt(alert.getDistanceSchoolMax());
			Integer minDistanceShopping = convertToNullableInt(alert.getDistanceShoppingMin());
			Integer maxDistanceShopping = convertToNullableInt(alert.getDistanceShoppingMax());
			Integer minDistancePublicTransport = convertToNullableInt(alert.getDistancePublicTransportMin());
			Integer maxDistancePublicTransport = convertToNullableInt(alert.getDistancePublicTransportMax());

			Integer minBuildYear = convertToNullableInt(alert.getBuildYearMin());
			Integer maxBuildYear = convertToNullableInt(alert.getBuildYearMax());
			Integer minRenovationYear = convertToNullableInt(alert.getRenovationYearMin());
			Integer maxRenovationYear = convertToNullableInt(alert.getRenovationYearMax());

			if (!inRange(minBath, maxBath, ad.getNumberOfBath())
					|| !inRange(minSquareFootage, maxSquareFootage, ad.getSquareFootage())
					|| !inRange(minNumberOfRooms, maxNumberOfRooms, ad.getNumberOfRooms())
					|| !inRange(minFloorLevel, maxFloorLevel, ad.getFloorLevel())
					|| !inRange(minDistanceSchool, maxDistanceSchool, ad.getDistanceSchool())

					|| !inRange(minBuildYear, maxBuildYear, ad.getBuildYear())
					|| !inRange(minRenovationYear, maxRenovationYear, ad.getRenovationYear())

					|| !inRange(minDistanceShopping, maxDistanceShopping, ad.getDistanceShopping())
					|| !inRange(minDistancePublicTransport, maxDistancePublicTransport,
							ad.getDistancePublicTransport())) {
				iterator.remove();
			}
		}
	}

	private void filterInfrastructureType(Ad ad, Iterable<Alert> alerts) {
		Iterator<Alert> iterator = alerts.iterator();
		InfrastructureType infraType = ad.getInfrastructureType();

		if (infraType != null) {
			while (iterator.hasNext()) {
				Alert alert = iterator.next();
				if (!(alert.getInfrastructureType() == null)) {
					if (!alert.getInfrastructureType().equals(infraType)) {
						iterator.remove();
					}
				}
			}
		}
	}

	private void filterBinaryCriteria(Ad ad, Iterable<Alert> alerts) {
		Iterator<Alert> iterator = alerts.iterator(); // a new iterator has to
														// be created, when the
														// iteration should
														// start anew (this
														// resets the cursor)
		while (iterator.hasNext()) {
			Alert alert = iterator.next();
			if (alert.isDishwasher()) {
				if (!ad.getDishwasher()) {
					iterator.remove();
				}
			}
		}

		iterator = alerts.iterator();
		while (iterator.hasNext()) {
			Alert alert = iterator.next();
			if (alert.isElevator()) {
				if (!ad.getElevator()) {
					iterator.remove();
				}
			}
		}

		iterator = alerts.iterator();
		while (iterator.hasNext()) {
			Alert alert = iterator.next();
			if (alert.isBalcony()) {
				if (!ad.getBalcony()) {
					iterator.remove();
				}
			}
		}

		iterator = alerts.iterator();
		while (iterator.hasNext()) {
			Alert alert = iterator.next();
			if (alert.isGarage()) {
				if (!ad.getGarage()) {
					iterator.remove();
				}
			}
		}

		iterator = alerts.iterator();
		while (iterator.hasNext()) {
			Alert alert = iterator.next();
			if (alert.isParking()) {
				if (!ad.getParking()) {
					iterator.remove();
				}
			}
		}
	}

	private Integer convertToNullableInt(int value) {
		return value > 0 ? value : null;
	}

	private boolean inRange(Integer min, Integer max, int value) {
		return (min == null || value >= min) && (max == null || value <= max);
	}

	/**
	 * Checks whether an ad is for a place too far away from the alert.
	 * 
	 * @param ad
	 *            the ad to compare to the alert
	 * @param alert
	 *            the alert to compare to the ad
	 * 
	 * @return true in case the alert does not match the ad (the ad is too far
	 *         away), false otherwise
	 */
	private boolean radiusMismatchWith(Ad ad, Alert alert) {
		final int earthRadiusKm = 6380;
		Location adLocation = geoDataService.getLocationsByCity(ad.getCity()).get(0);
		Location alertLocation = geoDataService.getLocationsByCity(alert.getCity()).get(0);

		double radSinLat = Math.sin(Math.toRadians(adLocation.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(adLocation.getLatitude()));
		double radLong = Math.toRadians(adLocation.getLongitude());
		double radLongitude = Math.toRadians(alertLocation.getLongitude());
		double radLatitude = Math.toRadians(alertLocation.getLatitude());
		double distance = Math.acos(radSinLat * Math.sin(radLatitude)
				+ radCosLat * Math.cos(radLatitude) * Math.cos(radLong - radLongitude)) * earthRadiusKm;
		return (distance > alert.getRadius());
	}

	private void filterByDate(Iterable<Alert> alerts, Ad ad) {
		assert ad.getMoveInDate() != null;

		// count alerts
		Iterator<Alert> iterator = alerts.iterator();
		int countAlerts = 0;

		while (iterator.hasNext()) {
			iterator.next();
			countAlerts++;
		}

		// reset iterator
		iterator = alerts.iterator();

		if (countAlerts > 0) {
			iterator = alerts.iterator(); // reset cursor of iterator

			while (iterator.hasNext()) {
				Alert alert = iterator.next();
				// iterator.next();

				Date earliestDate = alert.getEarliestMoveInDate();
				Date latestDate = alert.getLatestMoveInDate();

				if (!((earliestDate == null || earliestDate.compareTo(ad.getMoveInDate()) <= 0)
						&& (latestDate == null || latestDate.compareTo(ad.getMoveInDate()) >= 0))) {
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * Adds all AlertTypes to an alert.
	 * @param alert
	 */
	private void setAllAlertTypes(Alert alert) {
		List<Type> allTypes = new ArrayList<Type>();
		allTypes.add(Type.APARTMENT);
		allTypes.add(Type.LOFT);
		allTypes.add(Type.STUDIO);
		allTypes.add(Type.VILLA);
		allTypes.add(Type.HOUSE);

		List<AlertType> listAllAlertTypes = new ArrayList<AlertType>();
		for (Type type : allTypes) {
			AlertType alertType = new AlertType();
			alertType.setType(type);
			alertType.setAlert(alert);
			listAllAlertTypes.add(alertType);
		}
		alert.setAlertTypes(listAllAlertTypes);
	}
	
	// for testing
	public boolean radiusMismatch(Ad ad, Alert alert) {
		return radiusMismatchWith(ad, alert);
	}

	// for testing
	public boolean typeMismatch(Ad ad, Alert alert) {
		return typeMismatchWith(ad, alert);
	}
}