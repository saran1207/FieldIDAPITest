package com.n4systems.taskscheduling.task;

import com.n4systems.services.Initializer;
import com.n4systems.taskscheduling.TaskExecutor;

public class SignUpPackageSyncTaskInitializer implements Initializer {

	public void initialize() {
		TaskExecutor.getInstance().execute(new SignUpPackageSyncTask());
	}

	public void uninitialize() {
	}
}
