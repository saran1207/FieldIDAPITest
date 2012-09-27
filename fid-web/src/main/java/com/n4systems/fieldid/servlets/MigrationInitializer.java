package com.n4systems.fieldid.servlets;

import com.n4systems.fieldid.migrator.MigrationRunner;
import com.n4systems.services.Initializer;

public class MigrationInitializer implements Initializer {
    @Override
    public void initialize() throws Exception {
		MigrationRunner runner = new MigrationRunner();
		runner.execute();
	}

	@Override
	public void uninitialize() {

	}
}
