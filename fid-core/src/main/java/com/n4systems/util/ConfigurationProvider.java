package com.n4systems.util;

import java.io.Serializable;

public interface ConfigurationProvider extends Serializable {

	/**
	 * Returns the value of a configuration as a String.  Looks first for a tenant specific entry, then uses the global
	 * if no tenant specific entry could be found.  Lastly, defaults to the hard-coded ConfigEntry default. 
	 * @param entry			A ConfigEntry
	 * @param tenantId		Id of a TenantOrganization or null to search for global entries
	 * @return				The ConfigEntry's value
	 */
	String getString(ConfigEntry entry, Long tenantId);

	/**
	 * Returns the value of a global configuration as a String.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value
	 */
	default String getString(ConfigEntry entry) {
		return getString(entry, null);
	}

	/**
	 * Returns the value of a configuration as an Integer.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @param tenantId	Id of a TenantOrganization or null to search for global entries
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Integer
	 */
	Integer getInteger(ConfigEntry entry, Long tenantId);

	/**
	 * Returns the value of a global configuration as an Integer.
	 * @see #getInteger(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Integer
	 */
	default Integer getInteger(ConfigEntry entry) {
		return getInteger(entry, null);
	}

	/**
	 * Returns the value of a configuration as a Long.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @param tenantId	Id of a TenantOrganization or null to search for global entries
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Long
	 */
	Long getLong(ConfigEntry entry, Long tenantId);

	/**
	 * Returns the value of a global configuration as a Long.
	 * @see #getLong(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Long
	 */
	default Long getLong(ConfigEntry entry) {
		return getLong(entry, null);
	}

	/**
	 * Returns the value of a configuration as a Boolean.
	 * @see #getString(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @param tenantId	Id of a TenantOrganization or null to search for global entries
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Boolean
	 */
	Boolean getBoolean(ConfigEntry entry, Long tenantId);

	/**
	 * Returns the value of a global configuration as a Boolean.
	 * @see #getLong(ConfigEntry, Long)
	 * @param entry		A ConfigEntry
	 * @return			The ConfigEntry's value or null if the String value could not be converted to Boolean
	 */
	default Boolean getBoolean(ConfigEntry entry) {
		return getBoolean(entry, null);
	}

}