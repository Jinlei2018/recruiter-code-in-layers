package se\kth\iv1201\app\recruiter\domain;

import java.util.Set;

public interface HolderDTO {

    long getPersonId();

	String getFirstName();

	String getLastName();

	String getSsn();

	String getEmail();

	String getPassword();

	Role getRole();

	String getUserName();

    String getName();
	
	Set<CompetenceProfileDTO> getCompetenceProfiles();
	
	Set<AvailabilityDTO> getAvailabilities();
}