package com.n4systems.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.jboss.logging.Logger;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Configuration;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

@Interceptors({TimingInterceptor.class})
@Stateless
public class ConfigManagerImpl implements ConfigManager {
	private Logger logger = Logger.getLogger(ConfigManager.class);
	
	@EJB private PersistenceManager persistenceManager;

	public Configuration findEntry(ConfigEntry entry, Long tenantId) {
		QueryBuilder<Configuration> builder = new QueryBuilder<Configuration>(Configuration.class, new OpenSecurityFilter());
		builder.addSimpleWhere("key", entry);
		
		if(tenantId != null) {
			builder.addSimpleWhere("tenantId", tenantId);
		} else {
			// the Long generic is simply to satisfy WhereParameter, it has no real use
			builder.addWhere(new WhereParameter<Long>(WhereParameter.Comparator.NULL, "tenantId"));
		}
		
		Configuration config = null;
		try {
			config = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Unable to find config entry [" + entry.name() + "] tenant [" + tenantId + "]", e);
		}
		
		return config;
	}
	
	public Configuration findEntry(ConfigEntry entry) {
		return findEntry(entry, null);
	}
	
	public void removeEntry(Configuration entry) {
		/*
		 *  since we pretty much ignore, the id on ConfigurationBean we 
		 *  need to lookup the entry by it's key and tenant first.  See ConfigurationBean
		 *  for more information
		 */
		Configuration config = findEntry(entry.getKey(), entry.getTenantId());
		
		if(config != null) {
			try {
				persistenceManager.delete(config);
			} finally {
				// mark the current context dirty since changes have been made
				ConfigContext.getCurrentContext().markDirty();
			}
		}

	}
	
	public void removeEntry(ConfigEntry entry, Long tenantId) {
		removeEntry(new Configuration(entry, null, tenantId));
	}
	
	public void removeEntry(ConfigEntry entry) {
		removeEntry(entry, null);
	}
	
	public void saveEntry(Configuration entry) {
		/*
		 *  since we pretty much ignore, the id on ConfigurationBean we 
		 *  need to lookup the entry by it's key and tenant first.  See ConfigurationBean
		 *  for more information
		 */
		Configuration config = findEntry(entry.getKey(), entry.getTenantId());
		
		try {
			if(config != null) {
				// update the new value and merge it back in
				config.setValue(entry.getValue());
				persistenceManager.update(config);
			} else {
				// the entry appears to be new, save it
				persistenceManager.save(entry);
			}
		} finally {
			// mark the current context dirty since changes have been made
			ConfigContext.getCurrentContext().markDirty();
		}
	}
	
	public <T> void saveEntry(ConfigEntry entry, Long tenantId, T value) {
		// convert the value into a string first and save
		saveEntry(new Configuration(entry, value.toString(), tenantId));
	}
	
	public <T> void saveEntry(ConfigEntry entry, T value) {
		saveEntry(entry, null, value);
	}
	
}
