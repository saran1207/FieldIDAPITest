package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class InfoOptionCleanerTask extends ScheduledTask {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(InfoOptionCleanerTask.class);
	
	protected InfoOptionCleanerTask() {
	    super(60 * 60, TimeUnit.SECONDS);
    }

	@Override
    protected void runTask() throws Exception {
		LegacyAssetType assetTypeManager = ServiceLocator.getAssetType();

		int page = 1 ;
		int pageSize = 100;
		while( assetTypeManager.cleanInfoOptions( page , pageSize ) ) {
			page++;
		}
    }
}
