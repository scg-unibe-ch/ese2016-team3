package ch.unibe.ese.team3.controller.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jolbox.bonecp.BoneCPDataSource;

import ch.unibe.ese.team3.base.BaseService;
import ch.unibe.ese.team3.dto.Location;

/**
 * Provides read access to the geo db. Performs the reading operations manually,
 * no ORM is involved.
 */
@Service
public class GeoDataService extends BaseService {

	@Autowired
	private BoneCPDataSource mainDataSource;

	/**
	 * Returns a list of all locations in the database.
	 */
	public List<Location> getAllLocations() {		
		PreparedStatement statement = null;

		try {
			statement = getConnection().prepareStatement(
					"SELECT zip.zip , zip.location, zip.lat, zip.lon, zip.department FROM `zipcodes` zip");
			return executeQuery(statement);
		} catch (SQLException ex) {
			logger.error(String.format("Failed to get all locations from DB. Message: %s", ex.getMessage()));
		} finally {
			if (statement != null) {
				try {
					statement.getConnection().close();
				} catch (SQLException e) {
				}
			}
		}
		return new ArrayList<Location>();
	}

	/**
	 * Gets all locations that match the given city. The locations are ordered
	 * in ascending order in relation to the zip code.
	 * 
	 * @param city
	 *            the city to look for
	 * @return a list of all locations that match the given city
	 */
	public List<Location> getLocationsByCity(String city) {
		PreparedStatement statement = null;

		try {
			statement = getConnection().prepareStatement(
					"SELECT zip.zip , zip.location, zip.lat, zip.lon, zip.department FROM `zipcodes` zip WHERE location = ?");
			statement.setString(1, city);
			return executeQuery(statement);
		} catch (SQLException ex) {
			logger.error(String.format("Failed to get locations by city '%s' from DB. Message: %s", city, ex.getMessage()));
		} finally {
			if (statement != null) {
				try {
					statement.getConnection().close();
				} catch (SQLException e) {
				}
			}
		}
		return new ArrayList<Location>();
	}

	/**
	 * Gets all locations that have the given zipcode.
	 * 
	 * @param zipcode
	 *            the zipcode to search for
	 * @return a list of all locations that match
	 */
	public List<Location> getLocationsByZipcode(int zipcode) {

		PreparedStatement statement = null;

		try {
			statement = getConnection().prepareStatement(
					"SELECT zip.zip, zip.location, zip.lat, zip.lon, zip.department FROM `zipcodes` zip WHERE zip = ?");
			statement.setInt(1, zipcode);
			return executeQuery(statement);
		} catch (SQLException ex) {
			logger.error(String.format("Failed to get locations by zipcode %d from DB. Message: %s", ex.getMessage()));
		} finally {
			if (statement != null) {
				try {
					statement.getConnection().close();
				} catch (SQLException e) {
				}
			}
		}
		return new ArrayList<Location>();
	}

	private List<Location> executeQuery(PreparedStatement statement) throws SQLException {
		ResultSet resultSet = null;
		List<Location> locationList = new ArrayList<>();
		resultSet = statement.executeQuery();
		locationList = writeResultSet(resultSet, locationList);
		return locationList;
	}

	/**
	 * Fills the given list with the results from resultSet.
	 */
	private List<Location> writeResultSet(ResultSet resultSet, List<Location> locationList) throws SQLException {
		while (resultSet.next()) {
			int zip = resultSet.getInt("zip");
			String city = resultSet.getString("location");
			double latitude = resultSet.getDouble("lat");
			double longitude = resultSet.getDouble("lon");
			String department = resultSet.getString("department");

			Location location = new Location();
			location.setZip(zip);
			location.setCity(city);
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			location.setDepartment(department);

			locationList.add(location);
		}

		return locationList;
	}

	private Connection getConnection() throws SQLException {
		return mainDataSource.getConnection();
	}
}
