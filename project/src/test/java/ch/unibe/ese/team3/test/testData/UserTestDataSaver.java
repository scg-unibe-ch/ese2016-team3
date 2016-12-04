package ch.unibe.ese.team3.test.testData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Calendar;

import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.UserPicture;
import ch.unibe.ese.team3.model.UserRole;
import ch.unibe.ese.team3.model.CreditcardType;
import ch.unibe.ese.team3.model.dao.UserDao;

/**
 * This inserts some user test data into the database.
 */
@Service
public class UserTestDataSaver {

	@Autowired
	private UserDao userDao;

	@Transactional
	public void saveTestData() throws Exception {
		// system account
		User system = createUser("System", "1234", "FlatFindr", "Admin",
				"/img/test/system.jpg", Gender.ADMIN, AccountType.PREMIUM);
		system.setAboutMe("We keep you off the streets.");
		userDao.save(system);

		// Main test-user for the assistants (advertiser)
		User ese = createUser("ese@unibe.ch", "ese", "John", "Wayne",
				"/img/test/portrait.jpg", Gender.MALE, AccountType.PREMIUM);
		ese.setAboutMe(getDummyText());
		Calendar expirationOfPremium = Calendar.getInstance();
		expirationOfPremium.add(Calendar.DAY_OF_MONTH, 365);
		ese.setPremiumExpiryDate(expirationOfPremium.getTime());
		ese.setCreditCard("1111222233334444");
		ese.setCreditcardType(CreditcardType.VISA);
		ese.setCreditcardName("John Wayne");
		ese.setExpirationMonth("11");
		ese.setExpirationYear("2021");
		ese.setSecurityNumber("725");
		userDao.save(ese);
		
		// Searcher
		User janeDoe = createUser("jane@doe.com", "password", "Jane", "Doe",
				Gender.FEMALE, AccountType.BASIC);
		janeDoe.setAboutMe(getDummyText());
		userDao.save(janeDoe);

		// Another advertiser & searcher
		User bernerBaer = createUser("user@bern.com", "password",
				"Berner", "Bär", Gender.MALE, AccountType.BASIC);
		UserPicture picture = new UserPicture();
		picture.setFilePath("/img/test/berner_baer.png");
		picture.setUser(bernerBaer);
		bernerBaer.setPicture(picture);
		bernerBaer.setAboutMe("I am a PhD student and I am Italian. I am 26,"
				+ "I like winter-sports, hiking, traveling and cooking."
				+ "I enjoy spending time with friends, watching movies, "
				+ "going for drinks and organizing dinners. I have lived in Milan,"
				+ "London and Zurich, always in flatshares and i have never had"
				+ "problems with my flatmates.");
		userDao.save(bernerBaer);
		
		// Another advertiser & searcher
		User oprah = createUser("oprah@ithaca.com", "password", "Oprah", "Winfrey",
				"/img/test/oprah.jpg", Gender.FEMALE, AccountType.BASIC);
		oprah.setAboutMe(getDummyText());
		userDao.save(oprah);
		
		//User for updateUser test		
		User mark = createUser("mark@knopfler.com", "straits", "Mark", "Knopfler", "/img/test/user.jpg", Gender.MALE, AccountType.BASIC);
		mark.setAboutMe(getDummyText());
		userDao.save(mark);
		
		//User with no previous messages for MessageServiceTest
		User kim = createUser("Kim@kardashian.com", "1234", "Kim", "Kardashian",
				"/img/test/system.jpg", Gender.FEMALE, AccountType.BASIC);
		userDao.save(kim);
		
		//User for message test
		User eric = createUser("eric@clapton.com", "guitar", "Eric", "Clapton", "/img/test/user.jpg", Gender.MALE, AccountType.BASIC);
		eric.setAboutMe(getDummyText());
		userDao.save(eric);
		
		//User for Email test
		User email = createUser("ithacatest@trash-mail.com", "123456", "Ithaca", "Test", Gender.MALE, AccountType.PREMIUM);
		Date expiryTomorrow = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expiryTomorrow);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		expiryTomorrow = calendar.getTime();
		email.setPremiumExpiryDate(expiryTomorrow);
		email.setCreditCard("1111222233334444");
		email.setCreditcardType(CreditcardType.VISA);
		email.setCreditcardName("Email Test");
		email.setExpirationMonth("10");
		email.setExpirationYear("2020");
		email.setSecurityNumber("123");
		userDao.save(email);
		
		//User for removing expired Premium.
		User removePremium = createUser("ithacatest2@trash-mail.com", "123456", "Ithaca", "Test", Gender.MALE, AccountType.PREMIUM);
		Date expiryBefore = new Date();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(expiryBefore);
		calendar2.add(Calendar.HOUR_OF_DAY, 1);
		expiryBefore = calendar2.getTime();
		removePremium.setPremiumExpiryDate(expiryBefore);
		removePremium.setCreditCard("1111222233334444");
		removePremium.setCreditcardType(CreditcardType.VISA);
		removePremium.setCreditcardName("Email Test");
		removePremium.setExpirationMonth("10");
		removePremium.setExpirationYear("2020");
		removePremium.setSecurityNumber("123");
		userDao.save(removePremium);
	}

	public User createUser(String email, String password, String firstName,
			String lastName, Gender gender, AccountType type) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		user.addUserRole(role);
		user.setAccountType(type);
		return user;
	}

	public User createUser(String email, String password, String firstName,
			String lastName, String picPath, Gender gender, AccountType type) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		UserRole role = new UserRole();
		UserPicture picture = new UserPicture();
		picture.setUser(user);
		picture.setFilePath(picPath);
		user.setPicture(picture);
		role.setRole("ROLE_USER");
		role.setUser(user);
		user.addUserRole(role);
		user.setAccountType(type);
		return user;
	}

	private String getDummyText() {
		return "I am a Master student from switzerland. I'm 25 years old, "
				+ "my hobbies are summer-sports, hiking, traveling and cooking. "
				+ "I enjoy spending time with friends, watching movies, "
				+ "going for drinks and organizing dinners. I have lived in Fräkmündegg, "
				+ "London and Zurich, always in flatshares and i have never had "
				+ "problems with my flatmates because I am a nice person.";
	}

}
