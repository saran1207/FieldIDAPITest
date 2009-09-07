package com.n4systems.model.orgs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "org_division")
@PrimaryKeyJoinColumn(name="org_id")
public class DivisionOrg extends ExternalOrg {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private CustomerOrg parent;
	
	public DivisionOrg() {}
	
	@Override
	public PrimaryOrg getPrimaryOrg() {
		return parent.getPrimaryOrg();
	}
	
	@Override
	public InternalOrg getInternalOrg() {
		return parent.getInternalOrg();
	}

	@Override
	public CustomerOrg getCustomerOrg() {
		return parent;
	}

	@Override
	public DivisionOrg getDivisionOrg() {
		return this;
	}
	
	@Override
	protected Long getSecondaryOrgId() {
		return parent.getSecondaryOrgId();
	}
	
	@Override
	protected Long getCustomerOrgId() {
		return parent.getId();
	}

	@Override
	protected Long getDivisionOrgId() {
		return getId();
	}
	
	@Override
	public String getFilterPath() {
		return DIVISION_ID_FILTER_PATH;
	}
	
	@Override
	public CustomerOrg getParent() {
		return parent;
	}

	public void setParent(CustomerOrg parentOrg) {
		this.parent = parentOrg;
	}
	
	@Deprecated
	public String getDivisionId() {
		return getCode();
	}
}
