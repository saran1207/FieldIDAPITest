package com.n4systems.taskscheduling.task;

import org.jboss.logging.Logger;

import com.n4systems.services.Initializer;

public class SignUpPackageSyncTaskInitializer implements Initializer {

	Logger logger = Logger.getLogger(SignUpPackageSyncTaskInitializer.class);
	
	public void initialize() {
		try {
			new SignUpPackageSyncTask().runTask();
		} catch (Exception e) {
			logger.error("Problem executing sign up package sync task in initializer", e);
		}
	}

	public void uninitialize() {
	}
}
