package se.kth.iv1201.app.recruiter.presentation.recruiterContr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.iv1201.app.recruiter.application.RecruiterService;
import se.kth.iv1201.app.recruiter.domain.UserDTO;
import se.kth.iv1201.app.recruiter.domain.IllegalRecruiterTransactionException;
import se.kth.iv1201.app.recruiter.presentation.error.ExceptionHandlers;

import javax.validation.Valid;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
@Scope("session")
public class RecruiterController {
    static final String DEFAULT_PAGE_URL = "/";
    static final String SELECT_USER_PAGE_URL = "select-user";
    static final String APPLICANT_VIEW_PAGE_URL = "applicant-page";
    static final String RECRUITER_VIEW_PAGE_URL = "recruiter-page";	
    static final String CREATE_USER_URL = "create-user";
	static final String LOGIN_USER_URL = "login-user";
	static final String ADD_COMPETENCE_PROFILE_URL ="add-competence-profile";
	static final String ADD_ AVAILABILITY_URL ="add-availability";
	static final String SEND_ APPLICATION_URL ="send-application";	
	static final String SEARCH_ FOR_APPLICATIONS_URL ="search-for-applications";
	static final String LIST_APPLICATIONS_PAGE_URL ="list-applications-page";
	static final String SHOW_ APPLICATION_PAGE_URL ="show-application-page";	
	static final String CHANGE_APPLICATION_STATUS_URL="change-application-status";		
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RecruiterController.class);
	
    private static final String CURRENT_ACCT_OBJ_NAME = "currentAcct";
    private static final String SEARCH_APPLICATIONS_FORM_OBJ_NAME = "searchApplicationsForm";
    private static final String WITHDRAW_FORM_OBJ_NAME = "withdrawForm";
    private static final String LOGIN_FORM_OBJ_NAME = "loginForm";
    private static final String CREATE_USER_FORM_OBJ_NAME = "createUserForm";
	private static final String CURRENT_USER_OBJ_NAME = "currentUser";
	private static final String NEW_COMPETENCE_PROFILE_OBJ_NAME = "newCompetenceProfile";	
	private static final String TEMP_COMPETENCE_PROFILES_SET_OBJ_NAME = "tempCompetenceProfiles";
	private static final String AVAILABILITIES_SET_OBJ_NAME = "availabilities";	
	private static final String AVAILABILITY_FORM_OBJ_NAME= "addAvailabilityForm";
	private static final String COMPETENCE_PROFILE_FORM_OBJ_NAME= "addCompetenceProfileForm";
	private static final String SEND_APPLICATION_FORM_OBJ_NAME= "sendApplicationForm";	
	private static final String SEARCH_RESULT_SET_OBJ_NAME= "searchResult";	
	private static final String SET_APPLICATION_STATUS_FORM_OBJ_NAME= "setApplicationStatusForm";	
	
    private static final String REG_MESSAGE_NAME = "register-message";	
	private static final String REG_MSG="register successully, please login!";

    @Autowired
    private RecruiterService service;
    private UserDTO currentUser;
	private CompetenceProfileDTO newCompetenceProfile;
	private AvailabilityDTO newAvailability, searchedAvailability;
	private ApplicationDTO newApplication, selectedApplication;	
	private DateDTO availabilityFromDate, availabilityToDate, applicationDate, workFromDate, workToDate, searchedApplicationDate;		
	private Set<CompetenceProfileDTO> tempCompetenceProfiles;
	private Set<AvailabilityDTO> tempAvailabilities;
	private Set<ApplicationDTO> searchResultByAvailability, searchResultByApplicationDate, searchResultByName, searchResult;	
	private int selectedApplicationId;
    private ApplicationStatusDTO initialStatus;	

	

    /**
     * No page is specified, redirect to the welcome page.
     *
     * @return A response that redirects the browser to the welcome page.
     */
    @GetMapping(DEFAULT_PAGE_URL)
    public String showDefaultView() {
        LOGGER.trace("Call to context root.");
        return "redirect:" + SELECT_USER_PAGE_URL;
    }

    @GetMapping("/" + SELECT_USER_PAGE_URL)
    public String showUserSelectionView(CreateUserForm createUserForm, LoginForm loginForm) {
        LOGGER.trace("Call to user selection view.");
        return SELECT_USER_PAGE_URL;
    }

	@GetMapping("/" + APPLICANT_VIEW_PAGE_URL)
    public String showApplicantView(Model model) {
        LOGGER.trace("Call to applicant view.");
        return showApplicantViewPage(model, new AddCompetenceProfileForm(), new AddAvailabilityForm(), new SendApplicationForm());
    }

	@GetMapping("/" + RECRUITER_VIEW_PAGE_URL)
    public String showRecruiterView(Model model) {
        LOGGER.trace("Call to recruiter view.");
        return showRecruiterViewPage(model, new SearchApplicationsForm());
    }
	
	@GetMapping("/" + LIST_APPLICATIONS_PAGE_URL)
    public String listApplicationsView(Model model) {
        LOGGER.trace("Call to list-applications-page.");
        return LIST_APPLICATIONS_PAGE_URL;
    }
	setApplicationStatusForm
	
	@GetMapping("/" + SHOW_ APPLICATION_PAGE_URL)
    public String showApplicationView(SetApplicationStatusForm setApplicationStatusForm) {
        LOGGER.trace("Call to show-application-page.");	
        return SHOW_ APPLICATION_PAGE_URL;
    }

    @PostMapping("/" + CREATE_USER_URL)
    public String createUser(@Valid CreateUserForm createUserForm, BindingResult bindingResult, Model model)
        throws IllegalRecruiterTransactionException {
        LOGGER.trace("Post of user creation data.");
        LOGGER.trace("Form data: " + createUserForm);
        if (bindingResult.hasErrors()) {
            model.addAttribute(LOGIN_FORM_OBJ_NAME, new LoginForm());
            return SELECT_USER_PAGE_URL;
        }
        newUser = service.createUser(createUserForm.getPersonId(), createUserForm.getFirstName(), createUserForm.getLastName(),createUserForm.getSSN(),createUserForm.getEmail(), createUserForm.getPassword(),createUserForm.getRoleId(), createUserForm.getUsername());
		model.addAttribute(REG_MESSAGE_NAME, REG_MSG);
        return SELECT_USER_PAGE_URL;
    }
	
	@PostMapping("/" + LOGIN_USER_URL)
    public String loginUser(@Valid LoginUserForm loginUserForm, BindingResult bindingResult, Model model) {
        LOGGER.trace("Post of user login data.");
        LOGGER.trace("Form data: " + loginUserForm);
        if (bindingResult.hasErrors()) {
            model.addAttribute(CREATE_USER_FORM_OBJ_NAME, new CreateUserForm());
            return SELECT_USER_PAGE_URL;
        }
		currentUser=service.userLogin(loginUserForm.getUsername(), loginUserForm.getPassword());
        if (currentUser == null) {
            model.addAttribute(ExceptionHandlers.ERROR_TYPE_KEY, ExceptionHandlers.LOGIN_FAILED);
            return ExceptionHandlers.SELECT_USER_PAGE_URL;
        }
	    if (currentUser.getRole().getRoleId()==1) {
			return showRecruiterViewPage(model, new SearchApplicationsForm());
        }		
		if (currentUser.getRole().getRoleId()()==2) {
			return showApplicantViewPage(model, new AddCompetenceProfileForm(), new AddAvailabilityForm());
        }
    }
	

    private String showRecruiterViewPage(Model model, SearchApplicationsForm searchApplicationsForm) {
        if (currentUser != null) {
		model.addAttribute(CURRENT_USER_OBJ_NAME, currentUser);
        }
        if (searchApplicationsForm != null) {
            model.addAttribute(SEARCH_APPLICATIONS_FORM_OBJ_NAME, searchApplicationsForm);
        }
		return RECRUITER_VIEW_PAGE_URL;
    }
	
	private String showApplicantViewPage(Model model, AddCompetenceProfileForm addCompetenceProfileForm, AddAvailabilityForm addAvailabilityForm, SendApplicationForm sendApplicationForm) {
        if (currentUser != null) {
		model.addAttribute(CURRENT_USER_OBJ_NAME, currentUser);
        }
        if (addCompetenceProfileForm != null) {
            model.addAttribute(ADD_COMPETENCE_PROFILE_FORM_OBJ_NAME, addCompetenceProfileForm);
        }
        if (addAvailabilityForm != null) {
            model.addAttribute(AVAILABILITY_FORM_OBJ_NAME, addAvailabilityForm);
        }		
	    if (sendApplicationForm != null) {
            model.addAttribute(SEND_APPLICATION_FORM_OBJ_NAME, sendApplicationForm);
        }	
		return APPLICANT_VIEW_PAGE_URL;
    }
	
	@PostMapping("/" + ADD_COMPETENCE_PROFILE_URL)
    public String createCompetenceProfile(@Valid AddCompetenceProfileForm addCompetenceProfileForm, BindingResult bindingResult, Model model) {
        LOGGER.trace("Post of competence profile data.");
        LOGGER.trace("Form data: " + addCompetenceProfileForm);
        if (bindingResult.hasErrors()) {
            model.addAttribute(AVAILABILITY_FORM_OBJ_NAME, new AddAvailabilityForm());
            model.addAttribute(SEND_APPLICATION_FORM_OBJ_NAME, new SendApplicationForm());			
            return APPLICANT_VIEW_PAGE_URL;
        }
		newCompetenceProfile=new CompetenceProfile(currentUser.getUserId(),addCompetenceProfileForm.getCompetenceId(), addCompetenceProfileForm.getYearsOfExperience());
		tempCompetenceProfiles.add(newCompetenceProfile);
		model.addAttribute(TEMP_COMPETENCE_PROFILES_SET_OBJ_NAME, tempCompetenceProfiles);
		model.addAttribute(AVAILABILITY_FORM_OBJ_NAME, new AddAvailabilityForm());
		model.addAttribute(SEND_APPLICATION_FORM_OBJ_NAME, new SendApplicationForm());			
		
        return APPLICANT_VIEW_PAGE_URL;
    }
	
	@PostMapping("/" + ADD_AVAILABILITY_URL)
    public String createAvailability(@Valid AddAvailabilityForm addAvailabilityForm, BindingResult bindingResult, Model model) {
        LOGGER.trace("Post of availability data.");
        LOGGER.trace("Form data: " + addAvailabilityForm);
        if (bindingResult.hasErrors()) {
            model.addAttribute(COMPETENCE_PROFILE_FORM_OBJ_NAME, new AddCompetenceProfileForm());
			model.addAttribute(SEND_APPLICATION_FORM_OBJ_NAME, new SendApplicationForm());			
            return APPLICANT_VIEW_PAGE_URL;
        }
		availabilityFromDate=service.getDate(addAvailabilityForm.getFromYear(), addAvailabilityForm.getFromMonth(), addAvailabilityForm.getFromDay());
		availabilityToDate=service.getDate(addAvailabilityForm.getToYear(), addAvailabilityForm.getToMonth(), addAvailabilityForm.getToDay());		
		newAvailability= new Availability(currentUser,availabilityFromDate, availabilityToDate);
		tempAvailabilities.add(newAvailability);
		model.addAttribute(AVAILABILITIES_SET_OBJ_NAME, availabilities);
        model.addAttribute(COMPETENCE_PROFILE_FORM_OBJ_NAME, new AddCompetenceProfileForm());
		model.addAttribute(SEND_APPLICATION_FORM_OBJ_NAME, new SendApplicationForm());			
        return APPLICANT_VIEW_PAGE_URL;
    }
	
	@PostMapping("/" + SEND_ APPLICATION_URL)
    public String sendApplication(SendApplicationForm sendApplicationForm, Model model) {
        LOGGER.trace("Send application.");
        LOGGER.trace("Form data: " + sendApplicationForm);
		service.addCompetenceProfiles(tempCompetenceProfiles);
		service.addAvailabilities(tempAvailabilities);
		applicationDate=service.getCurrentDate();
		initialStatus=new ApplicationStatus("0");
		newApplication=service.createApplication(currentUser, applicationDate, tempCompetenceProfiles, tempAvailabilities, initialStatus);
		model.addAttribute(TEMP_COMPETENCE_PROFILES_SET_OBJ_NAME, tempCompetenceProfiles);
		model.addAttribute(AVAILABILITY_FORM_OBJ_NAME, new AddAvailabilityForm());
		model.addAttribute(SEND_APPLICATION_FORM_OBJ_NAME, new SendApplicationForm());			
        return APPLICANT_VIEW_PAGE_URL;
    }
	

    @PostMapping("/" + SEARCH_ FOR_APPLICATIONS_URL)
    public String searchForApplications(@Valid SearchApplicationsForm searchApplicationsForm, BindingResult bindingResult, Model model)
        throws IllegalRecruiterTransactionException {
        LOGGER.trace("Post of applications searching data.");
        LOGGER.trace("Form data: " + searchApplicationsForm);
        if (bindingResult.hasErrors()) {
            return RECRUITER_VIEW_PAGE_URL;
        }
		if(searchApplicationsForm.getAvailabilityFromYear()!=null&&searchApplicationsForm.getAvailabilityFromMonth()!=null&&searchApplicationsForm.getAvailabilityFromDay()!=null&&searchApplicationsForm.getAvailabilityToYear()!=null&&searchApplicationsForm.getAvailabilityToMonth()!=null&&searchApplicationsForm.getAvailabilityToDay()!=null){
			workFromDate=service.getDate(searchApplicationsForm.getAvailabilityFromYear(), searchApplicationsForm.getAvailabilityFromMonth(),searchApplicationsForm.getAvailabilityFromDay());
			workToDate=service.getDate(searchApplicationsForm.getAvailabilityToYear(), searchApplicationsForm.getAvailabilityToMonth(),searchApplicationsForm.getAvailabilityToDay());
			searchedAvailability=new Availability(currentUser, workFromDate, workToDate);
			searchResultByAvailability=service.findApplicationsByAvailability(searchedAvailability);			
		}else{
			searchResultByAvailability=service.getAllApplications();
		}
		if(searchApplicationsForm.getApplicationYear()!=null&&searchApplicationsForm.getApplicationMonth()!=null&&searchApplicationsForm.getApplicationDay()!=null){
			searchedApplicationDate=new Date(searchApplicationsForm.getApplicationYear(), searchApplicationsForm.getApplicationMonth(),searchApplicationsForm.getApplicationDay());
			searchResultByApplicationDate=service.findApplicationsByApplicationDate(searchedApplicationDate);			
		}else{
			searchResultByApplicationDate=service.getAllApplications();
		}
		if(searchApplicationsForm.getCompetenceId()!=null){
			searchResultByCompetence=service.findApplicationsByCompetenceId(searchApplicationsForm.getCompetenceId());			
		}else{
			searchResultByApplicationDate=service.getAllApplications();
		}
		if(searchApplicationsForm.getName()!=null){
			searchResultByName=service.findApplicationsByName(searchApplicationsForm.getName());			
		}else{
			searchResultByName=service.getAllApplications();
		}
		searchResult.clear();
        searchResult.addAll(searchResultByAvailability);
		searchResult.retainAll(searchResultByApplicationDate);
		searchResult.retainAll(searchResultByCompetence);
		searchResult.retainAll(searchResultByName);		
		model.addAttribute(SEARCH_RESULT_SET_OBJ_NAME, searchResult);
        return LIST_APPLICATIONS_PAGE_URL;
    }
	
	@RequestMapping("/" + SHOW_ APPLICATION_URL)
	public String show-application(HttpServletRequest request) {
		String applicationId=request.getParameter("applicationId");
		LOGGER.info(applicationId);
		selectedApplicationId=Integer.parseInt(applicationId);
		selectedApplication=service.findApplicationById(selectedApplicationId);
		model.addAttribute(SELECTED_APPLICATION_OBJ_NAME, selectedApplication);	
		return showApplicationViewPage(model, new SetApplicationStatusForm());
	}
	
	private String showApplicationViewPage(Model model, SetApplicationStatusForm setApplicationStatusForm) {
        if (currentUser != null) {
			model.addAttribute(CURRENT_USER_OBJ_NAME, currentUser);
        }
        if (setApplicationStatusForm != null) {
            model.addAttribute(SET_APPLICATION_STATUS_FORM_OBJ_NAME, setApplicationStatusForm);
        }
		return SHOW_ APPLICATION_PAGE_URL;
    }
	
	@PostMapping("/" + CHANGE_APPLICATION_STATUS_URL)
    public String changeApplicationStatus(@Valid SetApplicationStatusForm setApplicationStatusForm, BindingResult bindingResult, Model model) {
        LOGGER.trace("Post of changed status data.");
        LOGGER.trace("Form data: " + setApplicationStatusForm);
        if (bindingResult.hasErrors()) {
            return showApplicationViewPage(model, setApplicationStatusForm);
        }
		service.changeApplicationStatus(selectedApplication, setApplicationStatusForm.getStatusId());
		updateSelectedApplication();
		return showApplicationViewPage(model, setApplicationStatusForm);
    }
	
	private void updateSelectedApplication() {
        selectedApplication = service.findApplicationById(selectedApplication.getId());
    }
}
