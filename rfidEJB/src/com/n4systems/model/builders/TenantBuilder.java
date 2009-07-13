package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.InspectorOrganization;
import com.n4systems.model.ManufacturerOrganization;
import com.n4systems.model.TenantOrganization;

public class TenantBuilder extends BaseBuilder<TenantOrganization> {

	private final String name;
	private List<TenantOrganization> linkedTenants = new ArrayList<TenantOrganization>();
	private Set<ExtendedFeature> features = new HashSet<ExtendedFeature>();
	
	
	public static TenantOrganization n4() {
		return new TenantBuilder("n4", new ArrayList<TenantOrganization>()).withId(1L).build();
	}
	
	public static TenantBuilder aTenant() {
		return new TenantBuilder("first_tenant", new ArrayList<TenantOrganization>());
	}
	
	public static ManufacturerOrganization aManufacturer(String name) {
		ManufacturerOrganization manufacturer = new ManufacturerOrganization();
		manufacturer.setId(new Random().nextLong());
		manufacturer.setName(name);
		return manufacturer;
	}
	
	public TenantBuilder(String name, List<TenantOrganization> linkedTenants) {
		this(name, linkedTenants, null, new HashSet<ExtendedFeature>());
	}
	
	public TenantBuilder(String name, List<TenantOrganization> linkedTenants, Long id, Set<ExtendedFeature> features) {
		super(id);
		this.name = name;
		this.linkedTenants = linkedTenants;
		this.features = features;
	}
	
	public TenantBuilder named(String name) {
		return new TenantBuilder(name, this.linkedTenants, id, features);
	}
	
	public TenantBuilder linkedTo(TenantOrganization...linkedTenants) {
		return new TenantBuilder(name, Arrays.asList(linkedTenants), id, features);
	}
	
	public TenantBuilder withId(Long id) {
		return new TenantBuilder(name, linkedTenants, id, features);
	}
	
	public TenantBuilder withExtendedFeatures(ExtendedFeature...extendedFeatures) {
		return new TenantBuilder(name, linkedTenants, id, new HashSet<ExtendedFeature>(Arrays.asList(extendedFeatures)));
	}
	
	
	@Override
	public TenantOrganization build() {
		InspectorOrganization tenant = new InspectorOrganization();
		tenant.setId(id);
		tenant.setName(name);	
		tenant.setLinkedTenants(converTenantsToManufactures());
		tenant.setExtendedFeatures(features);
		return tenant;
	}
	
	private List<ManufacturerOrganization> converTenantsToManufactures() {
		List<ManufacturerOrganization> manufacturers = new ArrayList<ManufacturerOrganization>();
		for (TenantOrganization linkedTenant : linkedTenants) {
			ManufacturerOrganization manufacturer = new ManufacturerOrganization();
			manufacturer.setId(linkedTenant.getId());	
			manufacturer.setName(linkedTenant.getName());
			manufacturers.add(manufacturer);
		}
		
		return manufacturers;
	}

}
