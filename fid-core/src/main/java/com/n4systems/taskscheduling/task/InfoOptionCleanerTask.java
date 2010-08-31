package com.n4systems.taskscheduling.task;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;

public class InfoOptionCleanerTask extends ScheduledTask {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(InfoOptionCleanerTask.class);
	
	protected InfoOptionCleanerTask() {
	    super(60 * 60, TimeUnit.SECONDS);
    }

	@Override
    protected void runTask() throws Exception {
		LegacyProductType productTypeManager = ServiceLocator.getProductType();

		int page = 1 ;
		int pageSize = 100;
		while( productTypeManager.cleanInfoOptions( page , pageSize ) ) {
			page++;
		}
    }
}
