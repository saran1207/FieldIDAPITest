package com.n4systems.testutils;

import com.n4systems.model.Configuration;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class TestConfigContext extends ConfigContext {

	private TestConfigContext() {}
	
	public static TestConfigContext newContext() {
		TestConfigContext textContext = new TestConfigContext();
		textContext.setAsCurrentContext();
		return textContext;
	}
	
	private void setAsCurrentContext() {
		markClean();
		ConfigContext.setCurrentContext(this);
	}
	
	@Override
	protected void reloadConfigurations() {
		configruations.clear();
		markClean();
	}

	public TestConfigContext setEntry(ConfigEntry entry, Object value) {
		return setEntry(entry, value, null);
	}
	
	public TestConfigContext setEntry(ConfigEntry entry, Object value, Long tenantId) {
		Configuration config = new Configuration();
		config.setIdentifier(entry);
		config.setValue(String.valueOf(value));
		config.setTenantId(tenantId);
		
		configruations.add(config);
		return this;
	}
	
}
