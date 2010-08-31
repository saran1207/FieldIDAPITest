package com.n4systems.services;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

/**
 * Acts as an in-mem cache of {@link SetupDataLastModDates} for each tenant.  This service should be loaded
 * on startup and notified of any changes to {@link SetupDataLastModDates}.  The {@link #touchModDate(Long, SetupDataGroup)} 
 * should be used to update modification dates when changes are made to product types, inspection type, etc.. 
 */
public class SetupDataLastModUpdateService {
	private static Logger logger = Logger.getLogger(SetupDataLastModUpdateService.class);
	private static final int UPDATER_THREADPOOL_MIN = 5;
	private static final int UPDATER_THREADPOOL_MAX = Integer.MAX_VALUE;
	private static final long UPDATER_THREADPOOL_KEEPALIVE = 60L;
	
	private static final SetupDataLastModUpdateService self = new SetupDataLastModUpdateService();
	
	public static SetupDataLastModUpdateService getInstance() {
		return self;
	}
	
	private final ExecutorService updateExecutor;
	private final Map<Long, SetupDataLastModDates> tenantModDates;
	
	private SetupDataLastModUpdateService() {
		tenantModDates = new ConcurrentHashMap<Long, SetupDataLastModDates>();
		updateExecutor = new ThreadPoolExecutor(UPDATER_THREADPOOL_MIN, UPDATER_THREADPOOL_MAX, UPDATER_THREADPOOL_KEEPALIVE, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}
	
	public SetupDataLastModDates getModDates(Long tenantId) {
		return tenantModDates.get(tenantId);
	}
	
	public void updateModDates(SetupDataLastModDates modDates) {
		logger.debug("Updaing SetupDataLastModDates cache for [" + modDates.getTenant().toString() + "]");
		tenantModDates.put(modDates.getTenant().getId(), modDates);
	}
	
	/**
	 * Updates the SetupDataLastModDates for each tenant stored in the cache for the given group
	 * @see #touchModDate(Long, SetupDataGroup, Date)
	 */
	public void touchAllModDates(SetupDataGroup group) {
		Date now = new Date();
		
		for (Long tenantId: tenantModDates.keySet()) {
			touchModDate(tenantId, group, now);
		}
	}
	
	/**
	 * Convenience method for {@link #touchModDate(Long, SetupDataGroup, Date)} assuming a touchDate of now (<code>new Date()</code>)
	 * @see #touchModDate(Long, SetupDataGroup, Date)
	 */
	public void touchModDate(Long tenantId, SetupDataGroup group) {
		touchModDate(tenantId, group, new Date());
	}
	
	/**
	 * This method updates a single mod date for a tenant and type.  The updated
	 * SetupDataLastModDates is saved when finished.
	 * @param tenantId	ID of the Tenant to update the mod date for
	 * @param group		The setup data group to update
	 * @param touchDate	The date to update the mod date to
	 */
	public void touchModDate(Long tenantId, SetupDataGroup group, Date touchDate) {
		SetupDataLastModDates modDates = tenantModDates.get(tenantId);
		
		if (modDates == null) {
			logger.error("touchModDate requested for [" + tenantId + "], group [" + group.name() + "] but no mod dates have been cached for this Tenant.");
			return;
		}
		
		/*
		 *  we need to check if our new touch date is greater then the 
		 *  configured min update interval.  This is to stop cases
		 *  where 2 or more updates come in, in a row touching the date
		 *  by only a few seconds.
		 */
		Long minIntervalMs = ConfigContext.getCurrentContext().getLong(ConfigEntry.SETUPDATA_MIN_UPDATE_INTERVAL_MS, tenantId);
		Date lastDate = modDates.getModDate(group);
		if ((touchDate.getTime() - lastDate.getTime()) < minIntervalMs) {
			// we did not reach the min update interval.
			return;
		}
		
		updateExecutor.execute(new LastModUpdaterThread(modDates, group, touchDate));
	}
	
	/**
	 * Loads all SetupDataLastModDates from the db.
	 */
	public synchronized void loadModDates() {
		tenantModDates.clear();
		
		logger.debug("Reloading SetupDataLastModDates cache");
		AllEntityListLoader<SetupDataLastModDates> loader = new AllEntityListLoader<SetupDataLastModDates>(SetupDataLastModDates.class);
		
		for (SetupDataLastModDates modDates: loader.load()) {
			updateModDates(modDates);
		}
	}
	
	/**
	 * clear all SetupDataLastModDates from cache.
	 */
	public synchronized void clearModDates() {
		logger.debug("Clearing SetupDataLastModDates cache");
		tenantModDates.clear();
	}
	
	private class LastModUpdaterThread implements Runnable {
		private final SetupDataLastModDates modDates;
		private final Date touchDate;
		private final SetupDataGroup group;
		
		public LastModUpdaterThread(SetupDataLastModDates modDates, SetupDataGroup group, Date touchDate) {
			this.modDates = modDates;
			this.group = group;
			this.touchDate = touchDate;
		}
		
		public void run() {
			SetupDataLastModDatesSaver saver = new SetupDataLastModDatesSaver();
			
			// since the save originates from here, there's no need to notify back here.
			saver.setNotifiyUpdateService(false);
			
			// we're going to sync on this object, in case two 
			// requests came in at the same time for the same mod date
			synchronized(modDates) {
				logger.debug("Updating setup data mod date for tenant [" + modDates.getTenant().getName() + "], group [" + group.name() + "]");
				modDates.setModDate(group, touchDate);
				saver.update(modDates);
			}
		}
		
	}
}
