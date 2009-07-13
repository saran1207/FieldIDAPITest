package com.n4systems.model.parents.legacy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.Organization;
import com.n4systems.model.api.HasOrganization;

//this class this just temporary while we refactor the lagacy beans to use the new AbstractEntity

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBeanOrganizationWithCreateModifyDate extends LegacyBeanTenantWithCreateModifyDate implements HasOrganization {

	public static final String columnName = "r_organization";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = columnName)
	private Organization organization;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
