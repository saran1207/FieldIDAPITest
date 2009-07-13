package com.n4systems.plugins.integration.impl.cglift.client;

import org.apache.log4j.Logger;

import com.plexus_online.inventory.JobDataForManufacturingNo;
import com.plexus_online.inventory.JobDataForTrackingNo;

public class PlexusFactory {
	private static Logger logger = Logger.getLogger(PlexusFactory.class);
	
	public static InventoryTransactionsSoap createService() {
		logger.info("Creating Plexus Service");
		
		InventoryTransactionsClient inventoryTransactionsClient = new InventoryTransactionsClient();
		
		return inventoryTransactionsClient.getInventoryTransactionsSoap();
	}
	
	public static JobDataForManufacturingNo createJobDataForManufacturingNo(String preReelID) {
		JobDataForManufacturingNo.Container plexusContainer = new JobDataForManufacturingNo.Container();
		plexusContainer.setManufacturingNo(preReelID);
		
		JobDataForManufacturingNo plexusJobData = new JobDataForManufacturingNo();
		plexusJobData.setContainer(plexusContainer);
		
		return plexusJobData;
	}
	
	public static JobDataForTrackingNo createJobDataForTrackingNo(String reelID) {
		JobDataForTrackingNo.Container plexusContainer = new JobDataForTrackingNo.Container();
		plexusContainer.setTrackingNo(reelID);
		
		JobDataForTrackingNo plexusJobData = new JobDataForTrackingNo();
		plexusJobData.setContainer(plexusContainer);
		
		return plexusJobData;
	}
}
