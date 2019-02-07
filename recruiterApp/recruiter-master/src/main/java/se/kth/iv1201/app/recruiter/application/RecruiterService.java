package se.kth.iv1201.app.recruiter.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.kth.iv1201.app.recruiter.domain.User;
import se.kth.iv1201.app.recruiter.domain.UserDTO;
import se.kth.iv1201.app.recruiter.domain.CompetenceProfile;
import se.kth.iv1201.app.recruiter.domain.CompetenceProfileDTO;
import se.kth.iv1201.app.recruiter.domain.Availability;
import se.kth.iv1201.app.recruiter.domain.AvailabilityDTO;
import se.kth.iv1201.app.recruiter.domain.Application;
import se.kth.iv1201.app.recruiter.domain.ApplicationDTO;
import se.kth.iv1201.app.recruiter.domain.IllegalRecruiterTransactionException;
import se.kth.iv1201.app.recruiter.repository.UserRepository;
import se.kth.iv1201.app.recruiter.repository.CompetenceProfileRepository;
import se.kth.iv1201.app.recruiter.repository.AvailabilityRepository;



@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service
public class RecruiterService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CompetenceProfileRepository competenceProfileRepo;	
    @Autowired
    private AvailabilityRepository availabilityRepo;	
    @Autowired
    private ApplicationRepository applicationRepo;		
	
    public UserDTO createUser(long personId, String firstName, String lastName, String ssn, String email, String password, int roleId, String username) throws IllegalRecruiterTransactionException {
        Role roleEntity = roleRepo.findRoleByRoleId(roleId);
        if (roleEntity == null) {
            throw new IllegalRecruiterTransactionException("Role does not exist," + " role: " + role);
        }
        return userRepo.save(new User(personId, firstName, lastName, ssn, email, password, roleEntity, username));
    }	
	
	public UserDTO userLogin(String username, String password) throws IllegalRecruiterTransactionException {
        UserDTO user= userRepo.findUserByUsernameAndPassword(username, password);
        if (userEntity == null) {
            throw new IllegalRecruiterTransactionException("User does not exist," + " user: " + username);
        }
        return user;
    }	
	
    public Set<CompetenceProfileDTO> addCompetenceProfiles(Set<CompetenceProfileDTO> competenceProfiles) {
        return competenceProfileRepo.save(competenceProfiles);
    }	
	
    public Set<AvailabilityDTO> addAvailabilities(Set<AvailabilityDTO> availabilities) {
        return availabilityRepo.save(competenceProfiles);
    }	
    
	public Date getDate(int year, int month, int day) {
		Date currenDate=new Date(year-1900, month-1, day);
		return currenDate;
    }

	
	public Date getCurrentDate() {
		int y,m,d; 
		Calendar cal=Calendar.getInstance();    
		y=cal.get(Calendar.YEAR);    
		m=cal.get(Calendar.MONTH);    
		d=cal.get(Calendar.DATE);  
		Date currenDate=getDate(y, m, d);
		return currenDate;
    }

	public ApplicationDTO createApplication(User user, Date applicationDate, Set<CompetenceProfile> competenceProfiles, Set<Availability> availabilities, ApplicationStatus applicationStatus) throws IllegalRecruiterTransactionException {
        if (user != null&&applicationDate!= null&&competenceProfiles!= null&&availabilities!= null&&applicationStatus!= null) {
            return applicationRepo.save(new Application(user, applicationDate, competenceProfiles, availabilities, applicationStatus));
        }
    }

		
	public Set<ApplicationDTO> getAllApplications() {
		    return applicationRepo.findAll();
    }		
	
	public Set<ApplicationDTO> findApplicationsByAvailability(Availability availability) {
		    return applicationRepo.findApplicationsByAvailability(availability);
    }	

	public Set<ApplicationDTO> findApplicationsByApplicationDate(Date applicationDate) {
		    return applicationRepo.findApplicationsByApplicationDate(applicationDate);
    }	

	public Set<ApplicationDTO> findApplicationsByCompetenceId(int competenceId) {
		    return applicationRepo.findApplicationsByCompetenceId(competenceId);
    }	

	public Set<ApplicationDTO> findApplicationsByName(String name) {
		    return applicationRepo.findApplicationsByName(name);
    }		
	
	public Set<ApplicationDTO> findApplicationById(long id) {
		    return applicationRepo.findApplicationById(id);
    }
	
	public void changeApplicationStatus(Application app, Status status(){
		    return app.setApplicationStatus(status);
    }	
}
