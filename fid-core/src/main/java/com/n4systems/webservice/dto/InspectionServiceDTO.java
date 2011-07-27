package com.n4systems.webservice.dto;

import static com.n4systems.webservice.dto.MobileDTOHelper.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.util.StringUtils;

public class InspectionServiceDTO extends AbstractInspectionServiceDTO implements LocationServiceDTO {

	private String location;
	private Long predefinedLocationId;
	private Date utcDate;
	private boolean printable;
	private long performedById;
	private long inspectionGroupId;
	private long ownerId;
	private String status;
	private String nextDate;
	private long productStatusId;
	private long inspectionScheduleId;
	private String inspectionScheduleMobileGuid;
	private List<SubInspectionServiceDTO> subInspections = new ArrayList<SubInspectionServiceDTO>();
	private List<SubProductMapServiceDTO> newSubProducts = new ArrayList<SubProductMapServiceDTO>();
	private List<SubProductMapServiceDTO> detachSubProducts = new ArrayList<SubProductMapServiceDTO>();
	private long assignedUserId;
	private boolean assignmentIncluded = false;

	/*
	 * These are here only for the mobile side. The mobile will record the
	 * information about the product attached to this inspection. It is done as
	 * a precautinary method so if the product is ever lost we still have the
	 * info recorded in the stored request.
	 */
	private String serialNumber;
	private String rfidNumber;
	private long productTypeId;
	private String eventGroupId;
	
	private long inspectionBookId;
	private String inspectionBookTitle;
	private String eventBookId;
	
	//GPS Data used from version 1.31
	private double longitude;
	private double latitude;
	
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

	public Long getPredefinedLocationId() {
		return predefinedLocationId;
	}

	public void setPredefinedLocationId(Long predefinedLocationId) {
		this.predefinedLocationId = predefinedLocationId;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	@Deprecated
	public long getInspectorId() {
		return performedById;
	}

	@Deprecated
	public void setInspectorId(long performedById) {
		this.performedById = performedById;
	}

	public boolean inspectionGroupExists() {
		return isValidServerId(inspectionGroupId);
	}

	public long getInspectionGroupId() {
		return inspectionGroupId;
	}

	public void setInspectionGroupId(long inspectionGroupId) {
		this.inspectionGroupId = inspectionGroupId;
	}

	public boolean inspectionBookExists() {
		return isValidServerId(inspectionBookId);
	}

	public long getInspectionBookId() {
		return inspectionBookId;
	}

	public void setInspectionBookId(long inspectionBookId) {
		this.inspectionBookId = inspectionBookId;
	}
	
	public String getEventBookId() {
		return eventBookId;
	}

	public void setEventBookId(String eventBookId) {
		this.eventBookId = eventBookId;
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

	public boolean productStatusExists() {
		return isValidServerId(productStatusId);
	}

	public long getProductStatusId() {
		return productStatusId;
	}

	public void setProductStatusId(long productStatusId) {
		this.productStatusId = productStatusId;
	}

	public String getInspectionBookTitle() {
		return inspectionBookTitle;
	}

	public void setInspectionBookTitle(String inspectionBookTitle) {
		this.inspectionBookTitle = inspectionBookTitle;
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
		return isValidServerId(inspectionScheduleId) || StringUtils.isNotEmpty(inspectionScheduleMobileGuid);
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

	public List<SubProductMapServiceDTO> getDetachSubProducts() {
		return detachSubProducts;
	}

	public void setDetachSubProducts(List<SubProductMapServiceDTO> detachSubProducts) {
		this.detachSubProducts = detachSubProducts;
	}

	public long getPerformedById() {
		return performedById;
	}

	public void setPerformedById(long performedById) {
		this.performedById = performedById;
	}

	public String getInspectionScheduleMobileGuid() {
		return inspectionScheduleMobileGuid;
	}

	public void setInspectionScheduleMobileGuid(String inspectionScheduleMobileGuid) {
		this.inspectionScheduleMobileGuid = inspectionScheduleMobileGuid;
	}

	public long getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public boolean assignedUserIdExists() {
		return isValidServerId(assignedUserId);
	}

	public boolean isAssignmentIncluded() {
		return assignmentIncluded;
	}

	public void setAssignmentIncluded(boolean assignmentIncluded) {
		this.assignmentIncluded = assignmentIncluded;
	}

	public String getEventGroupId() {
		return eventGroupId;
	}

	public void setEventGroupId(String eventGroupId) {
		this.eventGroupId = eventGroupId;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

}
