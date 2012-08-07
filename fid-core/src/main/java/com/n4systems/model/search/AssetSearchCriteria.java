package com.n4systems.model.search;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.location.Location;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="saved_searches")
public class AssetSearchCriteria extends SearchCriteria {

    @Column
    private String rfidNumber;

    @Column
    private String identifier;

    @Column
    private String referenceNumber;

    private Location location = new Location();

    @Column
    private String orderNumber;

    @Column(name="purchaseOrderNumber")
    private String purchaseOrder;

    @ManyToOne
    @JoinColumn(name="assetStatus")
    private AssetStatus assetStatus;

    @ManyToOne
    @JoinColumn(name="assetType")
    private AssetType assetType;

    private DateRange dateRange = new DateRange(RangeType.CUSTOM);

    @ManyToOne
    @JoinColumn(name="assignedUser")
    private User assignedTo;

	@ElementCollection(fetch= FetchType.EAGER)
	@IndexColumn(name="idx")
    @JoinTable(name="saved_searches_columns", joinColumns = {@JoinColumn(name="saved_search_id")})
    @Column(name="column_id")
	private List<String> columns = new ArrayList<String>();
    
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

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }


    ///????? need to resolve this one????
//    public Date getIdentifiedFromDate() {
//    	return dateRange.calculateFromDate();
//    }
//
//    public Date getIdentifiedToDate() {
//		return dateRange.calculateToDate();
//	}

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
