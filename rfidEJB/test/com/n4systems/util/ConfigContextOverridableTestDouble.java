package com.n4systems.util;

import com.n4systems.model.Configuration;

public class ConfigContextOverridableTestDouble extends ConfigContext {

	public ConfigContextOverridableTestDouble() {
		super();
		markClean();
	}
	
	protected void reloadConfigurations() {
		configruations.clear();
	}
	
	
	public void addConfigurationValue(ConfigEntry entry, Object value) {
		configruations.add(new Configuration(entry, value.toString()));
		
	}


}
