package com.n4systems.ejb;

import javax.ejb.Local;


import com.n4systems.model.Configuration;
import com.n4systems.util.ConfigEntry;

@Local
public interface ConfigManager {

	/**
	 * Finds a Configuration for the given ConfigEntry and tenantId.  Looks specifically for 
	 * Configuration with null tenantId's when tenantId is null.<br/>
	 * Note that this should only be used in places where Configuration can be modified.  For
	 * read-only access to configurations, use {@link ConfigContext}.
	 * @see ConfigContext
	 * @param entry		The ConfigEntry to search for
	 * @param tenantId	The id of a TenantOrganization or null to search for global configurations
	 * @return			The Configuration or null
	 */
	public Configuration findEntry(ConfigEntry entry, Long tenantId);
	
	/**
	 * Finds a Configuration for the given ConfigEntry with a null tenantId.
	 * @see #findEntry(ConfigEntry, Long)
	 * @param entry		The ConfigEntry to search for
	 * @return			The Configuration or null
	 */
	public Configuration findEntry(ConfigEntry entry);
	
	/**
	 * Removes a Configuration.  Note that the id of the Configuration is ignored in this 
	 * operation.  Only it's key and tenantId are used to find the current entry before removal.
	 * @param entry		The Configuration to remove
	 */
	public void removeEntry(Configuration entry);
	
	/**
	 * Removes a Tenant Configuration if tenantId is not null.  Removes the global configuration 
	 * otherwise
	 * @see #removeEntry(ConfigEntry)
	 * @param entry		The Configuration key to remove
	 * @param tenantId	The id of a TenantOrganization or null to remove the global configuration
	 */
	public void removeEntry(ConfigEntry entry, Long tenantId);
	
	/**
	 * Removes a global Configuration.
	 * @see #removeEntry(ConfigEntry, Long)
	 * @param entry		The Configuration key to remove
	 */
	public void removeEntry(ConfigEntry entry);
	
	/**
	 * Saves or updates a Configuration.  Note that the id of the Configuration is ignored in this 
	 * operation.  Only it's key and tenantId are used to find the current entry before update/save.
	 * @param entry		The Configuration to save
	 */
	public void saveEntry(Configuration entry);
	
	/**
	 * Saves or updates a Configuration.  Saves a Tenant specific configuration when tenantId is not null.
	 * Saves a global configuration otherwise.  Note that a {@link toString()} will be called on value
	 * prior to saving, since all configurations are actually stored as Strings in the database.
	 * @see #saveEntry(Configuration)
	 * @param entry		The Configuration key to save
	 * @param tenantId	The id of a TenantOrganization or null to save a global configuration
	 * @param value		The new value
	 */
	public <T> void saveEntry(ConfigEntry entry, Long tenantId, T value);
	
	/**
	 * Saves or updates a global Configuration.
	 * @see #saveEntry(ConfigEntry, Object)
	 * @param entry		The Configuration key to save
	 * @param value		The new value
	 */
	public <T> void saveEntry(ConfigEntry entry, T value);
}
