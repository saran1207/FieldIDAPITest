package com.n4systems.fieldid.wicket.model.reporting;

import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.IClusterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventReportCriteriaModel implements IClusterable {

    private BaseOrg owner;
    private Location location = new Location();
    private Date fromDate;
    private Date toDate;
    private AssetType assetType;
    private AssetTypeGroup assetTypeGroup;
    private User assignedTo;

    private EventType eventType;
    private EventTypeGroup eventTypeGroup;

    private AssetStatus assetStatus;

    private EventBook eventBook;
    private Project job;

    private String rfidNumber;
    private String serialNumber;
    private String referenceNumber;

    private String orderNumber;
    private String purchaseOrder;

    private Status result;

    private User performedBy;

    private boolean includeSafetyNetwork;

    private List<ColumnMappingGroupView> columnGroups = new ArrayList<ColumnMappingGroupView>();

    private List<ColumnMappingGroupView> dynamicEventColumnGroups = new ArrayList<ColumnMappingGroupView>();
    private List<ColumnMappingGroupView> dynamicAssetColumnGroups = new ArrayList<ColumnMappingGroupView>();

    private Integer pageNumber = 0;

    private MultiIdSelection selection = new MultiIdSelection();

    private Long savedReportId;
    private String savedReportName;

    private ColumnMappingView sortColumn;
    private SortDirection sortDirection;

    private boolean reportAlreadyRun;

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public List<ColumnMappingGroupView> getColumnGroups() {
        return columnGroups;
    }

    public void setColumnGroups(List<ColumnMappingGroupView> columnGroups) {
        this.columnGroups = columnGroups;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public List<ColumnMappingGroupView> getDynamicEventColumnGroups() {
        return dynamicEventColumnGroups;
    }

    public void setDynamicEventColumnGroups(List<ColumnMappingGroupView> dynamicEventColumnGroups) {
        this.dynamicEventColumnGroups = dynamicEventColumnGroups;
    }

    public List<ColumnMappingGroupView> getDynamicAssetColumnGroups() {
        return dynamicAssetColumnGroups;
    }

    public void setDynamicAssetColumnGroups(List<ColumnMappingGroupView> dynamicAssetColumnGroups) {
        this.dynamicAssetColumnGroups = dynamicAssetColumnGroups;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<ColumnMappingView> getSortedStaticAndDynamicColumns() {
        return getSortedStaticAndDynamicColumns(true);
    }

    public List<ColumnMappingView> getSortedStaticAndDynamicColumns(boolean enabledOnly) {
        List<ColumnMappingGroupView> allColumnGroups = new ArrayList<ColumnMappingGroupView>();
        allColumnGroups.addAll(getColumnGroups());
        allColumnGroups.addAll(getDynamicAssetColumnGroups());
        allColumnGroups.addAll(getDynamicEventColumnGroups());

        List<ColumnMappingView> enabledColumns = new ArrayList<ColumnMappingView>();

        for (ColumnMappingGroupView columnGroup : allColumnGroups) {
            for (ColumnMappingView column : columnGroup.getMappings()) {
                if (!enabledOnly || column.isEnabled()) {
                    enabledColumns.add(column);
                }
            }
        }
        Collections.sort(enabledColumns, new Comparator<ColumnMappingView>() {
            @Override
            public int compare(ColumnMappingView column1, ColumnMappingView column2) {
                return new Integer(column1.getOrder()).compareTo(column2.getOrder());
            }
        });

        return enabledColumns;
    }

    public void enableSelectedColumns(EventSearchContainer eventSearchContainer) {
        List<String> selectedColumns = eventSearchContainer.getSelectedColumns();
        List<ColumnMappingView> sortedStaticAndDynamicColumns = getSortedStaticAndDynamicColumns(false);
        for (ColumnMappingView column : sortedStaticAndDynamicColumns) {
            boolean savedReportContainsColumn = selectedColumns.contains(column.getId());
            column.setEnabled(savedReportContainsColumn);
        }
    }

    public MultiIdSelection getSelection() {
        return selection;
    }

    public void setSelection(MultiIdSelection selection) {
        this.selection = selection;
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

    public ColumnMappingView getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(ColumnMappingView sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public boolean isReportAlreadyRun() {
        return reportAlreadyRun;
    }

    public void setReportAlreadyRun(boolean reportAlreadyRun) {
        this.reportAlreadyRun = reportAlreadyRun;
    }

    public void setSortColumnFromContainer(EventSearchContainer container) {
        List<ColumnMappingView> sortedStaticAndDynamicColumns = getSortedStaticAndDynamicColumns(true);
        for (ColumnMappingView column : sortedStaticAndDynamicColumns) {
            String sortExpression = column.getSortExpression();
            if (sortExpression != null && sortExpression.equals(container.getSortColumn())) {
                setSortColumn(column);
                setSortDirection(SortDirection.ASC.getDisplayName().equals(container.getSortDirection()) ? SortDirection.ASC : SortDirection.DESC);
            }
        }
    }
}
