package com.n4systems.model.search;

import java.util.Date;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.location.Location;
import com.n4systems.model.user.User;
import com.n4systems.util.chart.ChartDateRange;

public class EventReportCriteriaModel extends SearchCriteriaModel {

    private Location location = new Location();
    private Date fromDate;
    private Date toDate;
    private AssetType assetType;
    private AssetTypeGroup assetTypeGroup;
    private User assignedTo;
    private ChartDateRange dateRange;

    private EventType eventType;
    private EventTypeGroup eventTypeGroup;

    private AssetStatus assetStatus;

    private EventBook eventBook;
    private Project job;

    private String rfidNumber;
    private String identifier;
    private String referenceNumber;

    private String orderNumber;
    private String purchaseOrder;

    private Status result;

    private User performedBy;

    private boolean includeSafetyNetwork;

    private Long savedReportId;
    private String savedReportName;


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getFromDate() {
    	return dateRange != null ? dateRange.getFromDate() : fromDate; 
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
    	return dateRange != null ? dateRange.getToDate() : toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public AssetTypeGroup getAssetTypeGroup() {
        return assetTypeGroup;
    }

    public void setAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        this.assetTypeGroup = assetTypeGroup;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventTypeGroup getEventTypeGroup() {
        return eventTypeGroup;
    }

    public void setEventTypeGroup(EventTypeGroup eventTypeGroup) {
        this.eventTypeGroup = eventTypeGroup;
    }

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public EventBook getEventBook() {
        return eventBook;
    }

    public void setEventBook(EventBook eventBook) {
        this.eventBook = eventBook;
    }

    public Project getJob() {
        return job;
    }

    public void setJob(Project job) {
        this.job = job;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public Status getResult() {
        return result;
    }

    public void setResult(Status result) {
        this.result = result;
    }

    public boolean isIncludeSafetyNetwork() {
        return includeSafetyNetwork;
    }

    public void setIncludeSafetyNetwork(boolean includeSafetyNetwork) {
        this.includeSafetyNetwork = includeSafetyNetwork;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public Long getSavedReportId() {
        return savedReportId;
    }

    public void setSavedReportId(Long savedReportId) {
        this.savedReportId = savedReportId;
    }

    public String getSavedReportName() {
        return savedReportName;
    }

    public void setSavedReportName(String savedReportName) {
        this.savedReportName = savedReportName;
    }

	public void setDateRange(ChartDateRange dateRange) {
		this.dateRange = dateRange;
	}

	public ChartDateRange getDateRange() {
		return dateRange;
	}

}
