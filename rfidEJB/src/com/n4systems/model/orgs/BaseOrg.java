package com.n4systems.model.orgs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "org_base")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseOrg extends EntityWithTenant implements NamedEntity, Listable<Long>, Comparable<BaseOrg>, NetworkEntity<BaseOrg> {
	private static final long serialVersionUID = 1L;
	public static final String SECONDARY_ID_FILTER_PATH = "secondaryOrg.id";
	public static final String CUSTOMER_ID_FILTER_PATH = "customerOrg.id";
	public static final String DIVISION_ID_FILTER_PATH = "divisionOrg.id";
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "", null, null);
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
	
	public BaseOrg() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		setSecurityFields();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		setSecurityFields();
	}

	private void setSecurityFields() {
		secondaryOrg = getSecondaryOrg();
		customerOrg = getCustomerOrg();
		divisionOrg = getDivisionOrg();
	}

	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public String getDisplayName() {
		return name;
	}
	
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public String getName() {
		return name;
	}

	public void setName(String displayName) {
		this.name = (displayName != null) ? displayName.trim() : null;
	}

	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	@Override
	public String toString() {
		return String.format("%s (%d) [%s]", name, id, String.valueOf(getTenant()));
	}
	
	public int compareTo(BaseOrg other) {
		int cmp = name.compareToIgnoreCase(other.getName());
		return (cmp != 0) ? cmp : getId().compareTo(other.getId());
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isInternal() {
		return (this instanceof InternalOrg);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isExternal() {
		return (this instanceof ExternalOrg);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isPrimary() {
		return (this instanceof PrimaryOrg);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isSecondary() {
		return (this instanceof SecondaryOrg);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isCustomer() {
		return (this instanceof CustomerOrg);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isDivision() {
		return (this instanceof DivisionOrg);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
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
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	abstract public String getFilterPath();
	
	abstract public BaseOrg getParent();
	
	public boolean allowsAccessFor(BaseOrg child) {
		if (child == null) {
			return false;
		}
		
		// this is a special case check since secondary orgs can see information from the primary
		if (child.isSecondary() && isPrimary() && child.getPrimaryOrg().equals(this)) {
			return true;
		}
		
		if (isPrimary()) {
			return (child.getPrimaryOrg().equals(this));
		} else if (isSecondary()) {
			return (child.getSecondaryOrg() != null && child.getSecondaryOrg().equals(this));
		} else if (isCustomer()) {
			return (child.getCustomerOrg() != null && child.getCustomerOrg().equals(this));
		} else if (isDivision()) {
			return (child.equals(this));
		} else {
			return false;
		}
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SafetyNetworkSecurityCache.getSecurityLevel(fromOrg, this);
	}
}
