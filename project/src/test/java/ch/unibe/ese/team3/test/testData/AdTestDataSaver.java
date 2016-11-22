package ch.unibe.ese.team3.test.testData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.AdPicture;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.InfrastructureType;
import ch.unibe.ese.team3.model.Type;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.dao.AdDao;
import ch.unibe.ese.team3.model.dao.UserDao;

/** This inserts several ad elements into the database. */
@Service
public class AdTestDataSaver {

	@Autowired
	private AdDao adDao;
	@Autowired
	private UserDao userDao;

	@Transactional
	public void saveTestData() throws Exception {
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		User oprah = userDao.findByUsername("oprah@winfrey.com");
		User jane = userDao.findByUsername("jane@doe.com");

		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

		Date creationDate1 = formatter.parse("03.10.2014");
		Date creationDate2 = formatter.parse("13.11.2016");
		Date creationDate3 = formatter.parse("25.10.2014");
		Date creationDate4 = formatter.parse("05.11.2016");
		Date creationDate5 = formatter.parse("25.11.2013");
		Date creationDate6 = formatter.parse("01.12.2014");
		Date creationDate7 = formatter.parse("16.11.2014");

		Date creationDate8 = formatter.parse("27.11.2014");
		Date creationDate9 = formatter.parse("01.11.2016");

		Date moveInDate1 = formatter.parse("15.12.2014");
		Date moveInDate2 = formatter.parse("21.12.2014");
		Date moveInDate3 = formatter.parse("01.01.2015");
		Date moveInDate4 = formatter.parse("15.01.2015");
		Date moveInDate5 = formatter.parse("01.02.2015");
		Date moveInDate6 = formatter.parse("01.03.2015");
		Date moveInDate7 = formatter.parse("15.03.2015");
		Date moveInDate8 = formatter.parse("16.02.2015");
		Date moveInDate9 = formatter.parse("11.12.2016");

		Date startAuctionDate1 = formatter.parse("02.11.2016");

		Date endAuctionDate1 = formatter.parse("20.01.2017");

		String roomDescription1 = "The room is a part of 3.5 rooms apartment completely renovated"
				+ " in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ " Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ " transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ " charges are separate. If you are interested, feel free to drop me a message"
				+ " to have an appointment for a visit or can write me for any further information";

		Ad adBern = new Ad();
		adBern.setZipcode(3011);
		adBern.setType(Type.APARTMENT);
		adBern.setBuyMode(BuyMode.BUY);
		adBern.setMoveInDate(moveInDate1);
		adBern.setCreationDate(creationDate1);
		adBern.setPrice(100000);
		adBern.setSquareFootage(50);
		adBern.setNumberOfBath(1);
		adBern.setRoomDescription(roomDescription1);
		adBern.setUser(bernerBaer);
		adBern.setTitle("Roommate wanted in Bern");
		adBern.setStreet("Kramgasse 22");
		adBern.setCity("Bern");
		adBern.setLatitude(46.9479804);
		adBern.setLongitude(7.4509437);

		adBern.setBalcony(true);
		adBern.setGarage(true);
		adBern.setDishwasher(true);
		adBern.setElevator(false);
		adBern.setGarage(false);
		adBern.setBuildYear(1930);
		adBern.setRenovationYear(2000);
		adBern.setDistancePublicTransport(1000);
		adBern.setDistanceSchool(1000);
		adBern.setDistanceShopping(100);
		adBern.setFloorLevel(2);
		adBern.setNumberOfRooms(2);

		// set auction specific attributes
		adBern.setAuction(true);
		adBern.setStartDate(formatter.parse("03.11.2016")); // to houers !!
		adBern.setEndDate(formatter.parse("03.12.2016"));
		adBern.setStartPrice(70000);
		adBern.setIncreaseBidPrice(1000);
		adBern.setcurrentAuctionPrice(adBern.getStartPrice() + adBern.getIncreaseBidPrice());

		adBern.setInfrastructureType(InfrastructureType.CABLE);
		List<AdPicture> pictures = new ArrayList<>();
		pictures.add(createPicture(adBern, "/img/test/ad1_1.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_3.jpg"));
		adBern.getPictures().addAll(pictures);
		adDao.save(adBern);

		String studioDescription2 = "It is small studio close to the"
				+ " university and the bahnhof. The lovely neighbourhood"
				+ " Langgasse makes it an easy place to feel comfortable."
				+ " The studio is close to a Migross, Denner and the Coop."
				+ " The studio is 60m2. It has it own Badroom and kitchen."
				+ " Nothing is shared. The studio is fully furnished. The"
				+ " studio is also provided with a balcony. So if you want to"
				+ " have a privat space this could totally be good place for you."
				+ " Be aware it is only till the end of March. The price is from"
				+ " 550- 700 CHF, But there is always room to talk about it.";

		Ad adBern2 = new Ad();
		adBern2.setZipcode(3012);
		adBern2.setType(Type.APARTMENT);
		adBern2.setBuyMode(BuyMode.BUY);
		adBern2.setMoveInDate(moveInDate2);
		adBern2.setCreationDate(creationDate2);
		adBern2.setPrice(700);
		adBern2.setNumberOfBath(1);
		adBern2.setSquareFootage(60);
		// adBern2.setStudio(true);
		adBern2.setRoomDescription(studioDescription2);
		adBern2.setUser(ese);
		adBern2.setTitle("Cheap studio in Bern!");
		adBern2.setStreet("Längassstr. 40");
		adBern2.setCity("Bern");
		adBern2.setLatitude(46.9540149);
		adBern2.setLongitude(7.4315253);
		adBern2.setInfrastructureType(InfrastructureType.FOC);

		adBern2.setBalcony(false);
		adBern2.setGarage(true);
		adBern2.setDishwasher(true);
		adBern2.setElevator(true);
		adBern2.setGarage(false);
		adBern2.setBuildYear(1950);
		adBern2.setRenovationYear(1980);
		adBern2.setDistancePublicTransport(1000);
		adBern2.setDistanceSchool(500);
		adBern2.setDistanceShopping(100);
		adBern2.setFloorLevel(6);
		adBern2.setNumberOfRooms(4);

		adBern2.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adBern2, "/img/test/ad2_1.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_3.jpg"));
		adBern2.getPictures().addAll(pictures);
		adDao.save(adBern2);

		String studioDescription3 = " In the center of Gundeli (5 Min. away from the"
				+ " station, close to Tellplatz) there is a lovely house, covered with"
				+ " savage wine stocks, without any neighbours but with a garden. The"
				+ " house has two storey, on the first floor your new room is waiting"
				+ " for you. The house is totally equipped with everything a household"
				+ ": washing machine, kitchen, batroom, W-Lan...if you don´t have any"
				+ " furniture, don´t worry, I am sure, we will find something around"
				+ " the house. The price for the room and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ " new female flatmate from December on.";

		Ad adBasel = new Ad();
		adBasel.setZipcode(4051);
		adBasel.setType(Type.STUDIO);
		adBasel.setBuyMode(BuyMode.BUY);
		adBasel.setMoveInDate(moveInDate3);
		adBasel.setCreationDate(creationDate3);
		adBasel.setPrice(480);
		adBasel.setSquareFootage(10);
		// adBasel.setStudio(true);
		adBasel.setRoomDescription(studioDescription3);
		adBasel.setUser(bernerBaer);
		adBasel.setNumberOfBath(1);
		adBasel.setTitle("Nice, bright studio in the center of Basel");
		adBasel.setStreet("Bruderholzstrasse 32");
		adBasel.setCity("Basel");
		adBasel.setLatitude(47.5436664);
		adBasel.setLongitude(7.5940167);

		adBasel.setBalcony(false);
		adBasel.setGarage(false);
		adBasel.setDishwasher(true);
		adBasel.setElevator(true);
		adBasel.setGarage(true);
		adBasel.setBuildYear(1950);
		adBasel.setRenovationYear(1980);
		adBasel.setDistancePublicTransport(100);
		adBasel.setDistanceSchool(500);
		adBasel.setDistanceShopping(100);
		adBasel.setFloorLevel(3);
		adBasel.setNumberOfRooms(2);
		adBasel.setInfrastructureType(InfrastructureType.SATELLITE);

		adBasel.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adBasel, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_3.jpg"));
		adBasel.getPictures().addAll(pictures);
		adDao.save(adBasel);

		String studioDescription4 = "Flatshare of 3 persons. Flat with 5 rooms"
				+ " on the second floor. The bedroom is about 60 square meters"
				+ " with access to a nice balcony. In addition to the room, the"
				+ " flat has: a living room, a kitchen, a bathroom, a seperate WC,"
				+ " a storage in the basement, a balcony, a laundry room in the basement."
				+ " The bedroom is big and bright and has a nice parquet floor."
				+ " Possibility to keep some furnitures like the bed.";

		Ad adOlten = new Ad();
		adOlten.setZipcode(4600);
		adOlten.setType(Type.LOFT);
		adOlten.setBuyMode(BuyMode.BUY);
		adOlten.setMoveInDate(moveInDate4);
		adOlten.setCreationDate(creationDate4);
		adOlten.setPrice(430);
		adOlten.setSquareFootage(60);
		// adOlten.setStudio(false);
		adOlten.setRoomDescription(studioDescription4);
		adOlten.setUser(ese);
		adOlten.setNumberOfBath(1);
		adOlten.setTitle("Loft in Olten City");
		adOlten.setStreet("Zehnderweg 5");
		adOlten.setCity("Olten");
		adOlten.setLatitude(47.3526466);
		adOlten.setLongitude(7.899488);

		adOlten.setBalcony(false);
		adOlten.setGarage(false);
		adOlten.setDishwasher(true);
		adOlten.setElevator(true);
		adOlten.setGarage(true);
		adOlten.setBuildYear(1900);
		adOlten.setRenovationYear(1950);
		adOlten.setDistancePublicTransport(100);
		adOlten.setDistanceSchool(500);
		adOlten.setDistanceShopping(400);
		adOlten.setFloorLevel(3);
		adOlten.setNumberOfRooms(3);
		adOlten.setInfrastructureType(InfrastructureType.CABLE);

		adOlten.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adOlten, "/img/test/ad4_1.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_3.png"));
		adOlten.getPictures().addAll(pictures);
		adDao.save(adOlten);

		String studioDescription5 = "Studio meublé au 3ème étage, comprenant"
				+ " une kitchenette entièrement équipée (frigo, plaques,"
				+ " four et hotte), une pièce à vivre donnant sur un balcon,"
				+ " une salle de bains avec wc. Cave, buanderie et site satellite" + " à disposition.";

		Ad adNeuchâtel = new Ad();
		adNeuchâtel.setZipcode(2000);
		adNeuchâtel.setType(Type.VILLA);
		adNeuchâtel.setBuyMode(BuyMode.BUY);
		adNeuchâtel.setMoveInDate(moveInDate5);
		adNeuchâtel.setCreationDate(creationDate5);
		adNeuchâtel.setPrice(6000);
		adNeuchâtel.setSquareFootage(100);
		// adNeuchâtel.setStudio(true);
		adNeuchâtel.setRoomDescription(studioDescription5);
		adNeuchâtel.setNumberOfBath(2);
		adNeuchâtel.setUser(bernerBaer);
		adNeuchâtel.setTitle("Studio extrèmement bon marché à Neuchâtel");
		adNeuchâtel.setStreet("Rue de l'Hôpital 11");
		adNeuchâtel.setCity("Neuchâtel");

		adNeuchâtel.setLatitude(46.9917168);
		adNeuchâtel.setLongitude(6.9297439);

		adNeuchâtel.setBalcony(true);
		adNeuchâtel.setGarage(false);
		adNeuchâtel.setDishwasher(true);
		adNeuchâtel.setElevator(false);
		adNeuchâtel.setGarage(false);
		adNeuchâtel.setBuildYear(1900);
		adNeuchâtel.setRenovationYear(2000);
		adNeuchâtel.setDistancePublicTransport(100);
		adNeuchâtel.setDistanceSchool(500);
		adNeuchâtel.setDistanceShopping(300);
		adNeuchâtel.setFloorLevel(3);
		adNeuchâtel.setNumberOfRooms(7);
		adNeuchâtel.setInfrastructureType(InfrastructureType.CABLE);

		adNeuchâtel.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_1.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_3.jpg"));
		adNeuchâtel.getPictures().addAll(pictures);
		adDao.save(adNeuchâtel);

		String studioDescription6 = "A place just for yourself in a very nice part of Biel."
				+ " A studio for 1-2 persons with a big balcony, bathroom, kitchen and furniture already there."
				+ " It's quiet and nice, very close to the old city of Biel.";

		Ad adBiel = new Ad();
		adBiel.setZipcode(2503);
		adBiel.setType(Type.APARTMENT);
		adBiel.setBuyMode(BuyMode.BUY);
		adBiel.setMoveInDate(moveInDate6);
		adBiel.setCreationDate(creationDate6);
		adBiel.setPrice(480);
		adBiel.setSquareFootage(10);
		adBiel.setNumberOfBath(1);
		// adBiel.setStudio(true);
		adBiel.setRoomDescription(studioDescription6);
		adBiel.setUser(ese);
		adBiel.setTitle("Nice studio");
		adBiel.setStreet("Oberer Quai 12");
		adBiel.setCity("Biel/Bienne");
		adBiel.setLatitude(47.1372995);
		adBiel.setLongitude(7.247914);

		adBiel.setBalcony(true);
		adBiel.setGarage(false);
		adBiel.setDishwasher(true);
		adBiel.setElevator(false);
		adBiel.setGarage(false);
		adBiel.setBuildYear(1900);
		adBiel.setRenovationYear(2000);
		adBiel.setDistancePublicTransport(100);
		adBiel.setDistanceSchool(500);
		adBiel.setDistanceShopping(400);
		adBiel.setFloorLevel(3);
		adBiel.setNumberOfRooms(7);

		adBiel.setAuction(false);

		adBiel.setInfrastructureType(InfrastructureType.SATELLITE);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBiel, "/img/test/ad6_1.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_3.png"));
		adBiel.getPictures().addAll(pictures);
		adDao.save(adBiel);

		String roomDescription7 = "The room is a part of 3.5 rooms apartment completely renovated"
				+ " in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ " Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ " transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ " charges are separate. If you are interested, feel free to drop me a message"
				+ " to have an appointment for a visit or can write me for any further information";

		Ad adZurich = new Ad();
		adZurich.setZipcode(8000);
		adZurich.setType(Type.HOUSE);
		adZurich.setBuyMode(BuyMode.BUY);
		adZurich.setMoveInDate(moveInDate7);
		adZurich.setCreationDate(creationDate7);
		adZurich.setPrice(4000);
		adZurich.setSquareFootage(32);
		// adZurich.setStudio(false);
		adZurich.setRoomDescription(roomDescription7);
		adZurich.setUser(oprah);
		adZurich.setTitle("Roommate wanted in Zürich");
		//adZurich.setStreet("Hauptstrasse 61");
		adZurich.setStreet("Ankerstrasse 108");
		adZurich.setCity("Zürich");
		adZurich.setLatitude(47.3760101);
		adZurich.setLongitude(8.5277654);
		adZurich.setNumberOfBath(2);

		adZurich.setBalcony(true);
		adZurich.setGarage(false);
		adZurich.setDishwasher(true);
		adZurich.setElevator(true);
		adZurich.setGarage(false);
		adZurich.setBuildYear(1944);
		adZurich.setRenovationYear(2000);
		adZurich.setDistancePublicTransport(300);
		adZurich.setDistanceSchool(400);
		adZurich.setDistanceShopping(100);
		adZurich.setFloorLevel(3);
		adZurich.setNumberOfRooms(5);
		adZurich.setInfrastructureType(InfrastructureType.CABLE);

		adZurich.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adZurich, "/img/test/ad1_3.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_1.jpg"));
		adZurich.getPictures().addAll(pictures);
		adDao.save(adZurich);

		String studioDescription8 = "It is small studio close to the"
				+ " university and the bahnhof. The lovely neighbourhood"
				+ " Langgasse makes it an easy place to feel comfortable."
				+ " The studio is close to a Migross, Denner and the Coop."
				+ " The studio is 60m2. It has it own Badroom and kitchen."
				+ " Nothing is shared. The studio is fully furnished. The"
				+ " studio is also provided with a balcony. So if you want to"
				+ " have a privat space this could totally be good place for you."
				+ " Be aware it is only till the end of March. The price is from"
				+ " 550- 700 CHF, But there is always room to talk about it.";

		Ad adLuzern = new Ad();
		adLuzern.setZipcode(6000);
		adLuzern.setType(Type.APARTMENT);
		adLuzern.setBuyMode(BuyMode.BUY);
		adLuzern.setMoveInDate(moveInDate8);
		adLuzern.setCreationDate(creationDate2);
		adLuzern.setPrice(900);
		adLuzern.setSquareFootage(60);

		adLuzern.setRoomDescription(studioDescription8);
		adLuzern.setUser(oprah);
		adLuzern.setNumberOfBath(1);
		adLuzern.setTitle("Elegant Studio in Lucerne");
		adLuzern.setStreet("Schwanenplatz 61");
		adLuzern.setCity("Luzern");
		adLuzern.setLatitude(47.053668);
		adLuzern.setLongitude(8.30871659);
		adLuzern.setBalcony(true);
		adLuzern.setGarage(false);
		adLuzern.setDishwasher(true);
		adLuzern.setElevator(true);
		adLuzern.setGarage(false);
		adLuzern.setBuildYear(1960);
		adLuzern.setRenovationYear(1991);
		adLuzern.setDistancePublicTransport(500);
		adLuzern.setDistanceSchool(1000);
		adLuzern.setDistanceShopping(100);
		adLuzern.setFloorLevel(6);
		adLuzern.setNumberOfRooms(3);
		adLuzern.setInfrastructureType(InfrastructureType.CABLE);

		adLuzern.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adLuzern, "/img/test/ad2_3.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_1.jpg"));
		adLuzern.getPictures().addAll(pictures);
		adDao.save(adLuzern);

		String studioDescription9 = "In the center of Gundeli (5 Min. away from the"
				+ " station, close to Tellplatz) there is a lovely house, covered with"
				+ " savage wine stocks, without any neighbours but with a garden. The"
				+ " house has two storey, on the first floor your new room is waiting"
				+ " for you. The house is totally equipped with everything a household:"
				+ " washing machine, kitchen, batroom, W-Lan...if you don´t have any"
				+ " furniture, don´t worry, I am sure, we will find something around"
				+ " the house. The price for the room and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ " new female flatmate from December on.";

		Ad adAarau = new Ad();
		adAarau.setZipcode(5000);
		adAarau.setType(Type.APARTMENT);
		adAarau.setBuyMode(BuyMode.BUY);
		adAarau.setMoveInDate(moveInDate3);
		adAarau.setCreationDate(creationDate8);
		adAarau.setPrice(1500);
		adAarau.setSquareFootage(26);

		adAarau.setRoomDescription(studioDescription9);
		adAarau.setUser(oprah);
		adAarau.setTitle("Beautiful studio in Aarau");
		adAarau.setStreet("Herzbergstrasse 31");
		adAarau.setCity("Aarau");
		adAarau.setLatitude(47.3998157);
		adAarau.setLongitude(8.0451638);
		adAarau.setNumberOfBath(1);

		adAarau.setBalcony(true);
		adAarau.setGarage(true);
		adAarau.setDishwasher(true);
		adAarau.setElevator(true);
		adAarau.setGarage(true);
		adAarau.setBuildYear(1960);
		adAarau.setRenovationYear(1970);
		adAarau.setDistancePublicTransport(100);
		adAarau.setDistanceSchool(500);
		adAarau.setDistanceShopping(700);
		adAarau.setFloorLevel(1);
		adAarau.setNumberOfRooms(3);
		adAarau.setInfrastructureType(InfrastructureType.CABLE);

		adAarau.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adAarau, "/img/test/ad3_3.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_3.jpg"));

		adAarau.getPictures().addAll(pictures);
		adDao.save(adAarau);

		String studioDescription10 = "Flatshare of 3 persons. Flat with 5 rooms"
				+ " on the second floor. The bedroom is about 60 square meters"
				+ " with access to a nice balcony. In addition to the room, the"
				+ " flat has: a living room, a kitchen, a bathroom, a seperate WC,"
				+ " a storage in the basement, a balcony, a laundry room in the basement."
				+ " The bedroom is big and bright and has a nice parquet floor."
				+ " Possibility to keep some furnitures like the bed.";

		Ad adDavos = new Ad();
		adDavos.setZipcode(7260);
		adDavos.setType(Type.APARTMENT);
		adDavos.setBuyMode(BuyMode.BUY);
		adDavos.setMoveInDate(moveInDate2);
		adDavos.setCreationDate(creationDate4);
		adDavos.setPrice(1100);
		adDavos.setSquareFootage(74);

		adDavos.setRoomDescription(studioDescription10);
		adDavos.setUser(oprah);
		adDavos.setTitle("Free room in Davos City");

		adDavos.setStreet("Rossweidstrasse 9");
		adDavos.setCity("Davos");
		adDavos.setLatitude(46.8033266);
		adDavos.setLongitude(9.8339639);
		adDavos.setBalcony(false);
		adDavos.setGarage(false);
		adDavos.setDishwasher(false);
		adDavos.setElevator(false);
		adDavos.setGarage(true);
		adDavos.setBuildYear(1960);
		adDavos.setRenovationYear(1970);
		adDavos.setDistancePublicTransport(2000);
		adDavos.setDistanceSchool(500);
		adDavos.setDistanceShopping(700);
		adDavos.setFloorLevel(1);
		adDavos.setNumberOfRooms(3);
		adDavos.setInfrastructureType(InfrastructureType.FOC);
		adDavos.setNumberOfBath(1);

		adDavos.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adDavos, "/img/test/ad4_3.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_1.png"));
		adDavos.getPictures().addAll(pictures);
		adDao.save(adDavos);

		String studioDescription11 = "Studio meublé au 3ème étage, comprenant"
				+ " une kitchenette entièrement équipée (frigo, plaques,"
				+ " four et hotte), une pièce à vivre donnant sur un balcon,"
				+ " une salle de bains avec wc. Cave, buanderie et site satellite" + " à disposition.";

		Ad adLausanne = new Ad();
		adLausanne.setZipcode(1000);
		adLausanne.setType(Type.APARTMENT);
		adLausanne.setBuyMode(BuyMode.BUY);
		adLausanne.setMoveInDate(moveInDate5);
		adLausanne.setCreationDate(creationDate5);
		adLausanne.setPrice(1800);
		adLausanne.setSquareFootage(8);
		// adLausanne.setStudio(false);
		adLausanne.setRoomDescription(studioDescription11);
		adLausanne.setUser(oprah);
		adLausanne.setTitle("Studio extrèmement bon marché à Lausanne");
		adLausanne.setStreet("Rue de l'Eglise 26");
		adLausanne.setCity("Lausanne");
		adLausanne.setLatitude(46.5944014);
		adLausanne.setLongitude(6.5669777);
		adLausanne.setNumberOfBath(1);

		adLausanne.setBalcony(false);
		adLausanne.setGarage(false);
		adLausanne.setDishwasher(true);
		adLausanne.setElevator(false);
		adLausanne.setGarage(true);
		adLausanne.setBuildYear(1960);
		adLausanne.setRenovationYear(1980);
		adLausanne.setDistancePublicTransport(300);
		adLausanne.setDistanceSchool(100);
		adLausanne.setDistanceShopping(900);
		adLausanne.setFloorLevel(1);
		adLausanne.setNumberOfRooms(3);
		adLausanne.setInfrastructureType(InfrastructureType.CABLE);

		adLausanne.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adLausanne, "/img/test/ad5_3.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_1.jpg"));
		adLausanne.getPictures().addAll(pictures);
		adDao.save(adLausanne);

		String studioDescription12 = "A place just for yourself in a very nice part of Biel."
				+ " A studio for 1-2 persons with a big balcony, bathroom, kitchen and furniture already there."
				+ " It's quiet and nice, very close to the old city of Biel.";

		Ad adLocarno = new Ad();
		adLocarno.setZipcode(6600);
		adLocarno.setType(Type.VILLA);
		adLocarno.setBuyMode(BuyMode.BUY);
		adLocarno.setMoveInDate(moveInDate6);
		adLocarno.setCreationDate(creationDate6);
		adLocarno.setPrice(4500);
		adLocarno.setSquareFootage(42);
		// adLocarno.setStudio(false);
		adLocarno.setRoomDescription(studioDescription12);
		adLocarno.setUser(jane);
		adLocarno.setTitle("Malibu-style Beachhouse");
		//adLocarno.setStreet("Kirchweg 12");
		adLocarno.setStreet("Via Serafino Balestra 36");
		adLocarno.setCity("Locarno");
		adLocarno.setLatitude(46.1626373);
		adLocarno.setLongitude(8.797786);
		adLocarno.setNumberOfBath(1);

		adLocarno.setBalcony(true);
		adLocarno.setGarage(false);
		adLocarno.setDishwasher(true);
		adLocarno.setElevator(false);
		adLocarno.setGarage(true);
		adLocarno.setBuildYear(1800);
		adLocarno.setRenovationYear(1980);
		adLocarno.setDistancePublicTransport(300);
		adLocarno.setDistanceSchool(1000);
		adLocarno.setDistanceShopping(900);
		adLocarno.setFloorLevel(4);
		adLocarno.setNumberOfRooms(6);
		adLocarno.setInfrastructureType(InfrastructureType.CABLE);

		adLocarno.setAuction(false);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adLocarno, "/img/test/ad6_3.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_1.png"));
		adLocarno.getPictures().addAll(pictures);
		adDao.save(adLocarno);

		// Auction Ad
		String roomDescription13 = "This is a beautiful Villa near the Sea";

		Ad adInterlaken = new Ad();
		adInterlaken.setZipcode(6600);
		adInterlaken.setType(Type.VILLA);
		adInterlaken.setBuyMode(BuyMode.BUY);
		adInterlaken.setMoveInDate(moveInDate9);
		adInterlaken.setCreationDate(creationDate9);
		adInterlaken.setPrice(5000);
		adInterlaken.setSquareFootage(100);
		adInterlaken.setRoomDescription(roomDescription13);
		adInterlaken.setUser(jane);
		adInterlaken.setTitle("Vintage Villa");
		adInterlaken.setStreet("Spühlibachweg 10");
		adInterlaken.setCity("Interlaken");
		adInterlaken.setLatitude(46.68638);
		adInterlaken.setLongitude(7.8729456);
		adInterlaken.setNumberOfBath(5);

		adInterlaken.setBalcony(true);
		adInterlaken.setGarage(true);
		adInterlaken.setDishwasher(true);
		adInterlaken.setElevator(false);
		adInterlaken.setGarage(true);
		adInterlaken.setBuildYear(1999);
		adInterlaken.setRenovationYear(2015);
		adInterlaken.setDistancePublicTransport(1000);
		adInterlaken.setDistanceSchool(2000);
		adInterlaken.setDistanceShopping(800);
		adInterlaken.setFloorLevel(1);
		adInterlaken.setNumberOfRooms(10);
		adInterlaken.setInfrastructureType(InfrastructureType.CABLE);

		adInterlaken.setAuction(true);
		adInterlaken.setStartPrice(1500);
		adInterlaken.setIncreaseBidPrice(100);
		adInterlaken.setcurrentAuctionPrice(adInterlaken.getStartPrice() + adInterlaken.getIncreaseBidPrice());
		adInterlaken.setStartDate(startAuctionDate1);
		adInterlaken.setEndDate(endAuctionDate1);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adInterlaken, "/img/test/HouseAuction.jpg"));

		adInterlaken.getPictures().addAll(pictures);
		adDao.save(adInterlaken);

		// Auction Ad 2

		String roomDescription14 = "This is a beautiful house near the town center with a beautiful view to the alps.";

		Ad adGrosshoechstetten = new Ad();
		adGrosshoechstetten.setZipcode(3506);
		adGrosshoechstetten.setType(Type.HOUSE);
		adGrosshoechstetten.setBuyMode(BuyMode.BUY);
		adGrosshoechstetten.setMoveInDate(moveInDate9);
		adGrosshoechstetten.setCreationDate(creationDate9);
		adGrosshoechstetten.setPrice(1200000);
		adGrosshoechstetten.setSquareFootage(120);
		adGrosshoechstetten.setRoomDescription(roomDescription14);
		adGrosshoechstetten.setUser(jane);
		adGrosshoechstetten.setTitle("Nice house very close to town center");
		adGrosshoechstetten.setStreet("Sonnmattstrasse 16");
		adGrosshoechstetten.setCity("Grosshöchstetten");
		adGrosshoechstetten.setLatitude(46.9043114);
		adGrosshoechstetten.setLongitude(7.6403862);
		adGrosshoechstetten.setNumberOfBath(3);

		adGrosshoechstetten.setBalcony(true);
		adGrosshoechstetten.setGarage(true);
		adGrosshoechstetten.setDishwasher(true);
		adGrosshoechstetten.setElevator(false);
		adGrosshoechstetten.setParking(false);
		adGrosshoechstetten.setBuildYear(1980);
		adGrosshoechstetten.setRenovationYear(2015);
		adGrosshoechstetten.setDistancePublicTransport(2000);
		adGrosshoechstetten.setDistanceSchool(300);
		adGrosshoechstetten.setDistanceShopping(800);
		adGrosshoechstetten.setFloorLevel(2);
		adGrosshoechstetten.setNumberOfRooms(7);
		adGrosshoechstetten.setInfrastructureType(InfrastructureType.CABLE);

		adGrosshoechstetten.setAuction(true);
		adGrosshoechstetten.setStartPrice(900000);
		adGrosshoechstetten.setIncreaseBidPrice(1000);
		adGrosshoechstetten.setcurrentAuctionPrice(adGrosshoechstetten.getStartPrice() + adGrosshoechstetten.getIncreaseBidPrice());
		adGrosshoechstetten.setStartDate(startAuctionDate1);
		adGrosshoechstetten.setEndDate(endAuctionDate1);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adGrosshoechstetten, "/img/test/HouseAuction2.jpg"));

		adGrosshoechstetten.getPictures().addAll(pictures);
		adDao.save(adGrosshoechstetten);

		// Auction Ad 3 is a paused auction

		String roomDescription15 = "A lovely apartment near the lake of Thun. The \"Strämu\" is just a five minutes walk away!";

		Ad adThun = new Ad();
		adThun.setZipcode(3600);
		adThun.setType(Type.APARTMENT);
		adThun.setBuyMode(BuyMode.BUY);
		adThun.setMoveInDate(moveInDate9);
		adThun.setCreationDate(creationDate9);
		adThun.setPrice(450000);
		adThun.setSquareFootage(80);
		adThun.setRoomDescription(roomDescription15);
		adThun.setUser(jane);
		adThun.setTitle("Lovely flat near the lake of Thun");
		adThun.setStreet("Strandbadweg 4");
		adThun.setCity("Thun");
		adThun.setLatitude(46.7400189);
		adThun.setLongitude(7.6308307);
		adThun.setNumberOfBath(2);

		adThun.setBalcony(true);
		adThun.setGarage(false);
		adThun.setDishwasher(true);
		adThun.setElevator(true);
		adThun.setParking(true);
		adThun.setBuildYear(1996);
		adThun.setRenovationYear(2010);
		adThun.setDistancePublicTransport(2000);
		adThun.setDistanceSchool(600);
		adThun.setDistanceShopping(700);
		adThun.setFloorLevel(2);
		adThun.setNumberOfRooms(3);
		adThun.setInfrastructureType(InfrastructureType.FOC);

		adThun.setAuction(true);
		adThun.setAvailableForAuction(false);
		adThun.setStartPrice(300000);
		adThun.setIncreaseBidPrice(1000);
		adThun.setcurrentAuctionPrice(adThun.getStartPrice() + adThun.getIncreaseBidPrice());
		adThun.setStartDate(startAuctionDate1);
		adThun.setEndDate(endAuctionDate1);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adThun, "/img/test/HouseAuction3.jpg"));

		adThun.getPictures().addAll(pictures);
		adDao.save(adThun);

		// Auction Ad 4 is an expired auction

		String roomDescription16 = "A small lovely studio in the old town of Bern. Shopping possibilities, "
				+ "public transport and schools/university just few minutes away.";

		Ad adBern3 = new Ad();
		adBern3.setZipcode(3000);
		adBern3.setType(Type.STUDIO);
		adBern3.setBuyMode(BuyMode.BUY);
		adBern3.setMoveInDate(moveInDate9);
		adBern3.setCreationDate(creationDate9);
		adBern3.setPrice(800000);
		adBern3.setSquareFootage(50);
		adBern3.setRoomDescription(roomDescription16);
		adBern3.setUser(jane);
		adBern3.setTitle("Lovely studio in the old town of Bern");
		adBern3.setStreet("Kramgasse 10");
		adBern3.setCity("Bern");
		adBern3.setLatitude(46.9481317);
		adBern3.setLongitude(7.4517285);
		adBern3.setNumberOfBath(1);

		adBern3.setBalcony(true);
		adBern3.setGarage(false);
		adBern3.setDishwasher(false);
		adBern3.setElevator(false);
		adBern3.setParking(false);
		adBern3.setBuildYear(1810);
		adBern3.setRenovationYear(2005);
		adBern3.setDistancePublicTransport(300);
		adBern3.setDistanceSchool(600);
		adBern3.setDistanceShopping(100);
		adBern3.setFloorLevel(5);
		adBern3.setNumberOfRooms(2);
		adBern3.setInfrastructureType(InfrastructureType.SATELLITE);

		adBern3.setAuction(true);
		adBern3.setStartPrice(650000);
		adBern3.setIncreaseBidPrice(5000);
		adBern3.setcurrentAuctionPrice(adBern3.getStartPrice() + adBern3.getIncreaseBidPrice());

		Date startAuctionDateExpired = formatter.parse("10.10.2016");
		Date endAuctionDateExpired = formatter.parse("30.10.2016");

		adBern3.setStartDate(startAuctionDateExpired);
		adBern3.setEndDate(endAuctionDateExpired);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adBern3, "/img/test/HouseAuction4.jpg"));

		adBern3.getPictures().addAll(pictures);
		adDao.save(adBern3);

		// Auction Ad 5 is a completed auction

		String roomDescription17 = "A large old farm house in the hills of the Emmental. Some surroundings and part of a forest "
				+ "belong to the house.";

		Ad adEggiwil = new Ad();
		adEggiwil.setZipcode(3537);
		adEggiwil.setType(Type.HOUSE);
		adEggiwil.setBuyMode(BuyMode.BUY);
		adEggiwil.setMoveInDate(moveInDate9);
		adEggiwil.setCreationDate(creationDate9);
		adEggiwil.setPrice(2400000);
		adEggiwil.setSquareFootage(300);
		adEggiwil.setRoomDescription(roomDescription17);
		adEggiwil.setUser(jane);
		adEggiwil.setTitle("Old farm house in the Emmental");
		adEggiwil.setStreet("Inner Zimmerzei 688");
		adEggiwil.setCity("Eggiwil");
		adEggiwil.setLatitude(46.8941994);
		adEggiwil.setLongitude(7.7774038);
		adEggiwil.setNumberOfBath(4);

		adEggiwil.setBalcony(true);
		adEggiwil.setGarage(true);
		adEggiwil.setDishwasher(true);
		adEggiwil.setElevator(false);
		adEggiwil.setParking(true);
		adEggiwil.setBuildYear(1750);
		adEggiwil.setRenovationYear(2012);
		adEggiwil.setDistancePublicTransport(2000);
		adEggiwil.setDistanceSchool(2000);
		adEggiwil.setDistanceShopping(2000);
		adEggiwil.setFloorLevel(3);
		adEggiwil.setNumberOfRooms(8);
		adEggiwil.setInfrastructureType(InfrastructureType.CABLE);

		adEggiwil.setAuction(true);
		adEggiwil.setAuctionCompleted(true);
		adEggiwil.setAvailableForAuction(false);
		adEggiwil.setStartPrice(1800000);
		adEggiwil.setIncreaseBidPrice(10000);
		adEggiwil.setcurrentAuctionPrice(adEggiwil.getStartPrice() + adEggiwil.getIncreaseBidPrice());

		Date startAuctionDateExpired2 = formatter.parse("10.10.2016");
		Date endAuctionDateExpired2 = formatter.parse("10.11.2016");

		adEggiwil.setStartDate(startAuctionDateExpired2);
		adEggiwil.setEndDate(endAuctionDateExpired2);

		pictures = new ArrayList<>();
		pictures.add(createPicture(adEggiwil, "/img/test/HouseAuction5.jpg"));

		adEggiwil.getPictures().addAll(pictures);
		adDao.save(adEggiwil);

	}

	private AdPicture createPicture(Ad ad, String filePath) {
		AdPicture picture = new AdPicture();
		picture.setFilePath(filePath);
		return picture;
	}

}