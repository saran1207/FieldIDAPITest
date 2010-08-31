package com.n4systems.testutils;

import java.util.Collection;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;

public class TenantCacheTestDouble extends TenantCache {

	
	
	public TenantCacheTestDouble() {
		super(null, null, null, null, null);
	}

	@Override
	public void addPrimaryOrg(PrimaryOrg primaryOrg) {
		super.addPrimaryOrg(primaryOrg);
	}

	@Override
	public void addTenant(Tenant tenant) {
		super.addTenant(tenant);
	}

	@Override
	public PrimaryOrg clearPrimaryOrg(Long tenantId) {
		return super.clearPrimaryOrg(tenantId);
	}

	@Override
	public Tenant clearTenant(Long id) {
		return super.clearTenant(id);
	}

	@Override
	public Collection<PrimaryOrg> findAllPrimaryOrgs() {
		return super.findAllPrimaryOrgs();
	}

	@Override
	public Collection<Tenant> findAllTenants() {
		return super.findAllTenants();
	}

	@Override
	public PrimaryOrg findPrimaryOrg(Long tenantId) {
		return super.findPrimaryOrg(tenantId);
	}

	@Override
	public Tenant findTenant(Long id) {
		return super.findTenant(id);
	}

	@Override
	public Tenant findTenant(String name) {
		return super.findTenant(name);
	}

	@Override
	public void reloadAll() {
		super.reloadAll();
	}

	@Override
	public void reloadAllPrimaryOrgs() {
		super.reloadAllPrimaryOrgs();
	}

	@Override
	public void reloadAllTenants() {
		super.reloadAllTenants();
	}

	@Override
	public void reloadPrimaryOrg(Long tenantId) {
		super.reloadPrimaryOrg(tenantId);
	}

	@Override
	public void reloadTenant(Long id) {
		super.reloadTenant(id);
	}
	
	

}
