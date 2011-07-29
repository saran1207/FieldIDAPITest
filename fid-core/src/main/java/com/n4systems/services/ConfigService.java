package com.n4systems.services;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Configuration;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Transactional
public class ConfigService extends FieldIdPersistenceService implements ConfigurationProvider {
	
	private Configuration findGlobalEntry(ConfigEntry entry) {
		QueryBuilder<Configuration> builder = new QueryBuilder<Configuration>(Configuration.class);
		builder.addWhere(WhereClauseFactory.create("identifier", entry));
		builder.addWhere(WhereClauseFactory.createIsNull("tenantId"));
		Configuration config = persistenceService.find(builder);
		return config;
	}
	
	private Configuration findTenantEntry(ConfigEntry entry, Long tenantId) {
		QueryBuilder<Configuration> builder = new QueryBuilder<Configuration>(Configuration.class);
		builder.addWhere(WhereClauseFactory.create("identifier", entry));
		builder.addWhere(WhereClauseFactory.create("tenantId", tenantId));
		Configuration config = persistenceService.find(builder);
		return config;
	}
	
	private Configuration getEntry(ConfigEntry entry, Long tenantId) {
		Configuration config = null;
		if (tenantId != null) {
			config = findTenantEntry(entry, tenantId);
		}
		if (config == null) {
			config = findGlobalEntry(entry);
		}
		return config;
	}
	
	public String getString(ConfigEntry entry, Long tenantId) {
		Configuration config = getEntry(entry, tenantId);
		
		String value;
		if(config != null) {
			value = config.getValue();
		} else {
			value = entry.getDefaultValue();
		}
		
		return value;
	}
	
	public String getString(ConfigEntry entry) {
		return getString(entry, null);
	}
	
	public Integer getInteger(ConfigEntry entry, Long tenantId) {
		try {
			return Integer.valueOf(getString(entry, tenantId));
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	public Integer getInteger(ConfigEntry entry) {
		return getInteger(entry, null);
	}
	
	public Long getLong(ConfigEntry entry, Long tenantId) {
		try {
			return Long.valueOf(getString(entry, tenantId));
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	public Long getLong(ConfigEntry entry) {
		return getLong(entry, null);
	}
	
	public Boolean getBoolean(ConfigEntry entry, Long tenantId) {
		try {
			return Boolean.valueOf(getString(entry, tenantId));
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	public Boolean getBoolean(ConfigEntry entry) {
		return getBoolean(entry, null);
	}
}
