package com.n4systems.testutils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;

public class TenantCacheTestDouble extends TenantCache {

	
	
	public TenantCacheTestDouble() {
		super(null, null, null, null, null);
	}

	@Override
	public void addPrimaryOrg(PrimaryOrg primaryOrg) {
		// TODO Auto-generated method stub
		super.addPrimaryOrg(primaryOrg);
	}

	@Override
	public void addTenant(Tenant tenant) {
		// TODO Auto-generated method stub
		super.addTenant(tenant);
	}

	@Override
	public PrimaryOrg clearPrimaryOrg(Long tenantId) {
		// TODO Auto-generated method stub
		return super.clearPrimaryOrg(tenantId);
	}

	@Override
	public Tenant clearTenant(Long id) {
		// TODO Auto-generated method stub
		return super.clearTenant(id);
	}

	@Override
	public Collection<PrimaryOrg> findAllPrimaryOrgs() {
		// TODO Auto-generated method stub
		return super.findAllPrimaryOrgs();
	}

	@Override
	public Collection<Tenant> findAllTenants() {
		// TODO Auto-generated method stub
		return super.findAllTenants();
	}

	@Override
	public PrimaryOrg findPrimaryOrg(Long tenantId) {
		return super.findPrimaryOrg(tenantId);
	}

	@Override
	public Tenant findTenant(Long id) {
		// TODO Auto-generated method stub
		return super.findTenant(id);
	}

	@Override
	public Tenant findTenant(String name) {
		// TODO Auto-generated method stub
		return super.findTenant(name);
	}

	@Override
	public void reloadAll() {
		// TODO Auto-generated method stub
		super.reloadAll();
	}

	@Override
	public void reloadAllPrimaryOrgs() {
		// TODO Auto-generated method stub
		super.reloadAllPrimaryOrgs();
	}

	@Override
	public void reloadAllTenants() {
		// TODO Auto-generated method stub
		super.reloadAllTenants();
	}

	@Override
	public void reloadPrimaryOrg(Long tenantId) {
		// TODO Auto-generated method stub
		super.reloadPrimaryOrg(tenantId);
	}

	@Override
	public void reloadTenant(Long id) {
		// TODO Auto-generated method stub
		super.reloadTenant(id);
	}
	
	

}
