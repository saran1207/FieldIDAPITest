package com.n4systems.model.orgs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "org_secondary")
@PrimaryKeyJoinColumn(name="org_id")
public class SecondaryOrg extends InternalOrg {
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="primaryorg_id")
	private PrimaryOrg primaryOrg;
	
	public SecondaryOrg() {}
	
	@Override
	public InternalOrg getInternalOrg() {
		return this;
	}

	@Override
	public CustomerOrg getCustomerOrg() {
		return null;
	}

	@Override
	public DivisionOrg getDivisionOrg() {
		return null;
	}

	@Override
	protected Long getSecondaryOrgId() {
		return getId();
	}
	
	@Override
	protected Long getCustomerOrgId() {
		return null;
	}

	@Override
	protected Long getDivisionOrgId() {
		return null;
	}
	
	@Override
	public String getFilterPath() {
		return SECONDARY_ID_FILTER_PATH;
	}

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

	@Override
	public PrimaryOrg getParent() {
		return primaryOrg;
	}
	
}
