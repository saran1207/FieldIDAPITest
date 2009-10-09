package rfid.ejb.entity;

import java.io.File;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.EncryptionUtility;
import com.n4systems.util.timezone.CountryList;

@Entity
@Table(name = "users")
public class UserBean extends LegacyBeanTenantWithCreateModifyDate implements Listable<Long>, HasOwner, Saveable, SecurityEnhanced<UserBean> {
	private static final long serialVersionUID = 1L;
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(UserBean.class);
	}
	
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
	
	private boolean active = false;
	private boolean deleted = false;
	private boolean system = false;
	private boolean admin = false;
	
	
	
	@Column(name="permissions", nullable=false)
	private int permissions = 0; // permissions should always start out empty
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="owner_id", nullable = false)
	private BaseOrg owner;
	
	private Long externalId;
	
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

	public boolean isEmployee() {
		return owner.isInternal();
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

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
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
		return (owner.isExternal()) ? owner.getCustomerOrg().getId() : null;
	}
	
	@Deprecated
	public Long getR_EndUser() {
		return getCustomerId();
	}
	
	@Deprecated
	public Long getDivisionId() {
		return (owner.isDivision()) ? owner.getId() : null;
	}
	
	@Deprecated
	public Long getR_Division() {
		return getDivisionId();
	}
	
	public UserBean enhance(SecurityLevel level) {
		UserBean enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		return enhanced;
	}

} 
