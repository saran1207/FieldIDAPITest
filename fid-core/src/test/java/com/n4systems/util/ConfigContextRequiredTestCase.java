package com.n4systems.util;

import com.n4systems.services.config.ConfigServiceTestManager;
import org.junit.After;
import org.junit.Before;

public abstract class ConfigContextRequiredTestCase {
	@Before
	public void changeConfigContext() {
		ConfigServiceTestManager.setInstance(new ConfigContextOverridableTestDouble());
	}
	
	@After 
	public void removeConfig() {
		ConfigServiceTestManager.resetInstance();
	}
}

