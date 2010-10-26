package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.model.Inspection;
import com.n4systems.model.Status;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.NetworkIdSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.persistence.search.SortTerm;


public class InspectionSearchContainer extends SearchContainer implements ReportDefiner {
	private static final long serialVersionUID = 1L;
	public static final long UNASSIGNED_USER = 0L;
	
	private Long savedReportId;
	private boolean savedReportModified;
	private boolean includeNetworkResults = false;
	private String rfidNumber;
	private String serialNumber;
	private String orderNumber;
	private String purchaseOrder;
	private String referenceNumber;
	private LocationWebModel location = new LocationWebModel(this);
	private BaseOrg owner;
	private Long assetTypeId;
	private Long assetTypeGroupId;
	private Long assetStatusId;
	private Long assignedUserId;
	private Long inspectionTypeGroupId;
	private Long inspectionBookId;
	private Long jobId;
	private Date fromDate;
	private Date toDate;
	
	private Long performedBy;
	
	private Status status;
	
	public InspectionSearchContainer(SecurityFilter filter, LoaderFactory loaderFactory) {
		super(Inspection.class, "id", filter, loaderFactory);
	}
	
	@Override
	protected void evalJoinTerms() {
		addLeftJoinTerms("book", "asset.shopOrder.order", "asset.identifiedBy", "owner.customerOrg", "owner.secondaryOrg", "owner.divisionOrg",  "asset.type.group", "assetStatus");
		addPredefinedLocationJoin();
	}
	
	@Override
	protected void evalSearchTerms() {
		addWildcardOrStringTerm("asset.rfidNumber", rfidNumber);
		addWildcardOrStringTerm("asset.serialNumber", serialNumber);
		addWildcardOrStringTerm("asset.shopOrder.order.orderNumber", orderNumber);
		addWildcardOrStringTerm("asset.purchaseOrder", purchaseOrder);
		addWildcardOrStringTerm("asset.customerRefNumber", referenceNumber);
		addWildcardOrStringTerm("advancedLocation.freeformLocation", location.getFreeformLocation());
		addSimpleTerm("asset.type.id", assetTypeId);
		addSimpleTerm("asset.type.group.id", assetTypeGroupId);
		addSimpleTerm("assetStatus.uniqueID", assetStatusId);
		addSimpleTerm("type.group.id", inspectionTypeGroupId);
		addSimpleTerm("performedBy.id", performedBy);
		addSimpleTerm("schedule.project.id", jobId);
		addSimpleTerm("status", status);
		addDateRangeTerm("date", fromDate, toDate);
		
		addInspectionBookTerm();
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

	private void addInspectionBookTerm() {
		// when inspectionBookId is 0, we search for inspections not in a book
		if(inspectionBookId != null && inspectionBookId == 0) {
			addNullTerm("book.id");
		} else {
			addSimpleTerm("book.id", inspectionBookId);
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
	protected String defaultSortColumn() {
		return "date";
	}
	
	@Override
	protected SortTerm.Direction defaultSortDirection() {
		return SortTerm.Direction.DESC;
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

	public Long getAssetType() {
		return assetTypeId;
	}

	public void setAssetType(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	
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

	public Long getInspectionTypeGroup() {
		return inspectionTypeGroupId;
	}

	public void setInspectionTypeGroup(Long inspectionTypeGroupId) {
		this.inspectionTypeGroupId = inspectionTypeGroupId;
	}

	public Long getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(Long performedBy) {
		this.performedBy = performedBy;
	}

	public Long getInspectionBook() {
		return inspectionBookId;
	}

	public void setInspectionBook(Long inspectionBookId) {
		this.inspectionBookId = inspectionBookId;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
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
	
}
