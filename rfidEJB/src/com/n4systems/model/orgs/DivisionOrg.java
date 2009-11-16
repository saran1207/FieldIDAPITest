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
@Table(name = "org_division")
@PrimaryKeyJoinColumn(name="org_id")
public class DivisionOrg extends ExternalOrg {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="parent_id")
	private CustomerOrg parent;
	
	public DivisionOrg() {}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public PrimaryOrg getPrimaryOrg() {
		return parent.getPrimaryOrg();
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public InternalOrg getInternalOrg() {
		return parent.getInternalOrg();
	}

	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public SecondaryOrg getSecondaryOrg() {
		return parent.getSecondaryOrg();
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public CustomerOrg getCustomerOrg() {
		return parent;
	}

	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public DivisionOrg getDivisionOrg() {
		return this;
	}
	
	@Override
	public String getFilterPath() {
		return DIVISION_ID_FILTER_PATH;
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public CustomerOrg getParent() {
		return parent;
	}

	public void setParent(CustomerOrg parentOrg) {
		this.parent = parentOrg;
	}
	
	@Deprecated
	@NetworkAccessLevel(SecurityLevel.DIRECT)
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
