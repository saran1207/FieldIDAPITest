package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;

public class TenantBuilder extends BaseBuilder<Tenant> {

	private final String name;
	private List<Tenant> linkedTenants = new ArrayList<Tenant>();
	private Set<ExtendedFeature> features = new HashSet<ExtendedFeature>();
	
	
	public static Tenant n4() {
		return new TenantBuilder("n4", new ArrayList<Tenant>()).withId(1L).build();
	}
	
	public static TenantBuilder aTenant() {
		return new TenantBuilder("first_tenant", new ArrayList<Tenant>());
	}
	
//	public static ManufacturerOrganization aManufacturer(String name) {
//		ManufacturerOrganization manufacturer = new ManufacturerOrganization();
//		manufacturer.setId(new Random().nextLong());
//		manufacturer.setName(name);
//		return manufacturer;
//	}
	
	public TenantBuilder(String name, List<Tenant> linkedTenants) {
		this(name, linkedTenants, null, new HashSet<ExtendedFeature>());
	}
	
	public TenantBuilder(String name, List<Tenant> linkedTenants, Long id, Set<ExtendedFeature> features) {
		super(id);
		this.name = name;
		this.linkedTenants = linkedTenants;
		this.features = features;
	}
	
	public TenantBuilder named(String name) {
		return new TenantBuilder(name, this.linkedTenants, id, features);
	}
	
	public TenantBuilder linkedTo(Tenant...linkedTenants) {
		return new TenantBuilder(name, Arrays.asList(linkedTenants), id, features);
	}
	
	public TenantBuilder withId(Long id) {
		return new TenantBuilder(name, linkedTenants, id, features);
	}
	
	public TenantBuilder withExtendedFeatures(ExtendedFeature...extendedFeatures) {
		return new TenantBuilder(name, linkedTenants, id, new HashSet<ExtendedFeature>(Arrays.asList(extendedFeatures)));
	}
	
	@Override
	public Tenant build() {
		return new Tenant(id, name);
	}

}
