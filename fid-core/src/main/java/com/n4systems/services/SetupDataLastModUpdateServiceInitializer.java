package com.n4systems.services;

import org.apache.log4j.Logger;

public class SetupDataLastModUpdateServiceInitializer implements Initializer {
	private static Logger logger = Logger.getLogger(SetupDataLastModUpdateServiceInitializer.class);
	
	public SetupDataLastModUpdateServiceInitializer() {}
	
	public void initialize() {
		logger.info("Initializing SetupDataLastModUpdateService ... ");
		SetupDataLastModUpdateService.getInstance().loadModDates();
		logger.info("Complete");
	}

	public void uninitialize() {
		logger.info("Uninitializing SetupDataLastModUpdateService ... ");
		SetupDataLastModUpdateService.getInstance().clearModDates();
		logger.info("Complete");
	}

}
