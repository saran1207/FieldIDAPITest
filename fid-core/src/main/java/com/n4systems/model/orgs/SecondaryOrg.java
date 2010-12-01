package com.n4systems.model.orgs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.DenyReadOnlyUsersAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "org_secondary")
@PrimaryKeyJoinColumn(name="org_id")
public class SecondaryOrg extends InternalOrg {
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="primaryorg_id")
	private PrimaryOrg primaryOrg;
	
	public SecondaryOrg() {}
	
	@Override
    @DenyReadOnlyUsersAccess
	public InternalOrg getInternalOrg() {
		return this;
	}

	@Override
    @DenyReadOnlyUsersAccess
	public SecondaryOrg getSecondaryOrg() {
		return this;
	}
	
	@Override
    @DenyReadOnlyUsersAccess
	public CustomerOrg getCustomerOrg() {
		return null;
	}

	@Override
    @DenyReadOnlyUsersAccess
	public DivisionOrg getDivisionOrg() {
		return null;
	}
	
	@Override
	public String getFilterPath() {
		return SECONDARY_ID_FILTER_PATH;
	}

    @DenyReadOnlyUsersAccess
	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

	@Override
	@AllowSafetyNetworkAccess
	public PrimaryOrg getParent() {
		return primaryOrg;
	}
	
	public SecondaryOrg enhance(SecurityLevel level) {
		SecondaryOrg enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setPrimaryOrg((PrimaryOrg)enhance(primaryOrg, level));
		return enhanced;
	}
}
