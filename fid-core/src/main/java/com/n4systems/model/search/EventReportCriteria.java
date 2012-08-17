package com.n4systems.model.search;

import com.n4systems.model.*;
import com.n4systems.model.location.Location;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="saved_reports")
public class EventReportCriteria extends SearchCriteria {

    private static final String EVENT_LOCATION_COLUMN = "event_search_location";
    private static final String EVENT_DIVISION_COLUMN = "event_search_division";
    private static final String EVENT_CUSTOMER_COLUMN = "event_search_customer";
    private static final String EVENT_ORGANIZATION_COLUMN = "event_search_organization";

    private Location location = new Location();

    @ManyToOne
    @JoinColumn(name="assetType")
    private AssetType assetType;

    @ManyToOne
    @JoinColumn(name="assignedUser")
    private User assignedTo;

    @Column(name="assignee")
    private Long assigneeId;

    @ManyToOne
    @JoinColumn(name="eventTypeId")
    private EventType eventType;

    @Embedded
    private DateRange dateRange = new DateRange(RangeType.CUSTOM);

    @ManyToOne
    @JoinColumn(name="eventTypeGroup")
    private EventTypeGroup eventTypeGroup;

    @ManyToOne
    @JoinColumn(name="assetStatus")
    private AssetStatus assetStatus;

    @ManyToOne
    @JoinColumn(name="eventBook")
    @NotFound(action=NotFoundAction.IGNORE)
    private EventBook eventBook;

    @ManyToOne
    @JoinColumn(name="jobId")
    private Project job;

    @Column
    private String rfidNumber;

    @Column
    private String identifier;

    @Column
    private String referenceNumber;

    @Column
    private String orderNumber;

    @Column(name="purchaseOrderNumber")
    private String purchaseOrder;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status result;

    @ManyToOne
    @JoinColumn(name="performedBy")
    private User performedBy;

    @Column(name="includeSafetyNetwork")
    private boolean includeSafetyNetwork;

	@ElementCollection(fetch= FetchType.EAGER)
	@IndexColumn(name="idx")
    @JoinTable(name="saved_reports_columns", joinColumns = {@JoinColumn(name="saved_report_id")})
    @Column(name="column_id")
	private List<String> columns = new ArrayList<String>();

    @Column(name="eventState")
    @Enumerated(EnumType.STRING)
    private EventState eventState = EventState.COMPLETE;

    @Column(name="includeDueDateRange")
    @Enumerated(EnumType.STRING)
    private IncludeDueDateRange includeDueDateRange;

    @Embedded
	@AttributeOverrides({
		@AttributeOverride(name="rangeType", column = @Column(name="dueDateRange")),
		@AttributeOverride(name="fromDate", column = @Column(name="dueFromDate")),
        @AttributeOverride(name="toDate", column = @Column(name="dueToDate"))
	})
    private DateRange dueDateRange = new DateRange(RangeType.CUSTOM);

    @ManyToOne
    @JoinColumn(name="eventStatus")
    private EventStatus eventStatus;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

    @Override
	public List<String> getColumns() {
        return columns;
    }

    @Override
	public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    public DateRange getDueDateRange() {
        return dueDateRange;
    }

    public void setDueDateRange(DateRange dueDateRange) {
        this.dueDateRange = dueDateRange;
    }

    public IncludeDueDateRange getIncludeDueDateRange() {
        return includeDueDateRange;
    }

    public void setIncludeDueDateRange(IncludeDueDateRange includeDueDateRange) {
        this.includeDueDateRange = includeDueDateRange;
    }

    public void clearDateRanges() {
        setIncludeDueDateRange(null);
        this.dateRange = new DateRange(RangeType.CUSTOM);
        this.dueDateRange = new DateRange(RangeType.CUSTOM);
    }

    public boolean sortingByLocation() {
        ColumnMappingView sortColumn = getSortColumn();
        return sortColumn != null && EVENT_LOCATION_COLUMN.equals(sortColumn.getId());
    }

    public boolean sortingByDivision() {
        ColumnMappingView sortColumn = getSortColumn();
        return sortColumn != null && EVENT_DIVISION_COLUMN.equals(sortColumn.getId());
    }

    public boolean sortingByCustomer() {
        ColumnMappingView sortColumn = getSortColumn();
        return sortColumn != null && EVENT_CUSTOMER_COLUMN.equals(sortColumn.getId());
    }

    public boolean sortingByOrganization() {
        ColumnMappingView sortColumn = getSortColumn();
        return sortColumn != null && EVENT_ORGANIZATION_COLUMN.equals(sortColumn.getId());
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

}
