package se.kth.iv1201.app.recruiter.presentation.recruiterContr;

import se.kth.iv1201.app.recruiter.util.Util;

import javax.validation.constraints.NotNull;

/**
 * A form bean for the creat user form.
 */
class CreatUserForm {
    @NotNull(message = "{creat-user.personId.missing}")
    private Long personId;

    @NotNull(message = "{user.firstName.missing}")
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "{user.firstName.invalid-char}")
    @Size(min = 2, max = 30, message = "{user.firstName.length}")
    private String firstName;
	
	@NotNull(message = "{user.lastName.missing}")
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "{user.lastName.invalid-char}")
    @Size(min = 2, max = 30, message = "{user.lastName.length}")
    private String lastName;
	
	@NotNull(message = "{user.ssn.missing}")
    @Pattern(regexp = "^\d{3}-\d{2}-\d{4}$", message = "{user.ssn.invalid-char}")
    @Size(min = 1, max = 60, message = "{user.ssn.length}")
    private String ssn;
	
	@NotNull(message = "{user.email.missing}")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$", message = "{user.email.invalid-char}")
    @Size(min = 1, max = 60, message = "{user.email.length}")
    private String email;
	
	@NotNull(message = "{user.password.missing}")
    @Pattern(regexp = "^[a-zA-Z]\w{3,14}$", message = "{user.password.invalid-char}")
    @Size(min = 1, max = 15, message = "{user.password.length}")
    private String password;
	
    @NotNull(message = "{user.role.missing}")
    private String roleId;	
	
	@NotNull(message = "{user.userName.missing}")
    @Pattern(regexp = "^[[A-Z]|[a-z]][[A-Z]|[a-z]|\\d|[_]]{7,29}$", message = "{user.userName.invalid-char}")
    @Size(min = 2, max = 30, message = "{user.userName.length}")
    private String userName;
	
    public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


    @Override
    public String toString() {
        return Util.toString(this);
    }
}
