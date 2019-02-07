package se\kth\iv1201\app\recruiter\domain;

import se.kth.iv1201.app.recruiter.util.Util;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "USER")
public class User implements UserDTO {
    private static final String SEQUENCE_NAME_KEY = "SEQ_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME_KEY)
    @SequenceGenerator(name = SEQUENCE_NAME_KEY, sequenceName = "RECRUITER_SEQUENCE")
	@Pattern(regexp = "^(19|20)?[0-9]{6}[- ]?[0-9]{4}$", message = "{user.personId.invalid-char}")
    @Column(name = "USER_PERSON_ID", nullable = false)
    private long personId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CompetenceProfile> competenceProfiles = new HashSet<>();
    private Set<Availability> Availabilities = new HashSet<>();	

    @NotNull(message = "{user.firstName.missing}")
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "{user.firstName.invalid-char}")
    @Size(min = 2, max = 30, message = "{user.firstName.length}")
    @Column(name = "FIRST_NAME")
    private String firstName;
	
	@NotNull(message = "{user.lastName.missing}")
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "{user.lastName.invalid-char}")
    @Size(min = 2, max = 30, message = "{user.lastName.length}")
    @Column(name = "LAST_NAME")
    private String lastName;
	
	@NotNull(message = "{user.ssn.missing}")
    @Pattern(regexp = "^\d{3}-\d{2}-\d{4}$", message = "{user.ssn.invalid-char}")
    @Size(min = 1, max = 60, message = "{user.ssn.length}")
    @Column(name = "SSN")
    private String ssn;
	
	@NotNull(message = "{user.email.missing}")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$", message = "{user.email.invalid-char}")
    @Size(min = 1, max = 60, message = "{user.email.length}")
    @Column(name = "EMAIL")
    private String email;
	
	@NotNull(message = "{user.password.missing}")
    @Pattern(regexp = "^[a-zA-Z]\w{3,14}$", message = "{user.password.invalid-char}")
    @Size(min = 1, max = 15, message = "{user.password.length}")
    @Column(name = "PASSWORD")
    private String password;
	
    @NotNull(message = "{user.role.missing}")
    @JoinColumn(name = "USER_ROLE")
    private Role role;	
	
	@NotNull(message = "{user.userName.missing}")
    @Pattern(regexp = "^[[A-Z]|[a-z]][[A-Z]|[a-z]|\\d|[_]]{7,29}$", message = "{user.userName.invalid-char}")
    @Size(min = 2, max = 30, message = "{user.userName.length}")
    @Column(name = "USER_NAME")
    private String userName;

    public User(long personId, String firstName, String lastName, String ssn, String email, String password, Role role, String username) {
        this.personId = personId;		       
		this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
    }

    protected User() {
    }

    @Override
    public Set<CompetenceProfileDTO> getCompetenceProfiles() {
        Set<CompetenceProfileDTO> copyOfCompProfiles = new HashSet<>();
        copyOfCompProfiles.addAll(competenceProfiles);
        return copyOfCompProfiles;
    }
    @Override	
	public Set<AvailabilityDTO> getAvailabilities() {
        Set<AvailabilityDTO> copyOfAvailabilities = new HashSet<>();
        copyOfAvailabilities.addAll(availabilities);
        return copyOfAvailabilities;
    }

    public void addCompetenceProfile(CompetenceProfile cp) {
        competenceProfiles.add(cp);
    }
	
    public void addAvailability(Availability ava) {
        availabilities.add(ava);
    }

    @Override	
    public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}
    @Override
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
    @Override
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    @Override
	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
    @Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    @Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    @Override
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
    @Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCompetenceProfiles(Set<CompetenceProfile> competenceProfiles) {
		this.competenceProfiles = competenceProfiles;
	}

	public void setAvailabilities(Set<Availability> availabilities) {
		Availabilities = availabilities;
	}	


    @Override
    public int hashCode() {
        return Long.valueOf(personId).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User)object;
        return this.personId == other.personId;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}