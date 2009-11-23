package com.n4systems.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.logging.Logger;

import com.n4systems.model.Configuration;

/**
 * Provides a static configuration context for the system.  All read access to system configurations should be
 * done through this Class.  All access to this is thread-safe.  The current context will read and cache all
 * system configurations on access.  Any persistent changes to the system configuration MUST call
 * {@link #markDirty()} which will force a re-read of the configurations on next access.
 * @see ConfigContext#getCurrentContext()
 */
public class ConfigContext {
	private Logger logger = Logger.getLogger(ConfigContext.class);
	
	private static ConfigContext currentContext;
	
	protected final CopyOnWriteArrayList<Configuration> configruations = new CopyOnWriteArrayList<Configuration>();
	private final AtomicBoolean dirty = new AtomicBoolean();
	
	protected ConfigContext() {
		// mark us dirty now, so a load will happen on access
		markDirty();
	}
	
	/**
	 * Gets the current static configuration context.
	 * @return	The current ConfigContext
	 */
	public static ConfigContext getCurrentContext() {
		if (currentContext == null) {
			currentContext = new ConfigContext();
		}
		return currentContext;
	}
	
	
	public static synchronized void setCurrentContext(ConfigContext newContext) {
		currentContext = newContext;
	}
	
	/**
	 * Marks the current context as dirty, forcing a configuration reload on next access.<br />
	 * This MUST be called on any persistent change to system configurations, tenant specific or global.<br />
	 * This operation is thread safe.
	 */
	public void markDirty() {
		dirty.set(true);
	}
	
	protected void markClean() {
		dirty.set(false);
	}
	
	protected boolean isDirty() {
		return dirty.get();
	}
	
	/**
	 * Reloads the master configuration list.
	 */
	protected void reloadConfigurations() {
		logger.debug("Reloading configruations");
		
		/*
		 * Since we're using a CopyOnWriteArrayList, the following operation is thread-safe without a synchronized block.
		 * Changes to the list (clear, addAll) will be made in a copy of the array and then moved into place.  Thus
		 * any iterators on it will be unaffected (but also unaware) of the changes.  AKA, If a change were made, while 
		 * getEntry was searching for an entry, the old entry would be returned.  Sine config changes are rare and
		 * config reads are common, this system currently makes the most sense.
		 * 
		 */
		configruations.clear();
		try {
			// load all configs from the PersistenceManager.
			configruations.addAll(ServiceLocator.getPersistenceManager().findAll(Configuration.class));
			markClean();
		} catch(Exception e) {
			logger.error("Failed loading configurations", e);
		}
	}
	
	/**
	 * returns the master configuration list, reloading them if the current context is marked dirty.
	 * ALL access to the master configuration list should be done through this method, even privately.
	 * This is to ensure, dirty configurations are reloaded as soon as possible.
	 * @see #reloadConfigurations()
	 * @return The master list of configurations
	 */
	private CopyOnWriteArrayList<Configuration> getConfigruations() {
		if(isDirty()) {
			// if the list is dirty, reload the configs
			reloadConfigurations();
		}	
		return configruations;
	}
	
	/**
	 * Returns a list of all currently loaded configurations.  The list is a copy of the 
	 * context's master list.  Changes to it will not be reflected in the master.<br />
	 * Note that if the current context is marked dirty, a reload of the list will be 
	 * forced.
	 * @return		A copy of the contexts master configuration list
	 */
	public List<Configuration> getAllConfigs() {
		return new ArrayList<Configuration>(getConfigruations());
	}
	
	/**
	 * Finds the global configuration entry.  May return null if no global entry exists.
	 * @see #getEntry(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			A ConfigurationBean or null if no global entry exists
	 */
	public Configuration getEntry(ConfigEntry entry) {
		return getEntry(entry, null);
	}
	
	/**
	 * Finds a tenant specific configuration entry, defaulting back to the global entry
	 * if the tenant specific entry could not be located.  Also may return null if the
	 * global entry does not exist.
	 * @param entry			A ConfigEntry
	 * @param tenantId		Id of a TenantOrganization or null to find the global entry
	 * @return				A ConfigurationBean or null if no global or tenant entry exists
	 */
	public Configuration getEntry(ConfigEntry entry, Long tenantId) {
		Configuration tenantConfig = null;
		Configuration globalConfig = null;
		
		for(Configuration config: getConfigruations()) {
			if(config.getIdentifier().equals(entry)) {
				// global configs have a null tenant
				if(config.getTenantId() == null) {
					globalConfig = config;
					
					// if we were looking for a global config,  we can exit now
					if(tenantId == null) {
						break;
					}
				} else if(tenantId != null && config.getTenantId().equals(tenantId))  {
					tenantConfig = config;
					// we can break immediately as we will always return tenantConfig first, if one was found
					break;
				}
			}
		}
		
		// if we found a tenant config, return that, otherwise return the global.  Note that global may still be null;
		return (tenantConfig != null) ? tenantConfig : globalConfig;
	}
	
	/**
	 * Returns the value of a configuration as a String.  Looks first for a tenant specific entry, then uses the global
	 * if no tenant specific entry could be found.  Lastly, defaults to the hard-coded ConfigEntry default. 
	 * @param entry			A ConfigEntry
	 * @param tenantId		Id of a TenantOrganization or null to search for global entries
	 * @return				The ConfigEntry's value
	 */
	public String getString(ConfigEntry entry, Long tenantId) {
		Configuration config = getEntry(entry, tenantId);
		
		String value;
		// if we found a config, get it's value, otherwise use the ConfigEntries' hardcoded default
		if(config != null) {
			value = config.getValue();
		} else {
			value = entry.getDefaultValue();
		}
		
		return value;
	}
	
	/**
	 * Returns the value of a global configuration as a String.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value
	 */
	public String getString(ConfigEntry entry) {
		return getString(entry, null);
	}
	
	/**
	 * Returns the value of a configuration as an Integer.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @param tenantId	Id of a TenantOrganization or null to search for global entries
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Integer
	 */
	public Integer getInteger(ConfigEntry entry, Long tenantId) {
		try {
			return Integer.valueOf(getString(entry, tenantId));
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Returns the value of a global configuration as an Integer.
	 * @see #getInteger(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Integer
	 */
	public Integer getInteger(ConfigEntry entry) {
		return getInteger(entry, null);
	}
	
	/**
	 * Returns the value of a configuration as a Long.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @param tenantId	Id of a TenantOrganization or null to search for global entries
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Long
	 */
	public Long getLong(ConfigEntry entry, Long tenantId) {
		try {
			return Long.valueOf(getString(entry, tenantId));
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Returns the value of a global configuration as a Long.
	 * @see #getLong(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Long
	 */
	public Long getLong(ConfigEntry entry) {
		return getLong(entry, null);
	}
	
	/**
	 * Returns the value of a configuration as a Boolean.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @param tenantId	Id of a TenantOrganization or null to search for global entries
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Boolean
	 */
	public Boolean getBoolean(ConfigEntry entry, Long tenantId) {
		try {
			return Boolean.valueOf(getString(entry, tenantId));
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Returns the value of a global configuration as a Boolean.
	 * @see #getLong(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Boolean
	 */
	public Boolean getBoolean(ConfigEntry entry) {
		return getBoolean(entry, null);
	}
	
	/**
	 * A convenience method returning the Application Root directory.
	 * @return	A File pointing to the application root directory
	 */
	public File getAppRoot() {
		return new File(getString(ConfigEntry.GLOBAL_APPLICATION_ROOT));
	}
}
