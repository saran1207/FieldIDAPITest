package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.Event;
import com.n4systems.model.Status;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.NetworkIdSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.DateRange;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.persistence.search.SortDirection;


public class EventSearchContainer extends SearchContainer implements ReportDefiner {
	private static final long serialVersionUID = 1L;
	public static final long UNASSIGNED_USER = 0L;
	
	private Long savedReportId;
	private boolean savedReportModified;
	private boolean includeNetworkResults = false;
	private String rfidNumber;
	private String identifier;
	private String orderNumber;
	private String purchaseOrder;
	private String referenceNumber;
	private LocationWebModel location = new LocationWebModel(this);
	private BaseOrg owner;
	private Long assetTypeId;
	private Long assetTypeGroupId;
	private Long assetStatusId;
	private Long assignedUserId;
    private Long eventTypeId;
	private Long eventTypeGroupId;
	private Long eventBookId;
	private Long jobId;
	private Date fromDate;
	private Date toDate;
	private DateRange dateRange;
	
	private Long performedBy;
	
	private Status status;
	
	public EventSearchContainer(SecurityFilter filter, LoaderFactory loaderFactory, SystemSecurityGuard systemSecurityGuard) {
		super(Event.class, "id", filter, loaderFactory, systemSecurityGuard);
	}
	
	@Override
	protected void evalJoinTerms() {
		addPredefinedLocationJoin();
	}
	
	@Override
	protected void evalSearchTerms() {
		addWildcardOrStringTerm("asset.rfidNumber", rfidNumber);
		addWildcardOrStringTerm("asset.identifier", identifier);
		if(systemSecurityGuard.isIntegrationEnabled()) {
			addWildcardOrStringTerm("asset.shopOrder.order.orderNumber", orderNumber);
		} else {
			addWildcardOrStringTerm("asset.nonIntergrationOrderNumber", orderNumber);
		}
		addWildcardOrStringTerm("asset.purchaseOrder", purchaseOrder);
		addWildcardOrStringTerm("asset.customerRefNumber", referenceNumber);
		addWildcardOrStringTerm("advancedLocation.freeformLocation", location.getFreeformLocation());
		addSimpleTerm("asset.type.id", assetTypeId);
		addSimpleTerm("asset.type.group.id", assetTypeGroupId);
		addSimpleTerm("assetStatus.id", assetStatusId);
        addSimpleTerm("type.id", eventTypeId);
		addSimpleTerm("type.group.id", eventTypeGroupId);
		addSimpleTerm("performedBy.id", performedBy);
		addSimpleTerm("schedule.project.id", jobId);
		addSimpleTerm("status", status);
		addDateRangeTerm("completedDate", fromDate, toDate);
		
		addEventBookTerm();
		addAssignedToTerm();
		addPredefinedLocationTerm();
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

	private void addEventBookTerm() {
		// when eventBookId is 0, we search for events not in a book
		if(eventBookId != null && eventBookId == 0) {
			addNullTerm("book.id");
		} else {
			addSimpleTerm("book.id", eventBookId);
		}
	}

	private void addAssignedToTerm() {
		if (assignedUserId != null) {
			addSimpleTerm("assignedTo.assignmentApplyed", true);
			
			if(assignedUserId == 0) {
				addNullTerm("assignedTo.assignedUser.id");
			} else {
				addSimpleTerm("assignedTo.assignedUser.id", assignedUserId);
			}
		}
	}
	
	@Override
	public SecurityFilter getSecurityFilter() {
		// This is not ideal but if we're including network results,
		// we need to wrap our security filter in a NetworkIdSecurityFilter
		return (includeNetworkResults) ? new NetworkIdSecurityFilter(super.getSecurityFilter(), "asset.networkId") : super.getSecurityFilter();
	}

	@Override
	protected void evalSearchFilters() {
		addOwnerFilter(getOwner());
	}
	
	@Override
	public String defaultSortColumn() {
		return "completedDate";
	}
	
	@Override
	public SortDirection defaultSortDirection() {
		return SortDirection.DESC;
	}
	
	@Override
	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public Long getAssetType() {
		return assetTypeId;
	}

	public void setAssetType(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	
	@Override
	public Long getAssetTypeGroup() {
		return assetTypeGroupId;
	}

	public void setAssetTypeGroup(Long assetTypeGroupId) {
		this.assetTypeGroupId = assetTypeGroupId;
	}
	
	public Long getAssetStatus() {
		return assetStatusId;
	}
	
	public void setAssetStatus(Long assetStatusId) {
		this.assetStatusId = assetStatusId;
	}

	@Override
	public Long getEventTypeGroup() {
		return eventTypeGroupId;
	}

	public void setEventTypeGroup(Long eventTypeGroupId) {
		this.eventTypeGroupId = eventTypeGroupId;
	}

	@Override
	public Long getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(Long performedBy) {
		this.performedBy = performedBy;
	}

	@Override
	public Long getEventBook() {
		return eventBookId;
	}

	public void setEventBook(Long eventBookId) {
		this.eventBookId = eventBookId;
	}

	@Override
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@Override
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@Override
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public Long getAssignedUser() {
		return assignedUserId;
		
	}
	
	public void setAssignedUser(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public boolean isFromSavedReport() {
		return savedReportId != null;
	}
	
	public Long getSavedReportId() {
		return savedReportId;
	}

	public void setSavedReportId(Long savedReportId) {
		this.savedReportId = savedReportId;
	}

	public boolean isSavedReportModified() {
		return savedReportModified;
	}

	public void setSavedReportModified(boolean savedReportModified) {
		this.savedReportModified = savedReportModified;
	}

	public Long getJob() {
		return jobId;
	}

	public void setJob(Long jobId) {
		this.jobId = jobId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public LocationWebModel getLocation() {
		return location;
	}

	@Override
	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public Long getOwnerId() {
		return (owner != null) ? owner.getId() : null;
	}

	public boolean isIncludeNetworkResults() {
		return includeNetworkResults;
	}

	public void setIncludeNetworkResults(boolean includeNetworkResults) {
		this.includeNetworkResults = includeNetworkResults;
	}

	public void setStatus(String status) {
		try {
			this.status = Status.valueOf(status);
		} catch (Exception e) {
			this.status = null;
		}
	}

	public String getStatus() {
		return status != null ? status.name() : null;
	}

    public Long getEventType() {
        return eventTypeId;
    }

    public void setEventType(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public DateRange getDateRange() {
		return dateRange;
	}
}
