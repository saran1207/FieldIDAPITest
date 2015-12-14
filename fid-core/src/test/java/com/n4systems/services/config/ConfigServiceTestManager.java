package com.n4systems.services.config;

import com.n4systems.util.ConfigContextOverridableTestDouble;

public class ConfigServiceTestManager {

	private static ConfigService storedInstance;

	public static ConfigContextOverridableTestDouble setTestDouble() {
		ConfigContextOverridableTestDouble newInstance = new ConfigContextOverridableTestDouble();
		setInstance(newInstance);
		return newInstance;
	}

	public static void setInstance(ConfigService newInstance) {
		ConfigService.instance = newInstance;
	}

	public static void resetInstance() {
		ConfigService.instance = storedInstance;
		storedInstance = null;
	}

}
