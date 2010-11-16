package com.n4systems.fieldid.viewhelpers;


import java.util.Date;

import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.persistence.search.SortTerm;

public class EventScheduleSearchContainer extends SearchContainer {
	private static final long serialVersionUID = 1L;

	private String rfidNumber;
	private String serialNumber;
	private String orderNumber;
	private String purchaseOrder;
	private LocationWebModel location = new LocationWebModel(this);
	private String referenceNumber;
	private Long assetStatusId;
	private Long assetTypeId;
	private Long assetTypeGroupId;
	private Long assignedUserId;
	private Long eventTypeGroupId;
    private Long eventTypeId;
	private Long jobId;
	private Long jobAndNullId;
	private BaseOrg owner;
	private Date toDate;
	private Date fromDate;
	private CompressedScheduleStatus status = CompressedScheduleStatus.INCOMPLETE;
	
	public EventScheduleSearchContainer(SecurityFilter securityFilter, LoaderFactory loaderFactory) {
		super(EventSchedule.class, "id", securityFilter, loaderFactory);
	}

	@Override
	protected void evalJoinTerms() {
		addLeftJoinTerms("asset.shopOrder.order", "asset.assetStatus", "asset.identifiedBy", "owner.customerOrg", "owner.secondaryOrg", "owner.divisionOrg", "asset.type.group");
		addPredefinedLocationJoin();
	}
	
	@Override
	protected void evalSearchTerms() {
		addWildcardOrStringTerm("asset.rfidNumber", rfidNumber);
		addWildcardOrStringTerm("asset.serialNumber", serialNumber);
		addWildcardOrStringTerm("asset.shopOrder.order.orderNumber", orderNumber);
		addWildcardOrStringTerm("advancedLocation.freeformLocation", location.getFreeformLocation());
		addWildcardOrStringTerm("asset.purchaseOrder", purchaseOrder);
		addWildcardOrStringTerm("asset.customerRefNumber", referenceNumber);
		addSimpleTerm("asset.assetStatus.uniqueID", assetStatusId);
		addSimpleTerm("asset.type.id", assetTypeId);
		addSimpleTerm("asset.type.group.id", assetTypeGroupId);
		addSimpleTerm("eventType.group.id", eventTypeGroupId);
        addSimpleTerm("eventType.id", eventTypeId);
		addSimpleTerm("project.id", jobId);
		addSimpleTermOrNull("project.id", jobAndNullId);
		addDateRangeTerm("nextDate", fromDate, toDate);
		addSimpleInTerm("status", status.getScheduleStatuses());
		
		addPredefinedLocationTerm();
		addAssigUserTerm();
	}
	
	private void addPredefinedLocationJoin() {
		if (location.getPredefinedLocationId() != null) {
			addRequiredLeftJoin("advancedLocation.predefinedLocation.searchIds", "preLocSearchId");
		}
	}
	
	private void addPredefinedLocationTerm() {
		if (location.getPredefinedLocationId() != null) {
			addCustomTerm(new PredefinedLocationSearchTerm("preLocSearchId", location.getPredefinedLocationId()));
		}
	}

	private void addAssigUserTerm() {
		if(assignedUserId != null && assignedUserId == 0) {
			addNullTerm("asset.assignedUser.id");
		} else {
			addSimpleTerm("asset.assignedUser.id", assignedUserId);
		}
	}
	
	@Override
	protected void evalSearchFilters() {
		addOwnerFilter(getOwner());
	}
	
	@Override
	protected String defaultSortColumn() {
		return "nextDate";
	}
	
	@Override
	protected SortTerm.Direction defaultSortDirection() {
		return SortTerm.Direction.ASC;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public Long getAssetStatus() {
		return assetStatusId;
	}

	public void setAssetStatus(Long assetStatus) {
		this.assetStatusId = assetStatus;
	}

	public Long getAssetType() {
		return assetTypeId;
	}

	public void setAssetType(Long assetType) {
		this.assetTypeId = assetType;
	}

	public Long getEventTypeGroup() {
		return eventTypeGroupId;
	}
	
	public void setEventTypeGroup(Long eventTypeGroup) {
		this.eventTypeGroupId = eventTypeGroup;
	}

	public Long getEventType() {
		return eventTypeId;
	}

	public void setEventType(Long eventType) {
		this.eventTypeId = eventType;
	}
	
	public Long getAssignedUser() {
		return assignedUserId;
	}
	
	public void setAssignedUser(Long assignedUser) {
		this.assignedUserId = assignedUser;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getStatus() {
		return status.getName();
	}
	
	public CompressedScheduleStatus getStatusEnum() {
		return status;
	}

	public void setStatus(String status) {
		this.status = CompressedScheduleStatus.valueOf(status);
	}

	public Long getJob() {
		return jobId;
	}

	public void setJob(Long jobId) {
		this.jobId = jobId;
	}

	public Long getJobAndNullId() {
		return jobAndNullId;
	}

	public void setJobAndNullId(Long jobAndNullId) {
		this.jobAndNullId = jobAndNullId;
	}

	public LocationWebModel getLocation() {
		return location;
	}

	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public Long getOwnerId() {
		return (owner != null) ? owner.getId() : null;
	}

	public Long getAssetTypeGroup() {
		return assetTypeGroupId;
	}

	public void setAssetTypeGroup(Long assetTypeGroupId) {
		this.assetTypeGroupId = assetTypeGroupId;
	}
}
