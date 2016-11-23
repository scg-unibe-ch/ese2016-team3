package ch.unibe.ese.team3.controller;

import java.security.Principal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team3.controller.pojos.forms.MessageForm;
import ch.unibe.ese.team3.controller.pojos.forms.SignupForm;
import ch.unibe.ese.team3.controller.pojos.forms.GoogleSignupForm;
import ch.unibe.ese.team3.controller.pojos.forms.UpgradeForm;
import ch.unibe.ese.team3.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team3.controller.service.AdService;
import ch.unibe.ese.team3.controller.service.MessageService;
import ch.unibe.ese.team3.controller.service.SignupService;
import ch.unibe.ese.team3.controller.service.GoogleSignupService;
import ch.unibe.ese.team3.controller.service.GoogleLoginService;
import ch.unibe.ese.team3.controller.service.UpgradeService;
import ch.unibe.ese.team3.controller.service.UserService;
import ch.unibe.ese.team3.controller.service.UserUpdateService;
import ch.unibe.ese.team3.controller.service.VisitService;
import ch.unibe.ese.team3.enums.PageMode;
import ch.unibe.ese.team3.controller.service.PremiumChoiceService;
import ch.unibe.ese.team3.model.AccountType;
import ch.unibe.ese.team3.model.Ad;
import ch.unibe.ese.team3.model.BuyMode;
import ch.unibe.ese.team3.model.CreditcardType;
import ch.unibe.ese.team3.model.Gender;
import ch.unibe.ese.team3.model.User;
import ch.unibe.ese.team3.model.Visit;
import ch.unibe.ese.team3.model.PremiumChoice;
import ch.unibe.ese.team3.model.Type;

/**
 * Handles all requests concerning user accounts and profiles.
 */
@Controller
@EnableScheduling
public class ProfileController {
	
	@Autowired
	@Qualifier("authenticationManager")
	protected AuthenticationManager authenticationManager;
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private GoogleSignupService googleSignupService;
	
	@Autowired
	private GoogleLoginService googleLoginService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserUpdateService userUpdateService;

	@Autowired
	private VisitService visitService;

	@Autowired
	private AdService adService;
	
	@Autowired
	private UpgradeService upgradeService;

	@Autowired
	private PremiumChoiceService premiumChoiceService;

	/** Returns the login page. */
	@RequestMapping(value = "/login")
	public ModelAndView loginPage() {
		ModelAndView model = new ModelAndView("login");
		model.addObject("googleForm", new GoogleSignupForm());
		return model;
	}
	
	/** Handles Google sign in. */
	@RequestMapping(value = "/googlelogin", method = RequestMethod.POST)
	public ModelAndView googleLogin(GoogleSignupForm googleForm) {
		ModelAndView model = new ModelAndView("index");
		if(!googleSignupService.doesUserWithUsernameExist(googleForm.getEmail())){
			googleSignupService.saveFrom(googleForm);
		}
		googleLoginService.loginFrom(googleForm);
		model.addObject("newest", adService.getNewestAds(4, BuyMode.BUY));
		model.addObject("types", Type.values());
		model.addObject("searchForm", new SearchForm());
		return model;
	}
	

	/** Returns the signup page. */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signupPage() {
		ModelAndView model = new ModelAndView("signup");
		model.addObject("signupForm", new SignupForm());
		model.addObject("genders", Gender.valuesForDisplay());
		model.addObject("accountTypes", AccountType.values());
		model.addObject("creditcardTypes", CreditcardType.valuesForDisplay());
		model.addObject("years", GetYears());
		model.addObject("months", GetMonths());
		
		Iterable<PremiumChoice> allChoices = premiumChoiceService.findAll();
		model.addObject("premiumChoices", allChoices);
		model.addObject("durations", premiumChoiceService.getDurations());
		return model;
	}

