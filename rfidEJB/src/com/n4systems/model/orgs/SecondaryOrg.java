package com.n4systems.model.orgs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
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
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public InternalOrg getInternalOrg() {
		return this;
	}

	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public SecondaryOrg getSecondaryOrg() {
		return this;
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public CustomerOrg getCustomerOrg() {
		return null;
	}

	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public DivisionOrg getDivisionOrg() {
		return null;
	}
	
	@Override
	public String getFilterPath() {
		return SECONDARY_ID_FILTER_PATH;
	}

	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

	@Override
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public PrimaryOrg getParent() {
		return primaryOrg;
	}
	
	public SecondaryOrg enhance(SecurityLevel level) {
		SecondaryOrg enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setPrimaryOrg((PrimaryOrg)enhance(primaryOrg, level));
		return enhanced;
	}
}
