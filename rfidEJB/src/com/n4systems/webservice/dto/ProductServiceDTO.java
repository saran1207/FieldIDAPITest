package com.n4systems.webservice.dto;

import static com.n4systems.webservice.dto.MobileDTOHelper.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceDTO extends AbstractBaseDTOWithOwner implements ProductLookupable {

	private static final Long UNSET_ID = 0L;
	private String serialNumber;
	private String rfidNumber;
	private String comments;
	private String customerRefNumber;
	private String mobileGuid;
	private String purchaseOrder;
	private String location;
	private String orderNumber;
	private String identified; // date
	private long modifiedById;
	private long identifiedById;
	private String lastInspectionDate; //date
	private long productTypeId;
	private List<InfoOptionServiceDTO> infoOptions = new ArrayList<InfoOptionServiceDTO>();
	private long productStatusId;
	private String serverRequestGuid;
	private List<SubProductMapServiceDTO> subProducts = new ArrayList<SubProductMapServiceDTO>();
	private List<InspectionScheduleServiceDTO> schedules = new ArrayList<InspectionScheduleServiceDTO>();
	private String description;
	private long vendorId;
	private Date modified;
	private long assignedUserId;
	private long predefinedLocationId;
	
	// All of these are unused starting on mobile version 1.14
	private long organizationId;
	private long jobSiteId;

	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getRfidNumber() {
		return rfidNumber;
	}
	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}
	public String getCustomerRefNumber() {
		return customerRefNumber;
	}
	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}
	public String getMobileGuid() {
		return mobileGuid;
	}
	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getIdentified() {
		return identified;
	}
	public void setIdentified(String identified) {
		this.identified = identified;
	}
	public void unsetIdentifedById() {
		this.identifiedById = UNSET_ID;
	}
	
	public boolean isIdentifiedSet() {
		return !UNSET_ID.equals(identifiedById);
	}
	
	public String getLastInspectionDate() {
		return lastInspectionDate;
	}
	public void setLastInspectionDate(String lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}	
	public List<InfoOptionServiceDTO> getInfoOptions() {
		return infoOptions;
	}
	public void setInfoOptions(List<InfoOptionServiceDTO> infoOptions) {
		this.infoOptions = infoOptions;
	}
	
	public boolean productStatusExists() {
		return isValidServerId( productStatusId );
	}
	public long getProductStatusId() {
		return productStatusId;
	}
	public void setProductStatusId(long productStatusId) {
		this.productStatusId = productStatusId;
	}
	
	public long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public boolean identifiedByExists() {
		return isValidServerId( identifiedById );
	}
	public long getIdentifiedById() {
		return identifiedById;
	}
	public void setIdentifiedById(long identifiedById) {
		this.identifiedById = identifiedById;
	}
	public boolean modifiedByIdExists() {
		return isValidServerId(modifiedById);
	}
	public long getModifiedById() {
		return modifiedById;
	}
	public void setModifiedById(long modifiedById) {
		this.modifiedById = modifiedById;
	}
	public String getServerRequestGuid() {
		return serverRequestGuid;
	}
	public void setServerRequestGuid(String serverRequestGuid) {
		this.serverRequestGuid = serverRequestGuid;
	}
	public List<SubProductMapServiceDTO> getSubProducts() {
		return subProducts;
	}
	public void setSubProducts(List<SubProductMapServiceDTO> subProducts) {
		this.subProducts = subProducts;
	}
	public List<InspectionScheduleServiceDTO> getSchedules() {
		return schedules;
	}
	public void setSchedules(List<InspectionScheduleServiceDTO> schedules) {
		this.schedules = schedules;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean jobSiteExists() {
		return isValidServerId( jobSiteId );
	}
	public long getJobSiteId() {
		return jobSiteId;
	}
	public void setJobSiteId(long jobSiteId) {
		this.jobSiteId = jobSiteId;
	}
	public boolean organizationExists() {
		return isValidServerId( organizationId );
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public long getVendorId() {
		return vendorId;
	}
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}	
	public boolean vendorIdExists() {
		return isValidServerId(vendorId);
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}	
	public long getAssignedUserId() {
		return assignedUserId;
	}
	public void setAssignedUserId(long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	public boolean assignedUserIdExists() {
		return isValidServerId( assignedUserId );
	}
	public long getPredefinedLocationId() {
		return predefinedLocationId;
	}
	public void setPredefinedLocationId(long predefinedLocationId) {
		this.predefinedLocationId = predefinedLocationId;
	}
	public boolean predefinedLocationIdExists() {
		return isValidServerId(predefinedLocationId);
	}
	
	
}
