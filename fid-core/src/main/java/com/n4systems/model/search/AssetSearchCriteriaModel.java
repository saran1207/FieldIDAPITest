package com.n4systems.model.search;

import java.util.Date;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.chart.ChartDateRange;

public class AssetSearchCriteriaModel extends SearchCriteriaModel {

    private String rfidNumber;
    private String identifier;
    private String referenceNumber;

    private Location location = new Location();
    private BaseOrg owner;

    private String orderNumber;
    private String purchaseOrder;

    private AssetStatus assetStatus;
    private AssetTypeGroup assetTypeGroup;
    private AssetType assetType;

    private Date identifiedFromDate;
    private Date identifiedToDate;
    private ChartDateRange dateRange = ChartDateRange.FOREVER;

    private User assignedTo;
    
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
	public BaseOrg getOwner() {
        return owner;
    }

    @Override
	public void setOwner(BaseOrg owner) {
        this.owner = owner;
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

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public AssetTypeGroup getAssetTypeGroup() {
        return assetTypeGroup;
    }

    public void setAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        this.assetTypeGroup = assetTypeGroup;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public Date getIdentifiedFromDate() {
    	return dateRange!=null ? dateRange.getFromDate() : identifiedFromDate;
    }

    public void setIdentifiedFromDate(Date identifiedFromDate) {
        this.identifiedFromDate = identifiedFromDate;
    }

    public Date getIdentifiedToDate() {
    	return dateRange!=null ? dateRange.getInclusiveToDate() : identifiedToDate;
    }

    public void setIdentifiedToDate(Date identifiedToDate) {
        this.identifiedToDate = identifiedToDate;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

	public void setDateRange(ChartDateRange dateRange) {
		if (!ChartDateRange.CUSTOM.equals(dateRange)) { 
			this.identifiedToDate = this.identifiedFromDate = null;  // mutually exclusive.  can't have specific dates if using non-customized ranges. 
		}
		this.dateRange = dateRange;
	}

	public ChartDateRange getDateRange() {
		return dateRange;
	}
}
