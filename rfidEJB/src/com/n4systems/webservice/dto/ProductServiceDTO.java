package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceDTO extends AbstractBaseServiceDTO {

	private String serialNumber;
	private String rfidNumber;
	private String comments;
	private String customerRefNumber;
	private String mobileGuid;
	private String purchaseOrder;
	private String location;
	private String orderNumber;
	private long organizationId;
	private String identified; // date
	private long identifiedById;
	private String lastInspectionDate; //date
	private long productTypeId;
	private List<InfoOptionServiceDTO> infoOptions = new ArrayList<InfoOptionServiceDTO>();
	private long productStatusId;
	private long customerId;
	private long divisionId;
	private long jobSiteId;
	private String serverRequestGuid;
	private List<SubProductMapServiceDTO> subProducts = new ArrayList<SubProductMapServiceDTO>();
	private List<InspectionScheduleServiceDTO> schedules = new ArrayList<InspectionScheduleServiceDTO>();
	
	
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
	
	public boolean organizationExists() {
		return isValidServerId( organizationId );
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public String getIdentified() {
		return identified;
	}
	public void setIdentified(String identified) {
		this.identified = identified;
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
	
	public boolean customerExists() {
		return isValidServerId( customerId );
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	public boolean divisionExists() {
		return isValidServerId( divisionId );
	}
	public long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
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
}
