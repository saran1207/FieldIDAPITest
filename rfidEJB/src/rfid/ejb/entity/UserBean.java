package rfid.ejb.entity;

import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.n4systems.model.api.HasOrganization;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.tools.EncryptionUtility;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.timezone.CountryList;

@Entity
@Table(name = "users")
public class UserBean extends LegacyBeanTenantWithCreateModifyDate implements Listable<Long>, FilteredEntity, Saveable, HasOrganization {
	private static final long serialVersionUID = 1L;
	
	private String modifiedBy;
	private String userID;
	private String archivedUserID;
	private String firstName;
	private String lastName;
	private String emailAddress;	
	private String timeZoneID;
	private String hashPassword;
	private String position;
	private String initials;
	
	private String resetPasswordKey;
	private String hashSecurityCardNumber;
	
	// XXX - this will be removed when Customer becomes a subclass of OrganizationUnit
	private Long r_EndUser;
	private Long r_Division;
	
	private boolean active = false;
	private boolean deleted = false;
	private boolean system = false;
	private boolean admin = false;
	
	@Column(name="permissions", nullable=false)
	private int permissions = 0; // permissions should always start out empty
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private BaseOrg organization;
	
	@PrePersist
    protected void prePersist() {
		trimNames();
		super.prePersist();
    }
    
	@PreUpdate
	protected void preMerge() {
		super.preMerge();
		trimNames();
    }
	
	
	private void trimNames() {
		this.userID = (userID != null) ? userID.trim() : null;
		this.firstName = (firstName != null) ? firstName.trim() : null;
		this.lastName = (lastName != null) ? lastName.trim() : null;
	}
	
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets("tenant.id", "r_EndUser", "r_Division", "uniqueID", null);
	}
	
	public String getModifiedBy() {
		return modifiedBy;
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

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Long getR_EndUser() {
		return r_EndUser;
	}

	public void setR_EndUser(Long endUser) {
		r_EndUser = endUser;
	}
	
	public boolean isEmployee() {
		return r_EndUser == null;
	}

	public Long getR_Division() {
		return r_Division;
	}

	public void setR_Division(Long division) {
		r_Division = division;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void archiveUser() {
		archivedName();
		deleted = true;
	}
	
	private void archivedName() {
		archivedUserID = userID;
		userID = null;
	}
	

	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
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

	public void assignSecruityCardNumber(String rfidNumber) {
		this.hashSecurityCardNumber = (rfidNumber == null) ? null : hashSecurityCardNumber(rfidNumber);
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

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	
	public void createResetPasswordKey() {
		resetPasswordKey = EncryptionUtility.getSHA1HexHash( emailAddress + System.currentTimeMillis() );
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

	public String getDisplayName() {
		return getUserLabel();
	}

	public Long getId() {
		return getUniqueID();
	}

	@Override
    public String toString() {
	    return getUserID() + " (" + getUniqueID() + ")";
    }
	
	public Long getCustomerId() {
		return getR_EndUser();
	}

	public Long getDivisionId() {
		return getR_Division();
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
	
	public boolean equals(Object obj) {
		if (obj instanceof UserBean) {
			return equals((UserBean) obj);
		} else {
			return super.equals(obj);
		}
	}
	
	public boolean equals(UserBean user) {
		if (getUniqueID() == null && user.getUniqueID() == null) {
			return super.equals(user);
		} else {
			return getUniqueID().equals(user.getUniqueID());
		}
		
	}
	
	@Override
	public int hashCode() {
		if (getUniqueID() == null) {
			return super.hashCode();
		} else {
			return getUniqueID().intValue();
		}
	}
	
	public TimeZone getTimeZone() { 
		return CountryList.getInstance().getRegionByFullId(timeZoneID).getTimeZone();
	}

	public String getArchivedUserID() {
		return archivedUserID;
	}

	public boolean isNew() {
		return (getUniqueID() == null);
	}
	
	public BaseOrg getOrganization() {
		return organization;
	}

	public void setOrganization(BaseOrg organization) {
		this.organization = organization;
	}
} 
