package com.n4systems.model.orgs;

import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.DenyReadOnlyUsersAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;

@Entity
@Table(name = "org_division")
@PrimaryKeyJoinColumn(name="org_id")
public class DivisionOrg extends ExternalOrg {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="parent_id")
	private CustomerOrg parent;
	
	public DivisionOrg() {}
	
	@Override
	public PrimaryOrg getPrimaryOrg() {
		return parent.getPrimaryOrg();
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public InternalOrg getInternalOrg() {
		return parent.getInternalOrg();
	}

	@Override
    @DenyReadOnlyUsersAccess
	public SecondaryOrg getSecondaryOrg() {
		return parent.getSecondaryOrg();
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public CustomerOrg getCustomerOrg() {
		return parent;
	}

	@Override
    @DenyReadOnlyUsersAccess
	public DivisionOrg getDivisionOrg() {
		return this;
	}
	
	@Override
	public String getFilterPath() {
		return DIVISION_ID_FILTER_PATH;
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public CustomerOrg getParent() {
		return parent;
	}

	public void setParent(CustomerOrg parentOrg) {
		this.parent = parentOrg;
	}
	
	@Deprecated
	@AllowSafetyNetworkAccess
	public String getDivisionId() {
		return getCode();
	}
	
	public DivisionOrg enhance(SecurityLevel level) {
		DivisionOrg enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setParent((CustomerOrg)enhance(parent, level));
		enhanced.setContact(enhance(getContact(), level));
		return enhanced;
	}

	@Override
	public String getDisplayName() {
		return getName() + ", " + getCustomerOrg().getDisplayName();		
	}

}
