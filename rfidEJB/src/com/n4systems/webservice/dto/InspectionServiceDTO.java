package com.n4systems.webservice.dto;

import java.util.Date;
import java.util.List;

public class InspectionServiceDTO extends AbstractInspectionServiceDTO implements DTOHasOwners {

	private String location;	
	private Date utcDate;
	private boolean printable;
	private long inspectorId;
	private long inspectionGroupId;
	private long inspectionBookId;
	private long ownerId;
	private String inspectionBookTitle;
	private String status;
	private String nextDate;
	private long productStatusId;
	private long jobSiteId;
	private long inspectionScheduleId;
	private List<SubInspectionServiceDTO> subInspections;
	private List<SubProductMapServiceDTO> newSubProducts;
	private long orgId;
	private long customerId;
	private long divisionId;

	// These are only used by PRE 1.14 mobile versions; now uses ownerId
	private long organizationId;
	
	/*
	 * These are here only for the mobile side.  
	 * The mobile will record the information about the product attached to this inspection.
	 * It is done as a precautinary method so if the product is ever lost we still have the 
	 * info recorded in the stored request.
	 */
	private String serialNumber;
	private String rfidNumber;
	private long productTypeId;
	
	@Deprecated // in Version 1.11 of the mobile
	private String date;
	
	public List<SubProductMapServiceDTO> getNewSubProducts() {
		return newSubProducts;
	}
	public void setNewSubProducts(List<SubProductMapServiceDTO> newSubProducts) {
		this.newSubProducts = newSubProducts;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isPrintable() {
		return printable;
	}
	public void setPrintable(boolean printable) {
		this.printable = printable;
	}
	public boolean inspectorExists() {
		return isValidServerId( inspectorId );
	}
	public long getInspectorId() {
		return inspectorId;
	}
	public void setInspectorId(long inspectorId) {
		this.inspectorId = inspectorId;
	}
	public boolean inspectionGroupExists() {
		return isValidServerId( inspectionGroupId );
	}
	public long getInspectionGroupId() {
		return inspectionGroupId;
	}
	public void setInspectionGroupId(long inspectionGroupId) {
		this.inspectionGroupId = inspectionGroupId;
	}
	public boolean inspectionBookExists(){
		return isValidServerId( inspectionBookId );
	}
	public long getInspectionBookId() {
		return inspectionBookId;
	}
	public void setInspectionBookId(long inspectionBookId) {
		this.inspectionBookId = inspectionBookId;
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
	public String getStatus() {
		return status;
	}	
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getNextDate() {
		return nextDate;
	}
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	public void setDate(String date) {
		this.date = date;
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
	public String getDate() {
		return date;
	}
	public String getInspectionBookTitle() {
		return inspectionBookTitle;
	}
	public void setInspectionBookTitle(String inspectionBookTitle) {
		this.inspectionBookTitle = inspectionBookTitle;
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
	public List<SubInspectionServiceDTO> getSubInspections() {
		return subInspections;
	}
	public void setSubInspections(List<SubInspectionServiceDTO> subInspections) {
		this.subInspections = subInspections;
	}
	public long getInspectionScheduleId() {
		return inspectionScheduleId;
	}
	public void setInspectionScheduleId(long inspectionScheduleId) {
		this.inspectionScheduleId = inspectionScheduleId;
	}	
	public boolean inspectionScheduleExists() {
		return isValidServerId(inspectionScheduleId);
	}
	public Date getUtcDate() {
		return utcDate;
	}
	public void setUtcDate(Date utcDate) {
		this.utcDate = utcDate;
	}
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
	public long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	public boolean ownerIdExists() {
		return isValidServerId(ownerId);
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}	
}
