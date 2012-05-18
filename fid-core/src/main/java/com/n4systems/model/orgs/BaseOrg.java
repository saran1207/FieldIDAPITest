package com.n4systems.model.orgs;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.api.*;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.DenyReadOnlyUsersAccess;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.GlobalID;

import javax.persistence.*;

@Entity
@Table(name = "org_base")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseOrg extends ArchivableEntityWithTenant implements NamedEntity, Listable<Long>, Comparable<BaseOrg>, NetworkEntity<BaseOrg>, Exportable, Archivable {

	private static final long serialVersionUID = 1L;
	public static final String SECONDARY_ID_FILTER_PATH = "secondaryOrg.id";
	public static final String CUSTOMER_ID_FILTER_PATH = "customerOrg.id";
	public static final String DIVISION_ID_FILTER_PATH = "divisionOrg.id";
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "", null, "state");
	}
	
	@Column(name="name", nullable = false, length = 255)
	private String name;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "addressinfo_id")
	private AddressInfo addressInfo = new AddressInfo();
	
	@SuppressWarnings("unused")
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="secondary_id")
	private SecondaryOrg secondaryOrg;
	
	@SuppressWarnings("unused")
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private CustomerOrg customerOrg;
	
	@SuppressWarnings("unused")
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="division_id")
	private DivisionOrg divisionOrg;
	
	@Column(name="global_id", nullable=false, unique=true)
	private String globalId;
	
	@Column(name="notes", length = 1000)
	private String notes;
	
	public BaseOrg() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		setSecurityFields();
		generateGlobalId();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		setSecurityFields();
		generateGlobalId();
	}

	private void setSecurityFields() {
		secondaryOrg = getSecondaryOrg();
		customerOrg = getCustomerOrg();
		divisionOrg = getDivisionOrg();
	}

	protected void generateGlobalId() {
		if (globalId == null) {
			globalId = GlobalID.getId();
		}
	}
	
	@Override
	@AllowSafetyNetworkAccess
    @DenyReadOnlyUsersAccess
	public String getDisplayName() {
		return name;
	}
	
	@AllowSafetyNetworkAccess
    @DenyReadOnlyUsersAccess
	public String getHierarchicalDisplayName() {
		StringBuffer buff = new StringBuffer();
        if (getDivisionOrg() != null) {
            buff.append(getDivisionOrg().getName()).append(", ").append(getCustomerOrg().getName()).append(" (").append(getPrimaryOrg().getName()).append(")");
        } else if (getCustomerOrg() != null) {
            buff.append(getCustomerOrg().getName()).append(" (").append(getPrimaryOrg().getName()).append(")");
        } else {
            buff.append(getPrimaryOrg().getName());
        }
		return buff.toString();
	}
	
	
	@Override
	@AllowSafetyNetworkAccess
    @DenyReadOnlyUsersAccess
	public String getName() {
		return name;
	}

	@Override
	public void setName(String displayName) {
		this.name = (displayName != null) ? displayName.trim() : null;
	}

	@AllowSafetyNetworkAccess
    @DenyReadOnlyUsersAccess
	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	@Override
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	
	@Override
	public String getGlobalId() {
		return globalId;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%d) [%s]", name, id, String.valueOf(getTenant()));
	}
	
	@Override
	public int compareTo(BaseOrg other) {
		int cmp = name.compareToIgnoreCase(other.getName());
		return (cmp != 0) ? cmp : getId().compareTo(other.getId());
	}
	
	@AllowSafetyNetworkAccess
	public boolean isInternal() {
		return (this instanceof InternalOrg);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isExternal() {
		return (this instanceof ExternalOrg);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isPrimary() {
		return (this instanceof PrimaryOrg);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isSecondary() {
		return (this instanceof SecondaryOrg);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isCustomer() {
		return (this instanceof CustomerOrg);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isDivision() {
		return (this instanceof DivisionOrg);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isLinked() {
		return (isExternal() && ((ExternalOrg)this).getLinkedOrg() != null);
	}
	
	public boolean sameTypeAs(BaseOrg org) {
		return org.getClass().isInstance(this);
	}
	
	/** @return The PrimaryOrg for this Tenant */
	abstract public PrimaryOrg getPrimaryOrg();
	
	/** 
	 * @return The closest InternalOrg.  PrimaryOrg and SecondaryOrg will return themselves.  
	 * CustomerOrg and SecondaryOrg will return the parent of the CustomerOrg
	 */
	abstract public InternalOrg getInternalOrg();
	
	/** 
	 * @return The closest SecondaryOrg.  PrimaryOrg will return null, SecondaryOrg will return itself.  
	 * CustomerOrg and DivisionOrg will return their parent InternalOrg if it is a SecondaryOrg
	 */	
	abstract public SecondaryOrg getSecondaryOrg();
	
	/** 
	 * @return The closest CustomerOrg.  PrimaryOrg and SecondaryOrg will return null.  
	 * CustomerOrg returns itself and SecondaryOrg will return its parent.
	 */	
	abstract public CustomerOrg getCustomerOrg();

	/** 
	 * @return The closest DivisionOrg.  Will be null unless this is a DivisionOrg
	 */	
	abstract public DivisionOrg getDivisionOrg();
	
	/**
	 * @return The bean path used for security filtering
	 */
	@AllowSafetyNetworkAccess
	abstract public String getFilterPath();
	
	abstract public BaseOrg getParent();
	
	public boolean canAccess(BaseOrg child) {
		if (child == null) {
			return false;
		}
		
		// any org can see itself
		if (child.equals(this)) {
			return true;
		}
		
		if (isPrimary()) {
			return (child.getPrimaryOrg().equals(this));
		} else if (isSecondary()) {
			// this is a special case since secondary orgs can see information from the primary
			if (child.isPrimary() && getPrimaryOrg().equals(child)) {
				return true;
			} else {
				return (child.getSecondaryOrg() != null && child.getSecondaryOrg().equals(this));
			}
		} else if (isCustomer()) {
			return (child.getCustomerOrg() != null && child.getCustomerOrg().equals(this));
		} else if (isDivision()) {
			return (child.equals(this));
		} else {
			return false;
		}
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SecurityLevel.calculateSecurityLevel(fromOrg, this);
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
