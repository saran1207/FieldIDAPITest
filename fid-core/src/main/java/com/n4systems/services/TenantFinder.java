package com.n4systems.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByEmailLoader;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

/**
 * Simple finder for Tenants and PrimaryOrgs
 */
public class TenantFinder {
	private static TenantFinder self = new TenantFinder();
	
	public static TenantFinder getInstance() {
		return self;
	}
	
	public static synchronized void setInstance(TenantFinder finder) {
		self = finder;
	}

    public Tenant findTenant(Long id) {
        return new NonSecureIdLoader<Tenant>(Tenant.class).setId(id).load();
    }

    public Tenant findTenant(String name) {
        name = name != null ? name.toLowerCase() : null;
        return new TenantByNameLoader().setTenantName(name).load();
    }
    
    public List<Tenant> findTenantsByEmail(String email){
    	List<Tenant> tenants = new ArrayList<Tenant>();
    	email = email != null ? email.toLowerCase() : null;
    	List<User> users= new UserByEmailLoader(new OpenSecurityFilter()).setEmail(email).load();
    	for (User user : users){
    		if (!tenants.contains(user.getTenant())){
    			tenants.add(user.getTenant());
    		}
    	}
    	return tenants;
    }

    public PrimaryOrg findPrimaryOrg(Long tenantId) {
        return new PrimaryOrgByTenantLoader().setTenantId(tenantId).load();
    }

    public Collection<Tenant> findAllTenants() {
        return new AllEntityListLoader<Tenant>(Tenant.class).load();
    }

    public Collection<PrimaryOrg> findAllPrimaryOrgs() {
        return new AllEntityListLoader<PrimaryOrg>(PrimaryOrg.class).load();
    }
	
}
