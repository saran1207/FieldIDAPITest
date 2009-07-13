package com.n4systems.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
@DiscriminatorValue(value = "inspector")
public class InspectorOrganization extends TenantOrganization {

	private static final long serialVersionUID = 1L;
	
	@ManyToMany (targetEntity = Organization.class, fetch = FetchType.LAZY)
	@JoinTable (name = "tenantlink",
					joinColumns = @JoinColumn(name = "r_linkedtenant", referencedColumnName = "id"),
					inverseJoinColumns = @JoinColumn(name = "r_manufacturer", referencedColumnName = "id") )
	private List<ManufacturerOrganization> linkedTenats;
	
	public List<ManufacturerOrganization> getLinkedManufacturers() {
		return linkedTenats;
	}
	public void setLinkedTenants(List<ManufacturerOrganization> linkedTenats) {
		this.linkedTenats = linkedTenats;
	}
	@Override
	public List<? extends TenantOrganization> getLinkedTenants() {
		return getLinkedManufacturers();
	}
	
	
	
}
