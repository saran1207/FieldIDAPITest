package com.n4systems.model.orgs;

import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.DenyReadOnlyUsersAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;

@Entity
@Table(name = "org_customer")
@PrimaryKeyJoinColumn(name="org_id")
public class CustomerOrg extends ExternalOrg {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Note, this references BaseOrg rather then InternalOrg since 
	 * InternalOrg is not an Entity (it's a MappedSuperclass).  Hibernate 
	 * has no idea what to do in that situation, so we set it directly to the BaseOrg
	 * and force casing to InternalOrg via getter/setter
	 */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="parent_id")
	private BaseOrg parent;
	
	public CustomerOrg() {}
	
	@Override
    @AllowSafetyNetworkAccess
	public PrimaryOrg getPrimaryOrg() {
		return parent.getPrimaryOrg();
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public InternalOrg getInternalOrg() {
		return getParent();
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public SecondaryOrg getSecondaryOrg() {
		return parent.getSecondaryOrg();
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public CustomerOrg getCustomerOrg() {
		return this;
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public DivisionOrg getDivisionOrg() {
		return null;
	}
	
	@Override
	public String getFilterPath() {
		return CUSTOMER_ID_FILTER_PATH;
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public InternalOrg getParent() {
		// Note the type is downcast to InternalOrg (should always be the case because of forced setter)
		return (InternalOrg)parent;
	}

	// Note we force the type to be InternalOrg
	public void setParent(InternalOrg parentOrg) {
		this.parent = parentOrg;
	}
	
	@Deprecated
	@AllowSafetyNetworkAccess
	public String getCustomerId() {
		return getCode();
	}
	
	public CustomerOrg enhance(SecurityLevel level) {
		CustomerOrg enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setParent((InternalOrg)enhance(parent, level));
		enhanced.setContact(enhance(getContact(),level));
		return enhanced;
	}
	
	@Override
	public String getDisplayName() {
		return getCustomerOrg().getName() + " (" + getInternalOrg().getName() + ")";		
	}

}
