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
		adOlten.setTitle("Roommate wanted in Olten City");
		adOlten.setStreet("Zehnderweg 5");
		adOlten.setCity("Olten");

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
		// adAarau.setStudio(true);
		adAarau.setRoomDescription(studioDescription9);
		adAarau.setUser(oprah);
		adAarau.setTitle("Beautiful studio in Aarau");
	//	adAarau.setStreet("Bruderholzstrasse 32");
		adAarau.setStreet("Herzbergstrasse 31");
		adAarau.setCity("Aarau");
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
		// adDavos.setStudio(false);
		adDavos.setRoomDescription(studioDescription10);
		adDavos.setUser(oprah);
		adDavos.setTitle("Free room in Davos City");
	//	adDavos.setStreet("Kathrinerweg 5");
		adDavos.setStreet("Rossweidstrasse 9");
		adDavos.setCity("Davos");
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

		Ad auctionAd1 = new Ad();
		auctionAd1.setZipcode(6600);
		auctionAd1.setType(Type.VILLA);
		auctionAd1.setBuyMode(BuyMode.BUY);
		auctionAd1.setMoveInDate(moveInDate9);
		auctionAd1.setCreationDate(creationDate9);
		auctionAd1.setPrice(5000);
		auctionAd1.setSquareFootage(100);
		auctionAd1.setRoomDescription(roomDescription13);
		auctionAd1.setUser(jane);
		auctionAd1.setTitle("Vintage Villa");
		auctionAd1.setStreet("Spühlibachweg 10");
		auctionAd1.setCity("Interlaken");
		auctionAd1.setNumberOfBath(5);

		auctionAd1.setBalcony(true);
		auctionAd1.setGarage(true);
		auctionAd1.setDishwasher(true);
		auctionAd1.setElevator(false);
		auctionAd1.setGarage(true);
		auctionAd1.setBuildYear(1999);
		auctionAd1.setRenovationYear(2015);
		auctionAd1.setDistancePublicTransport(1000);
		auctionAd1.setDistanceSchool(2000);
		auctionAd1.setDistanceShopping(800);
		auctionAd1.setFloorLevel(1);
		auctionAd1.setNumberOfRooms(10);
		auctionAd1.setInfrastructureType(InfrastructureType.CABLE);

		auctionAd1.setAuction(true);
		auctionAd1.setStartPrice(1500);
		auctionAd1.setIncreaseBidPrice(100);
		auctionAd1.setcurrentAuctionPrice(auctionAd1.getStartPrice() + auctionAd1.getIncreaseBidPrice());
		auctionAd1.setStartDate(startAuctionDate1);
		auctionAd1.setEndDate(endAuctionDate1);

		pictures = new ArrayList<>();
		pictures.add(createPicture(auctionAd1, "/img/test/HouseAuction.jpg"));

		auctionAd1.getPictures().addAll(pictures);
		adDao.save(auctionAd1);

		// Auction Ad 2

		String roomDescription14 = "This is a beautiful house near the town center with a beautiful view to the alps.";

		Ad auctionAd2 = new Ad();
		auctionAd2.setZipcode(3506);
		auctionAd2.setType(Type.HOUSE);
		auctionAd2.setBuyMode(BuyMode.BUY);
		auctionAd2.setMoveInDate(moveInDate9);
		auctionAd2.setCreationDate(creationDate9);
		auctionAd2.setPrice(1200000);
		auctionAd2.setSquareFootage(120);
		auctionAd2.setRoomDescription(roomDescription14);
		auctionAd2.setUser(jane);
		auctionAd2.setTitle("Nice house very close to town center");
		auctionAd2.setStreet("Sonnmattstrasse 12");
		auctionAd2.setCity("Grosshöchstetten");
		auctionAd2.setNumberOfBath(3);

		auctionAd2.setBalcony(true);
		auctionAd2.setGarage(true);
		auctionAd2.setDishwasher(true);
		auctionAd2.setElevator(false);
		auctionAd2.setParking(false);
		auctionAd2.setBuildYear(1980);
		auctionAd2.setRenovationYear(2015);
		auctionAd2.setDistancePublicTransport(2000);
		auctionAd2.setDistanceSchool(300);
		auctionAd2.setDistanceShopping(800);
		auctionAd2.setFloorLevel(2);
		auctionAd2.setNumberOfRooms(7);
		auctionAd2.setInfrastructureType(InfrastructureType.CABLE);

		auctionAd2.setAuction(true);
		auctionAd2.setStartPrice(900000);
		auctionAd2.setIncreaseBidPrice(1000);
		auctionAd2.setcurrentAuctionPrice(auctionAd2.getStartPrice() + auctionAd2.getIncreaseBidPrice());
		auctionAd2.setStartDate(startAuctionDate1);
		auctionAd2.setEndDate(endAuctionDate1);

		pictures = new ArrayList<>();
		pictures.add(createPicture(auctionAd2, "/img/test/HouseAuction2.jpg"));

		auctionAd2.getPictures().addAll(pictures);
		adDao.save(auctionAd2);

		// Auction Ad 3 is a paused auction

		String roomDescription15 = "A lovely apartment near the lake of Thun. The \"Strämu\" is just a five minutes walk away!";

		Ad auctionAd3 = new Ad();
		auctionAd3.setZipcode(3600);
		auctionAd3.setType(Type.APARTMENT);
		auctionAd3.setBuyMode(BuyMode.BUY);
		auctionAd3.setMoveInDate(moveInDate9);
		auctionAd3.setCreationDate(creationDate9);
		auctionAd3.setPrice(450000);
		auctionAd3.setSquareFootage(80);
		auctionAd3.setRoomDescription(roomDescription15);
		auctionAd3.setUser(jane);
		auctionAd3.setTitle("Lovely flat near the lake of Thun");
		auctionAd3.setStreet("Strandbadweg 4");
		auctionAd3.setCity("Thun");
		auctionAd3.setNumberOfBath(2);

		auctionAd3.setBalcony(true);
		auctionAd3.setGarage(false);
		auctionAd3.setDishwasher(true);
		auctionAd3.setElevator(true);
		auctionAd3.setParking(true);
		auctionAd3.setBuildYear(1996);
		auctionAd3.setRenovationYear(2010);
		auctionAd3.setDistancePublicTransport(2000);
		auctionAd3.setDistanceSchool(600);
		auctionAd3.setDistanceShopping(700);
		auctionAd3.setFloorLevel(2);
		auctionAd3.setNumberOfRooms(3);
		auctionAd3.setInfrastructureType(InfrastructureType.FOC);

		auctionAd3.setAuction(true);
		auctionAd3.setAvailableForAuction(false);
		auctionAd3.setStartPrice(300000);
		auctionAd3.setIncreaseBidPrice(1000);
		auctionAd3.setcurrentAuctionPrice(auctionAd3.getStartPrice() + auctionAd3.getIncreaseBidPrice());
		auctionAd3.setStartDate(startAuctionDate1);
		auctionAd3.setEndDate(endAuctionDate1);

		pictures = new ArrayList<>();
		pictures.add(createPicture(auctionAd3, "/img/test/HouseAuction3.jpg"));

		auctionAd3.getPictures().addAll(pictures);
		adDao.save(auctionAd3);

		// Auction Ad 4 is an expired auction

		String roomDescription16 = "A small lovely studio in the old town of Bern. Shopping possibilities, "
				+ "public transport and schools/university just few minutes away.";

		Ad auctionAd4 = new Ad();
		auctionAd4.setZipcode(3000);
		auctionAd4.setType(Type.STUDIO);
		auctionAd4.setBuyMode(BuyMode.BUY);
		auctionAd4.setMoveInDate(moveInDate9);
		auctionAd4.setCreationDate(creationDate9);
		auctionAd4.setPrice(800000);
		auctionAd4.setSquareFootage(50);
		auctionAd4.setRoomDescription(roomDescription16);
		auctionAd4.setUser(jane);
		auctionAd4.setTitle("Lovely studio in the old town of Bern");
		auctionAd4.setStreet("Kramgasse 10");
		auctionAd4.setCity("Bern");
		auctionAd4.setNumberOfBath(1);

		auctionAd4.setBalcony(true);
		auctionAd4.setGarage(false);
		auctionAd4.setDishwasher(false);
		auctionAd4.setElevator(false);
		auctionAd4.setParking(false);
		auctionAd4.setBuildYear(1810);
		auctionAd4.setRenovationYear(2005);
		auctionAd4.setDistancePublicTransport(300);
		auctionAd4.setDistanceSchool(600);
		auctionAd4.setDistanceShopping(100);
		auctionAd4.setFloorLevel(5);
		auctionAd4.setNumberOfRooms(2);
		auctionAd4.setInfrastructureType(InfrastructureType.SATELLITE);

		auctionAd4.setAuction(true);
		auctionAd4.setStartPrice(650000);
		auctionAd4.setIncreaseBidPrice(5000);
		auctionAd4.setcurrentAuctionPrice(auctionAd4.getStartPrice() + auctionAd4.getIncreaseBidPrice());

		Date startAuctionDateExpired = formatter.parse("10.10.2016");
		Date endAuctionDateExpired = formatter.parse("30.10.2016");

		auctionAd4.setStartDate(startAuctionDateExpired);
		auctionAd4.setEndDate(endAuctionDateExpired);

		pictures = new ArrayList<>();
		pictures.add(createPicture(auctionAd4, "/img/test/HouseAuction4.jpg"));

		auctionAd4.getPictures().addAll(pictures);
		adDao.save(auctionAd4);

		// Auction Ad 5 is a completed auction

		String roomDescription17 = "A large old farm house in the hills of the Emmental. Some surroundings and part of a forest "
				+ "belong to the house.";

		Ad auctionAd5 = new Ad();
		auctionAd5.setZipcode(3537);
		auctionAd5.setType(Type.HOUSE);
		auctionAd5.setBuyMode(BuyMode.BUY);
		auctionAd5.setMoveInDate(moveInDate9);
		auctionAd5.setCreationDate(creationDate9);
		auctionAd5.setPrice(2400000);
		auctionAd5.setSquareFootage(300);
		auctionAd5.setRoomDescription(roomDescription17);
		auctionAd5.setUser(jane);
		auctionAd5.setTitle("Old farm house in the Emmental");
		auctionAd5.setStreet("Inner Zimmerzei 688");
		auctionAd5.setCity("Eggiwil");
		auctionAd5.setNumberOfBath(4);

		auctionAd5.setBalcony(true);
		auctionAd5.setGarage(true);
		auctionAd5.setDishwasher(true);
		auctionAd5.setElevator(false);
		auctionAd5.setParking(true);
		auctionAd5.setBuildYear(1750);
		auctionAd5.setRenovationYear(2012);
		auctionAd5.setDistancePublicTransport(2000);
		auctionAd5.setDistanceSchool(2000);
		auctionAd5.setDistanceShopping(2000);
		auctionAd5.setFloorLevel(3);
		auctionAd5.setNumberOfRooms(8);
		auctionAd5.setInfrastructureType(InfrastructureType.CABLE);

		auctionAd5.setAuction(true);
		auctionAd5.setAuctionCompleted(true);
		auctionAd5.setAvailableForAuction(false);
		auctionAd5.setStartPrice(1800000);
		auctionAd5.setIncreaseBidPrice(10000);
		auctionAd5.setcurrentAuctionPrice(auctionAd5.getStartPrice() + auctionAd5.getIncreaseBidPrice());

		Date startAuctionDateExpired2 = formatter.parse("10.10.2016");
		Date endAuctionDateExpired2 = formatter.parse("10.11.2016");

		auctionAd5.setStartDate(startAuctionDateExpired2);
		auctionAd5.setEndDate(endAuctionDateExpired2);

		pictures = new ArrayList<>();
		pictures.add(createPicture(auctionAd5, "/img/test/HouseAuction5.jpg"));

		auctionAd5.getPictures().addAll(pictures);
		adDao.save(auctionAd5);

	}

	private AdPicture createPicture(Ad ad, String filePath) {
		AdPicture picture = new AdPicture();
		picture.setFilePath(filePath);
		return picture;
	}

}