package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.n4systems.model.Tenant;

public class TenantBuilder extends BaseBuilder<Tenant> {

	private final String name;
	private List<Tenant> linkedTenants = new ArrayList<Tenant>();
	
	
	
	public static Tenant n4() {
		return new TenantBuilder("n4", new ArrayList<Tenant>()).withId(1L).build();
	}
	
	public static TenantBuilder aTenant() {
		return new TenantBuilder("first_tenant", new ArrayList<Tenant>());
	}
	

	public TenantBuilder(String name, List<Tenant> linkedTenants) {
		this(name, linkedTenants, null);
	}
	
	public TenantBuilder(String name, List<Tenant> linkedTenants, Long id) {
		super(id);
		this.name = name;
		this.linkedTenants = linkedTenants;
	}
	
	public TenantBuilder named(String name) {
		return new TenantBuilder(name, this.linkedTenants, id);
	}
	
	public TenantBuilder linkedTo(Tenant...linkedTenants) {
		return new TenantBuilder(name, Arrays.asList(linkedTenants), id);
	}
	
	public TenantBuilder withId(Long id) {
		return new TenantBuilder(name, linkedTenants, id);
	}
	
	
	@Override
	public Tenant build() {
		return new Tenant(id, name);
	}

}
