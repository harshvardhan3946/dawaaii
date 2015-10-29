package com.dawaaii.service.user.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.dawaaii.service.auth.model.AccessToken;
import com.dawaaii.service.common.model.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.dawaaii.service.hash.Encoder;
import com.dawaaii.service.security.model.Role;
/**
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "User")
public class User extends BaseEntity {

    @Column(name = "username")
    private String userName;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "passwordsalt")
    private String passwordSalt;

    @Column(name = "passwordhash")
    private String passwordHash;

    @Column(name = "lastlogin")
    private Date lastLogin;

    @Column(name = "initials", length = 64)
    private String initials;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "user")
    private List<AccessToken> accessTokens = new ArrayList<>();

    @Column(name = "emailconfirmed", nullable = false)
    private boolean emailConfirmed;
    
    @Column(name = "addressline1")
    private String addressLine1;
   
    @Column(name = "addressline2")
    private String addressLine2;
    
    @Column(name = "phonenumber")
    private String phoneNumber;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "zippostalcode")
    private String zipPostalCode;
    
    @Column(name = "state")
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "creationsource", nullable=false)
    private UserCreationSource creationSource;
    
    @Column(name="password", nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "roles_id"),
    uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "roles_id" }))
    private List<Role> roles;
    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public void generatePasswordHash() {
        byte[] bytes = new byte[20];
        new SecureRandom().nextBytes(bytes);
        String passwordSalt = bytes.toString();
        generatePasswordHashUsingSalt(passwordSalt);
    }

    public void generatePasswordHashUsingSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
        this.passwordHash = Encoder.hash(password, passwordSalt);
    }

    public void addRole(Role role){
        if(roles == null) roles = new ArrayList<Role>();
        roles.add(role);
    }

    public void addAccessToken(AccessToken accessToken) {
        accessTokens.add(accessToken);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(List<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public UserCreationSource getCreationSource() {
		return creationSource;
	}

	public void setCreationSource(UserCreationSource creationSource) {
		this.creationSource = creationSource;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                '}';
    }
}