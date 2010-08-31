
package com.n4systems.plugins.integration.impl.cglift.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import com.plexus_online.inventory.JobDataForManufacturingNoResponse;
import com.plexus_online.inventory.JobDataForTrackingNoResponse;

@WebService(name = "InventoryTransactionsSoap", targetNamespace = "http://www.plexus-online.com/Inventory")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface InventoryTransactionsSoap {

	@WebMethod(operationName = "JobDataForManufacturingNo", action = "http://www.plexus-online.com/Inventory/JobDataForManufacturingNo")
	@WebResult(name = "JobDataForManufacturingNoResponse", targetNamespace = "http://www.plexus-online.com/Inventory")
	public JobDataForManufacturingNoResponse jobDataForManufacturingNo(
			@WebParam(name = "JobDataForManufacturingNo", targetNamespace = "http://www.plexus-online.com/Inventory")
			com.plexus_online.inventory.JobDataForManufacturingNo JobDataForManufacturingNo);

	@WebMethod(operationName = "JobDataForTrackingNo", action = "http://www.plexus-online.com/Inventory/JobDataForTrackingNo")
	@WebResult(name = "JobDataForTrackingNoResponse", targetNamespace = "http://www.plexus-online.com/Inventory")
	public JobDataForTrackingNoResponse jobDataForTrackingNo(
			@WebParam(name = "JobDataForTrackingNo", targetNamespace = "http://www.plexus-online.com/Inventory")
			com.plexus_online.inventory.JobDataForTrackingNo JobDataForTrackingNo);

}