	private List<Integer> GetMonths() {
		ArrayList<Integer> months = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++){
			months.add(i);
		}
		return months;
	}

	private List<Integer> GetYears() {
		ArrayList<Integer> years = new ArrayList<Integer>();
		int year = Year.now().getValue();
		for (int i = 0; i < 10; i++){
			years.add(new Integer(year + i));
		}
		return years;
	}

	/** Validates the signup form and on success persists the new user. */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView signupResultPage(@Valid SignupForm signupForm,
			BindingResult bindingResult) {
		ModelAndView model;
		if (!bindingResult.hasErrors()) {
			signupService.saveFrom(signupForm);
			model = new ModelAndView("login");
			model.addObject("googleForm", new GoogleSignupForm());
			model.addObject("confirmationMessage", "Signup complete!");
		} else {
			model = new ModelAndView("signup");
			model.addObject("signupForm", signupForm);
			model.addObject("genders", Gender.valuesForDisplay());
			model.addObject("accountTypes", AccountType.values());
			model.addObject("creditcardTypes", CreditcardType.valuesForDisplay());
			model.addObject("years", GetYears());
			model.addObject("months", GetMonths());
			
			Iterable<PremiumChoice> allChoices = premiumChoiceService.findAll();
			model.addObject("premiumChoices", allChoices);
			model.addObject("durations", premiumChoiceService.getDurations());
		}
		
		return model;
	}

	/** Checks and returns whether a user with the given email already exists. */
	@RequestMapping(value = "/signup/doesEmailExist", method = RequestMethod.POST)
	public @ResponseBody boolean doesEmailExist(@RequestParam String email) {
		return signupService.doesUserWithUsernameExist(email);
	}

	/** Shows the edit profile page. */
	@RequestMapping(value = "/profile/editProfile", method = RequestMethod.GET)
	public ModelAndView editProfilePage(Principal principal) {
		ModelAndView model = new ModelAndView("editProfile");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		model.addObject("editProfileForm", new EditProfileForm());
		model.addObject("currentUser", user);
		return model;
	}

	/** Handles the request for editing the user profile. */
	@RequestMapping(value = "/profile/editProfile", method = RequestMethod.POST)
	public ModelAndView editProfileResultPage(
			@Valid EditProfileForm editProfileForm,
			BindingResult bindingResult, Principal principal) {
		ModelAndView model;
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (!bindingResult.hasErrors()) {
			userUpdateService.updateFrom(editProfileForm, user);
			Authentication request = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
			model = new ModelAndView("redirect:../user?id=" + user.getId());
			return model;
		} else {
			model = new ModelAndView("editProfile");
			model.addObject("editProfileForm", editProfileForm);
			model.addObject("currentUser", user);
			return model;
		}
	}

	/** Displays the public profile of the user with the given id. */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView user(@RequestParam("id") long id, Principal principal) {
		ModelAndView model = new ModelAndView("user");
		User user = userService.findUserById(id);
		if (principal != null) {

			String username = principal.getName();
			User user2 = userService.findUserByUsername(username);
			if (user2 == null) {
				user2 = user;
			}
			long principalID = user2.getId();
			model.addObject("principalID", principalID);
		}
		model.addObject("user", user);
		model.addObject("messageForm", new MessageForm());
		return model;
	}

	/** Displays the schedule page of the currently logged in user. */
	@RequestMapping(value = "/profile/schedule", method = RequestMethod.GET)
	public ModelAndView schedule(Principal principal) {
		ModelAndView model = new ModelAndView("schedule");
		User user = userService.findUserByUsername(principal.getName());

		// visits, i.e. when the user sees someone else's property
		Iterable<Visit> visits = visitService.getVisitsForUser(user);
		model.addObject("visits", visits);

		// presentations, i.e. when the user presents a property
		Iterable<Ad> usersAds = adService.getAdsByUser(user);
		ArrayList<Visit> usersPresentations = new ArrayList<Visit>();

		for (Ad ad : usersAds) {
			try {
				usersPresentations.addAll((ArrayList<Visit>) visitService
						.getVisitsByAd(ad));
			} catch (Exception e) {
			}
		}

		model.addObject("presentations", usersPresentations);
		return model;
	}

	/** Returns the visitors page for the visit with the given id. */
	@RequestMapping(value = "/profile/visitors", method = RequestMethod.GET)
	public ModelAndView visitors(@RequestParam("visit") long id) {
		ModelAndView model = new ModelAndView("visitors");
		Visit visit = visitService.getVisitById(id);
		Iterable<User> visitors = visit.getSearchers();

		model.addObject("visitors", visitors);

		Ad ad = visit.getAd();
		model.addObject("ad", ad);
		return model;
	}
	
	/** Returns the upgrade page. */
	@RequestMapping(value = "/profile/upgrade", method = RequestMethod.GET)
	public ModelAndView upgradePage(Principal principal) {
		ModelAndView model = new ModelAndView("upgrade");
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		Iterable<PremiumChoice> allChoices = premiumChoiceService.findAll();
		model.addObject("upgradeForm", new UpgradeForm());
		model.addObject("creditcardTypes", CreditcardType.valuesForDisplay());
		model.addObject("accountTypes", AccountType.values());
		model.addObject("currentUser", user);
		model.addObject("years", GetYears());
		model.addObject("months", GetMonths());
		model.addObject("premiumChoices", allChoices);
		model.addObject("durations", premiumChoiceService.getDurations());
		return model;
	}

	/** Validates the upgrade form and on success persists the new user. */
	@RequestMapping(value = "/profile/upgrade", method = RequestMethod.POST)
	public ModelAndView upgradeResultPage(@Valid UpgradeForm upgradeForm,
			BindingResult bindingResult, Principal principal) {
		ModelAndView model;
		String username = principal.getName();
		User user = userService.findUserByUsername(username);
		if (!bindingResult.hasErrors()) {
			PremiumChoice premiumChoice = premiumChoiceService.findPremiumChoiceByDuration(upgradeForm.getDuration());
			upgradeService.upgradeFrom(upgradeForm, user, premiumChoice);
			user = userService.findUserByUsername(username);
			model = new ModelAndView("redirect:../user?id=" + user.getId());
			return model;
		} else {
			Iterable<PremiumChoice> allChoices = premiumChoiceService.findAll();
			model = new ModelAndView("upgrade");
			model.addObject("upgradeForm", upgradeForm);
			model.addObject("creditcardTypes", CreditcardType.valuesForDisplay());
			model.addObject("accountTypes", AccountType.values());
			model.addObject("currentUser", user);
			model.addObject("years", GetYears());
			model.addObject("months", GetMonths());
			model.addObject("premiumChoices", allChoices);
			model.addObject("durations", premiumChoiceService.getDurations());
		}
		return model;
	}

}
