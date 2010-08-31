package com.n4systems.persistence.listeners;

import org.apache.log4j.Logger;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;

public class TenantCacheUpdateListener implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TenantCacheUpdateListener.class);
	
	public void onPostUpdate(PostUpdateEvent event) {
		updateTenantCache(event.getEntity());
	}

	public void onPostInsert(PostInsertEvent event) {
		insertTenantCache(event.getEntity());
	}

	public void onPostDelete(PostDeleteEvent event) {
		deleteTenantCache(event.getEntity());
	}
	
	private void updateTenantCache(Object entity) {
		try {
			if (entity instanceof Tenant) {
				Tenant tenant = (Tenant)entity;
				TenantCache.getInstance().reloadTenant(tenant.getId());
				logger.debug(String.format("Updated [%s] in TenantCache", tenant.toString()));
			} else if (entity instanceof PrimaryOrg) {
				PrimaryOrg primaryOrg = (PrimaryOrg)entity;
				TenantCache.getInstance().reloadPrimaryOrg(primaryOrg.getTenant().getId());
				logger.debug(String.format("Updated [%s] in TenantCache", primaryOrg.toString()));
			}
		} catch(RuntimeException e) {
			logger.error("Failed updating Tenant cache", e);
		}
	}
	
	private void insertTenantCache(Object entity) {
		try {
			if (entity instanceof Tenant) {
				Tenant tenant = (Tenant)entity;
				TenantCache.getInstance().addTenant(tenant);
				logger.debug(String.format("Added [%s] to TenantCache", tenant.toString()));
			} else if (entity instanceof PrimaryOrg) {
				PrimaryOrg primaryOrg = (PrimaryOrg)entity;
				TenantCache.getInstance().addPrimaryOrg(primaryOrg);
				logger.debug(String.format("Added [%s] to TenantCache", primaryOrg.toString()));
			}
		} catch(RuntimeException e) {
			logger.error("Failed adding to TenantCache", e);
		}
	}
	
	private void deleteTenantCache(Object entity) {
		try {
			if (entity instanceof Tenant) {
				Tenant tenant = (Tenant)entity;
				TenantCache.getInstance().clearTenant(tenant.getId());
				logger.debug(String.format("Removed [%s] from TenantCache", tenant.toString()));
			} else if (entity instanceof PrimaryOrg) {
				PrimaryOrg primaryOrg = (PrimaryOrg)entity;
				TenantCache.getInstance().clearPrimaryOrg(primaryOrg.getTenant().getId());
				logger.debug(String.format("Removed [%s] from TenantCache", primaryOrg.toString()));
			}
		} catch(RuntimeException e) {
			logger.error("Failed removal from TenantCache", e);
		}
	}
}
