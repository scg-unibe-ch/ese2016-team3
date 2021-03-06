package ch.unibe.ese.team3.controller.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.code.geocoder.model.LatLng;

import ch.unibe.ese.team3.dto.Location;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC_test.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
@Transactional
public class GeoDataServiceTest {
	
	private List<Location> locations;

	@Autowired
	private GeoDataService geoDataService;

	@Test
	public void getAllLocations() {
		locations = geoDataService.getAllLocations();
		assertEquals(5000, locations.get(0).getZip());
		assertEquals(3418, locations.size());
	}
	
	@Test
	public void getSpecificLocationByCity(){
		locations = geoDataService.getLocationsByCity("Aarau");
		assertEquals(3, locations.size());
		assertEquals(5000, locations.get(0).getZip());
		assertEquals(5001, locations.get(1).getZip());
		assertEquals(5004, locations.get(2).getZip());
	}
	
	@Test
	public void getSpecificLocationByZip(){
		locations = geoDataService.getLocationsByZipcode(5600);
		assertEquals(2, locations.size());
		assertEquals("Lenzburg", locations.get(0).getCity());
		assertEquals("Ammerswil", locations.get(1).getCity());
	}
	
	@Test
	public void getSpecificLocationByCityWithUmlaut(){
		locations = geoDataService.getLocationsByCity("Abländschen");
		assertEquals(1, locations.size());
	}
	
	@Test
	public void getSpecificLocationByCitySpecialChar(){
		locations = geoDataService.getLocationsByCity("L'Abbaye");
		assertEquals(1, locations.size());
	}
	
	@Test
	public void getSpecificLocationByCityNoCity(){
		locations = geoDataService.getLocationsByCity("Niemandshausen");
		assertEquals(0, locations.size());
	}
	
	@Test
	public void getSpecficiLocationByZipNoCity(){
		locations = geoDataService.getLocationsByZipcode(9999);
		assertEquals(0, locations.size());
	}
	
	@Test
	public void getInvalidCoordinates(){
		LatLng result = geoDataService.getCoordinates("not a valid address");
		assertEquals(null, result);
	}
	
	@Test
	public void getValidCoordinates(){
		LatLng result = geoDataService.getCoordinates("Sidlerstrasse 5 3000 Bern");
		assertEquals(46.9513886, result.getLat().doubleValue(), 0.00001);
		assertEquals(7.43860601, result.getLng().doubleValue(), 0.00001);
	}
}
