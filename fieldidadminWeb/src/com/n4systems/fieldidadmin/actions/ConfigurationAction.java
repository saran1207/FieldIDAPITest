package com.n4systems.fieldidadmin.actions;

import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.TenantOrganization;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListHelper;
import com.opensymphony.xwork2.ActionSupport;

public class ConfigurationAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private ConfigManager configManager;
	private PersistenceManager persistenceManager;
	
	private ConfigEntry key;
	private Long tenantId;
	private String value;
	
	public ConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}
	
	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public String doLoad() {
		return SUCCESS;
	}
	
	public String doGetConfiguration() {
		if(key != null) {
			String cfgVal = ConfigContext.getCurrentContext().getString(key, tenantId); 
			
			if(cfgVal.equals(key.getDefaultValue())) {
				cfgVal += " (default)";
			}
			
			setValue(cfgVal);
		}
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			configManager.saveEntry(key, tenantId, value);
		} catch(Exception e) {
			e.printStackTrace(System.err);
			addActionError("Failed to save/update key: [" + key + "] with value: [" + value + "]");
			return ERROR;
		}
		
		addActionMessage("Saved key: " + key);
		return SUCCESS;
	}
	
	public String doRemove() {
		try {
			configManager.removeEntry(key, tenantId);
		} catch(Exception e) {
			e.printStackTrace(System.err);
			addActionError("Failed to delete: " + key);
			return ERROR;
		}
		
		addActionMessage("Deletd: " + key);
		return SUCCESS;
	}
	
	public Map<String, String> getConfigs() {
		Map<String, String> configMap = new LinkedHashMap<String, String>();
		
		for(ConfigEntry entry: ConfigEntry.values()) {
			configMap.put(entry.name(), entry.name());
		}
		
		return configMap;
	}
	
	public Map<Long, String> getTenants() {
		return ListHelper.longListableToMap(persistenceManager.findAll(TenantOrganization.class));
	}

	public String getKey() {
		return key.name();
	}

	public void setKey(String key) {
		this.key = ConfigEntry.valueOf(key);
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
