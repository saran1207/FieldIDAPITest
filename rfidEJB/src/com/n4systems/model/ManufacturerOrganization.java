package com.n4systems.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
@DiscriminatorValue(value = "manufacturer")
public class ManufacturerOrganization extends TenantOrganization {

	private static final long serialVersionUID = 1L;
	
	@ManyToMany (targetEntity = Organization.class, fetch = FetchType.LAZY)
	@JoinTable (name = "tenantlink",
					joinColumns = @JoinColumn(name = "r_manufacturer", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_linkedtenant", referencedColumnName = "id") )
	private List<InspectorOrganization> linkedTenants;

	public List<InspectorOrganization> getLinkedInspectors() {
		return linkedTenants;
	}

	public void setLinkedInspectors(List<InspectorOrganization> linkedTenants) {
		this.linkedTenants = linkedTenants;
	}

	public List<? extends TenantOrganization> getLinkedTenants() {
		return getLinkedInspectors();
	}
	
	
}
