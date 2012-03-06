package com.n4systems.model.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.utils.GlobalID;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.tools.EncryptionUtility;
import com.n4systems.util.RandomString;
import com.n4systems.util.timezone.CountryList;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "users")
public class User extends ArchivableEntityWithOwner implements Listable<Long>, Saveable, SecurityEnhanced<User>, Exportable {
	private static final long serialVersionUID = 1L;
	public static final int REFERRAL_KEY_LENGTH = 10;
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(User.class);
	}
	
	private String globalId;
	private String userID;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String timeZoneID;
	private String hashPassword;
	private String position;
	private String initials;
	private String referralKey;
	private String resetPasswordKey;
	private int failedLoginAttempts;
	private String hashSecurityCardNumber;
	private boolean locked;
	private Date lockedUntil;
	private Date passwordChanged;
    private boolean displayLastRunSearches;

    @OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "lastRunReportId")
    private EventReportCriteriaModel lastRunReport;

    @OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "lastRunSearchId")
    private AssetSearchCriteriaModel lastRunSearch;

	@Column(nullable = false, unique = true)
	private String authKey;
	
	@Column(name="password", nullable=false)
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="user_previous_pw", joinColumns = @JoinColumn(name="userId"))
	@OrderBy("id")
	private List<String> previousPasswords = new ArrayList<String>();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private UserType userType=UserType.ALL;

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name="users_saved_items", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="item_id"))
    @IndexColumn(name="orderIdx")
    private List<SavedItem> savedItems;
	
	private boolean registered = false;
	
	// TODO DD : refactor this to use PermissionType.
	@Column(name="permissions", nullable=false)
	private int permissions = Permissions.NO_PERMISSIONS;
	
	private Long externalId;
	
	@Override
	protected void onCreate() {
		super.onCreate();
		onChange();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		onChange();
	}

	protected void generateGlobalIdIfNull() {
		if (globalId == null) {
			globalId = GlobalID.getId();
		}
	}
	
	protected void generateAuthKey() {
		if (authKey == null) {
			authKey = UUID.randomUUID().toString();
		}
	}
	
	private void onChange() {
		trimNames();
		generateReferralKeyIfNull();
		generateGlobalIdIfNull();
		generateAuthKey();
	}		
	
	private void trimNames() {
		this.userID = (userID != null) ? userID.trim() : null;
		this.firstName = (firstName != null) ? firstName.trim() : null;
		this.lastName = (lastName != null) ? lastName.trim() : null;
	}
	
	private void generateReferralKeyIfNull() {
		if (referralKey == null) {
			referralKey = RandomString.getString(REFERRAL_KEY_LENGTH);
		}
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isEmployee() {
		return userType.equals(UserType.FULL) || userType.equals(UserType.ADMIN) || userType.equals(UserType.LITE);
	}
	
	public boolean isFullUser(){
		return userType.equals(UserType.FULL);
	}
	
	public boolean isLiteUser() {
		return userType.equals(UserType.LITE);
	}
	
	public boolean isReadOnly(){
		return userType.equals(UserType.READONLY);
	}

	public void archiveUser() {
		userID = null;
		archiveEntity();
	}
	
	public boolean isSystem() {
		return userType.equals(UserType.SYSTEM);
	}

	public boolean isAdmin() {
		return userType.equals(UserType.ADMIN);
	}

	public int getPermissions() {
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public String getTimeZoneID() {
		return timeZoneID;
	}

	public void setTimeZoneID(String timeZoneID) {
		this.timeZoneID = timeZoneID;
	}

	/**
	 * Converts the plain text password into its encrypted version and stores
	 * that in the hashPassword field
	 * 
	 * @param plainTextPassword
	 */
	public void assignPassword(String plainTextPassword) {
		this.hashPassword = hashPassword(plainTextPassword);
	}
	
	/**
	 * call this when you are giving the user a new password. (as opposed to just assigning it via some User creator for example).
	 * it takes care of associated housekeeping. 
	 */
	public void updatePassword(String plainTextPassword) {
		addPreviousPassword();
		assignPassword(plainTextPassword);
		setPasswordChanged(new Date());
		clearResetPasswordKey();
		unlock();
	}

	public void assignSecruityCardNumber(String rfidNumber) {
		this.hashSecurityCardNumber = (rfidNumber == null || rfidNumber.length() == 0) ? null : hashSecurityCardNumber(rfidNumber);
	}

	public String getUserLabel() {
		return getFirstName() + " " + getLastName();
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public String getReferralKey() {
		return referralKey;
	}

	public void setReferralKey(String referralKey) {
		this.referralKey = referralKey;
	}

	public String createResetPasswordKey() {
		return resetPasswordKey = EncryptionUtility.getSHA1HexHash( emailAddress + UUID.randomUUID().toString());
	}

	public String getResetPasswordKey() {
		return resetPasswordKey;
	}
	
	public void clearResetPasswordKey() {
		resetPasswordKey = null;
	}
	
	public boolean matchesPassword(String password) {
		return (this.hashPassword.equals(hashPassword(password)));
	}
	
	public boolean matchesSecurityCardNumber(String rfidNumber) {
		if (rfidNumber == null) { return false; }
		return (this.hashSecurityCardNumber.equals(hashSecurityCardNumber(rfidNumber)));
	}

	@Override
	public String getDisplayName() {
		return getUserLabel();
	}

	@Override
    public String toString() {
	    return String.format("%s (%d) {%s}", getUserID(), getId(), getTenant());
    }

	public String getHashSecurityCardNumber() {
		return hashSecurityCardNumber;
	}
	
	public static String hashSecurityCardNumber(String rfidNumber) {
		return EncryptionUtility.getSHA1HexHash(rfidNumber.toUpperCase());
	}
	
	public static String hashPassword(String plainTextPassword) {
		return EncryptionUtility.getSHA1HexHash(plainTextPassword);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return equals((User) obj);
		} else {
			return super.equals(obj);
		}
	}
	
	public boolean equals(User user) {
		if (getId() == null && user.getId() == null) {
			return super.equals(user);
		} else {
			return getId().equals(user.getId());
		}
	}
	
	@Override
	public int hashCode() {
		if (getId() == null) {
			return super.hashCode();
		} else {
			return getId().intValue();
		}
	}

	public TimeZone getTimeZone() { 
		return CountryList.getInstance().getRegionByFullId(timeZoneID).getTimeZone();
	}

	@Override
	public boolean isNew() {
		return (getId() == null);
	}


	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}
	
	public File getPrivateDir() {
		return PathHandler.getUserPrivateDir(this);
	}
	
	public SecurityFilter getSecurityFilter() {
		return new UserSecurityFilter(this);
	}
	
	@Deprecated
	public Long getCustomerId() {
		return (getOwner().isExternal()) ? getOwner().getCustomerOrg().getId() : null;
	}	
	
	@Override
	public User enhance(SecurityLevel level) {
		User enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		return enhanced;
	}

	public boolean isPasswordAssigned() {
		return hashPassword != null;
	}

	public void setUserType(UserType userType){
		this.userType = userType;
	}

	public UserType getUserType() {
		return userType;
	}

	@Override
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	@Override
	public String getGlobalId() {
		return globalId;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public Integer getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLockedUntil(Date lockedUntil) {
		this.lockedUntil = lockedUntil;
	}

	public Date getLockedUntil() {
		return lockedUntil;
	}

	public void unlock() {
		setLocked(false);
		setLockedUntil(null);
		setFailedLoginAttempts(0);		
	}

	public void setPasswordChanged(Date passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	public Date getPasswordChanged() {
		return passwordChanged;
	}

	public void setPreviousPasswords(List<String> previousPasswords) {
		this.previousPasswords = previousPasswords;
	}

	public List<String> getPreviousPasswords() {
		return previousPasswords;
	}
	
	public void addPreviousPassword() {
		if (hashPassword!=null) { 
			previousPasswords.add(hashPassword);
		}
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

    public List<SavedItem> getSavedItems() {
        return savedItems;
    }

    public void setSavedItems(List<SavedItem> savedItems) {
        this.savedItems = savedItems;
    }

    public boolean isDisplayLastRunSearches() {
        return displayLastRunSearches;
    }

    public void setDisplayLastRunSearches(boolean displayLastRunSearches) {
        this.displayLastRunSearches = displayLastRunSearches;
    }

    public EventReportCriteriaModel getLastRunReport() {
        return lastRunReport;
    }

    public void setLastRunReport(EventReportCriteriaModel lastRunReport) {
        this.lastRunReport = lastRunReport;
    }

    public AssetSearchCriteriaModel getLastRunSearch() {
        return lastRunSearch;
    }

    public void setLastRunSearch(AssetSearchCriteriaModel lastRunSearch) {
        this.lastRunSearch = lastRunSearch;
    }
}
