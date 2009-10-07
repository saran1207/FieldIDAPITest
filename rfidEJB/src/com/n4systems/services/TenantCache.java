package com.n4systems.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

/**
 * Simple mem-cache for Tenants and PrimaryOrgs
 */
public class TenantCache {
	private static TenantCache self = new TenantCache();
	
	public static TenantCache getInstance() {
		return self;
	}
	
	public static synchronized void setInstance(TenantCache cache) {
		self = cache;
	}

	private final NonSecureIdLoader<Tenant> tenantLoader;
	private final PrimaryOrgByTenantLoader orgLoader;
	private final TenantByNameLoader tenantNameLoader;
	private final AllEntityListLoader<Tenant> allTenantLoader;
	private final AllEntityListLoader<PrimaryOrg> allPrimaryOrgLoader;
	
	private Map<Long, Tenant> tenantIdMap = new ConcurrentHashMap<Long, Tenant>();
	private Map<String, Tenant> tenantNameMap = new ConcurrentHashMap<String, Tenant>();
	private Map<Long, PrimaryOrg> primaryOrgMap = new ConcurrentHashMap<Long, PrimaryOrg>();
	
	private TenantCache() {
		this(new NonSecureIdLoader<Tenant>(Tenant.class), new TenantByNameLoader(), new AllEntityListLoader<Tenant>(Tenant.class), new PrimaryOrgByTenantLoader(), new AllEntityListLoader<PrimaryOrg>(PrimaryOrg.class));
	}
	
	public TenantCache(NonSecureIdLoader<Tenant> tenantLoader, TenantByNameLoader tenantNameLoader,  AllEntityListLoader<Tenant> allTenantLoader, PrimaryOrgByTenantLoader orgLoader, AllEntityListLoader<PrimaryOrg> allPrimaryOrgLoader) {
		this.tenantLoader  = tenantLoader;
		this.tenantNameLoader = tenantNameLoader;
		this.orgLoader = orgLoader;
		this.allTenantLoader = allTenantLoader;
		this.allPrimaryOrgLoader = allPrimaryOrgLoader;
	}
	
	/**
	 * Returns a Tenant for the given id.  If the Tenant is not in cache, it will be loaded.
	 * @param id	Tenant Id
	 * @return		Tenant
	 */
	public Tenant findTenant(Long id) {
		if (!tenantIdMap.containsKey(id)) {
			loadTenant(id);
		}
		return tenantIdMap.get(id);
	}
	
	/**
	 * Returns a Tenant for the given name.  If the Tenant is not in cache, it will be loaded.
	 * @param name	Tenant name
	 * @return		Tenant
	 */
	public Tenant findTenant(String name) {
		name = name != null ? name.toLowerCase() : null;
		if (!tenantNameMap.containsKey(name)) {
			loadTenant(name);
		}
		return tenantNameMap.get(name);
	}
	
	/**
	 * Returns the PrimaryOrg for a given Tenant.  If the PrimaryOrg is not in cache, it will be loaded
	 * @param name	Tenant id
	 * @return		PrimaryOrg
	 */
	public PrimaryOrg findPrimaryOrg(Long tenantId) {
		if (!primaryOrgMap.containsKey(tenantId)) {
			loadPrimaryOrg(tenantId);
		}
		return primaryOrgMap.get(tenantId);
	}
	
	/**
	 * @return A Collection containing all cached Tenants
	 */
	public Collection<Tenant> findAllTenants() {
		return new TreeSet<Tenant>(tenantIdMap.values());
	}
	
	/**
	 * @return A Collection containing all cached PrimaryOrgs 
	 */
	public Collection<PrimaryOrg> findAllPrimaryOrgs() {
		return new TreeSet<PrimaryOrg>(primaryOrgMap.values());
	}
	
	/**
	 * Clears and returns the Tenant for the given id from cache. 
	 * @param id	Tenant id
	 * @return		Cleared Tenant
	 */
	public Tenant clearTenant(Long id) {
		Tenant tenant = findTenant(id);
		removeTenant(tenant);
		return tenant;
	}
	
	/**
	 * Clears and returns the PrimaryOrg for the given Tenant from cache. 
	 * @param tenant	Tenant
	 * @return			Cleared PrimaryOrg
	 */
	public PrimaryOrg clearPrimaryOrg(Long tenantId) {
		PrimaryOrg primaryOrg = findPrimaryOrg(tenantId);
		removePrimaryOrg(primaryOrg);
		return primaryOrg;
	}
	
	/**
	 * Reloads a Tenant from the database
	 * @param id	Tenant id
	 */
	public void reloadTenant(Long id) {
		clearTenant(id);
		findTenant(id);
	}
	
	/**
	 * Reloads a PrimaryOrg from the database
	 * @param tenant	Tenant
	 */
	public void reloadPrimaryOrg(Long tenantId) {
		clearPrimaryOrg(tenantId);
		findPrimaryOrg(tenantId);
	}
	
	/**
	 * Reloads all Tenants and PrimaryOrgs from the database
	 */
	public void reloadAll() {
		reloadAllTenants();
		reloadAllPrimaryOrgs();
	}
	
	/**
	 * Reloads all Tenants from the database
	 */
	public void reloadAllTenants() {
		List<Tenant> tenants = allTenantLoader.load();

		for (Tenant tenant: tenants) {
			addTenant(tenant);
		}
	}
	
	/**
	 * Reloads all PrimaryOrgs from the database
	 */
	public void reloadAllPrimaryOrgs() {
		List<PrimaryOrg> orgs = allPrimaryOrgLoader.load();
		
		for (PrimaryOrg org: orgs) {
			addPrimaryOrg(org);
		}
	}
	
	/**
	 * Adds or Replaces a Tenant in the cache
	 * @param tenant	Tenant
	 */
	public void addTenant(Tenant tenant) {
		if (tenant != null) {
			tenantIdMap.put(tenant.getId(), tenant);
			tenantNameMap.put(tenant.getName(), tenant);
		}
	}
	
	/**
	 * Adds or Replaces a PrimaryOrg in the cache
	 * @param primaryOrg	PrimaryOrg
	 */
	public void addPrimaryOrg(PrimaryOrg primaryOrg) {
		if (primaryOrg != null) {
			primaryOrgMap.put(primaryOrg.getTenant().getId(), primaryOrg);
		}
	}
	
	private void removeTenant(Tenant tenant) {
		if (tenant != null) {
			tenantIdMap.remove(tenant.getId());
			tenantNameMap.remove(tenant.getName());
		}
	}
	
	private void removePrimaryOrg(PrimaryOrg primaryOrg) {
		if (primaryOrg != null) {
			primaryOrgMap.remove(primaryOrg.getTenant());
		}
	}
	
	private void loadTenant(Long id) {
		tenantLoader.setId(id);
		addTenant(tenantLoader.load());
	}
	
	private void loadTenant(String name) {
		tenantNameLoader.setTenantName(name);
		addTenant(tenantNameLoader.load());
	}
	
	private void loadPrimaryOrg(Long tenantId) {
		orgLoader.setTenantId(tenantId);
		addPrimaryOrg(orgLoader.load());
	}
	
}
