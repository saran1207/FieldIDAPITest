package com.n4systems.model.search;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.GpsBounds;
import com.n4systems.model.location.Location;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.persistence.search.AssetLockoutTagoutStatus;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="saved_searches")
public class AssetSearchCriteria extends SearchCriteria {

    private static final String LAST_EVENT_DATE_COLUMN = "asset_search_lasteventdate";

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

    private @Transient Integer maxItemsBeforeGrouping = 199;

    @Embedded
    private GpsBounds bounds;

    @Column
    private Boolean hasGps;

    @Enumerated(EnumType.STRING)
    @Column(name="assetLockoutTagoutStatus")
    private AssetLockoutTagoutStatus assetLockoutTagoutStatus;

    public AssetLockoutTagoutStatus getAssetLockoutTagoutStatus() {
        return assetLockoutTagoutStatus;
    }

    public void setAssetLockoutTagoutStatus(AssetLockoutTagoutStatus assetLockoutTagoutStatus) {
        this.assetLockoutTagoutStatus = assetLockoutTagoutStatus;
    }

    public GpsBounds getBounds() {
        return bounds;
    }

    public void setBounds(GpsBounds bounds) {
        this.bounds = bounds;
    }

    public Boolean getHasGps() {
        return hasGps;
    }

    public void setHasGps(Boolean hasGps) {
        this.hasGps = hasGps;
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

    public Integer getMaxItemsBeforeGrouping() {
        return maxItemsBeforeGrouping;
    }

    @Transient
    public boolean sortingByLastEventDate() {
        ColumnMappingView sortColumn = getSortColumn();
        return sortColumn != null && LAST_EVENT_DATE_COLUMN.equals(sortColumn.getId());
    }

    @Transient
    public boolean sortingByOrIncludingLastEventDate() {
        if (sortingByLastEventDate()) {
            return true;
        }
        for (ColumnMappingView columnMappingView : getSortedStaticAndDynamicColumns()) {
            if (columnMappingView.getId().equals(LAST_EVENT_DATE_COLUMN)) {
                return true;
            }
        }
        return false;
    }

}
