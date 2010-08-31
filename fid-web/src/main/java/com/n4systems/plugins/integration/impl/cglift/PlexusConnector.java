package com.n4systems.plugins.integration.impl.cglift;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.n4systems.plugins.PluginException;
import com.n4systems.plugins.integration.CustomerOrderTransfer;
import com.n4systems.plugins.integration.OrderResolver;
import com.n4systems.plugins.integration.ShopOrderTransfer;
import com.n4systems.plugins.integration.impl.cglift.client.PlexusFactory;
import com.plexus_online.inventory.JobDataForManufacturingNo;
import com.plexus_online.inventory.JobDataForManufacturingNoResponse;
import com.plexus_online.inventory.JobDataForTrackingNo;
import com.plexus_online.inventory.JobDataForTrackingNoResponse;


public class PlexusConnector implements OrderResolver {
	private Logger logger = Logger.getLogger(OrderResolver.class);
	private static final String PLEXUS_DATE_FORMAT = "MM/dd/yyyy hh:mm:ss aa";
	
	public ShopOrderTransfer findByReelID(String reelID) {
		logger.info("Polling Plexus by TrackingNo [" + reelID + "]");
		
		JobDataForTrackingNo plexusJobData = PlexusFactory.createJobDataForTrackingNo(reelID);
		JobDataForTrackingNoResponse plexusJob = PlexusFactory.createService().jobDataForTrackingNo(plexusJobData);
		
		// check to see that Plexus returned an order
		if(plexusJob == null || plexusJob.getJob() == null || plexusJob.getJob().getOrderNo() == null || plexusJob.getJob().getOrderNo().trim().length() == 0) {
			return null;
		}
		
		JobDataForTrackingNoResponse.Job jobData = plexusJob.getJob();
		
		ShopOrderTransfer order = new ShopOrderTransfer();
		
		order.setOrderNumber(jobData.getOrderNo());
		order.setCustomerId(jobData.getCustomerCode());
		order.setCustomerName(jobData.getCustomerName());
		order.setDivisionName(jobData.getCustomerAddress());
		order.setPoNumber(jobData.getPONo());

		// Plexus orders always have a quantity of 1
		order.setOrderQuantity(1L);
		
		try {
			order.setOrderDate((new SimpleDateFormat(PLEXUS_DATE_FORMAT)).parse(jobData.getPODate()));
		} catch (Exception e) {
			logger.warn("Parsing plexus PO date", e);
		}
		
		order.setLineItemDescription(jobData.getPartDescription());
		
		// cg uses both the part group and part type for their product codes
		order.setProductCode((jobData.getPartGroup() + " " + jobData.getPartType()).trim());
		
		// the plexus line items don't have a quantity field ... strange ...
		order.setOrderQuantity(1L);
		
		/*
		 * I've changed the line items to externally resolve against the part number.  This may actually be incorrect.
		 * I'm assuming that the part numbers will be unique for each line within an order but since orders come in
		 * from plexus denormalized and one line at a time, it's hard to tell.
		 * This used to resolve against the plexus lineNumber field however that appears to always come in as null.
		 */
		order.setLineItemId(jobData.getPartNo());
		
		return order;
	}
	
	public ShopOrderTransfer findByPreReelID(String preReelID) {
		logger.info("Polling Plexus by ManufacturingNo [" + preReelID + "]");
		
		JobDataForManufacturingNo plexusJobData = PlexusFactory.createJobDataForManufacturingNo(preReelID);
		JobDataForManufacturingNoResponse plexusJob = PlexusFactory.createService().jobDataForManufacturingNo(plexusJobData);

		// check to see that Plexus returned an order
		if(plexusJob == null || plexusJob.getJob() == null || plexusJob.getJob().getOrderNo() == null || plexusJob.getJob().getOrderNo().trim().length() == 0) {
			return null;
		}
		
		JobDataForManufacturingNoResponse.Job jobData = plexusJob.getJob();
		
		ShopOrderTransfer order = new ShopOrderTransfer();
		
		order.setOrderNumber(jobData.getOrderNo());
		order.setCustomerId(jobData.getCustomerCode());
		order.setCustomerName(jobData.getCustomerName());
		order.setDivisionName(jobData.getCustomerAddress());
		order.setPoNumber(jobData.getPONo());

		// Plexus orders always have a quantity of 1
		order.setOrderQuantity(1L);
		
		try {
			order.setOrderDate((new SimpleDateFormat(PLEXUS_DATE_FORMAT)).parse(jobData.getPODate()));
		} catch (Exception e) {
			logger.warn("Parsing plexus PO date", e);
		}
		
		order.setLineItemDescription(jobData.getPartDescription());
		
		// cg uses both the part group and part type for their product codes
		order.setProductCode((jobData.getPartGroup() + " " + jobData.getPartType()).trim());
		
		// the plexus line items don't have a quantity field ... strange ...
		order.setOrderQuantity(1L);
		
		/*
		 * I've changed the line items to externally resolve against the part number.  This may actually be incorrect.
		 * I'm assuming that the part numbers will be unique for each line within an order but since orders come in
		 * from plexus denormalized and one line at a time, it's hard to tell.
		 * This used to resolve against the plexus lineNumber field however that appears to always come in as null.
		 */
		order.setLineItemId(jobData.getPartNo());
		
		return order;
	}

	
	public ShopOrderTransfer findShopOrder(String orderNumber, String organizationName) throws PluginException {
		
		ShopOrderTransfer orderTransfer = null;
		try {	
			// to find our order, we'll first look by reelid and then by prereelid
			orderTransfer = findByReelID(orderNumber);
			
			if(orderTransfer == null) {
				orderTransfer = findByPreReelID(orderNumber);
			}
		} catch(RuntimeException e) {
			// none of the plexus classes throw exceptions but we should still handle RuntimeExceptions
			throw new PluginException("Failed Plexus request: " + e.getMessage());
		}
		
		return orderTransfer;
	}

	// TODO: Implement Plexus connection testing.
	public boolean testConnection() throws PluginException {
		return true;
	}
	
	// CG only uses Shop orders
	public CustomerOrderTransfer findCustomerOrder(String orderNumber, String organizationName) throws PluginException {
		return null;
	}
	
}
