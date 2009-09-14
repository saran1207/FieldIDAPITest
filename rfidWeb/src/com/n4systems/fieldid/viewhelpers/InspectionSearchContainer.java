package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.model.Inspection;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.persistence.search.SortTerm;


public class InspectionSearchContainer extends SearchContainer implements ReportDefiner {
	private static final long serialVersionUID = 1L;
	private static final String[] joinColumns = {"book", "product.shopOrder.order", "product.productStatus", "product.identifiedBy"};
	
	private Long savedReportId;
	private boolean savedReportModified;
	private String rfidNumber;
	private String serialNumber;
	private String orderNumber;
	private String purchaseOrder;
	private String referenceNumber;
	private String location;
	private BaseOrg owner;
	private Long productTypeId;
	private Long productStatusId;
	private Long assignedUserId;
	private Long inspectionTypeGroupId;
	private Long inspectorId;
	private Long inspectionBookId;
	private Long jobId;
	private Date fromDate;
	private Date toDate;
	
	public InspectionSearchContainer(SecurityFilter filter) {
		super(Inspection.class, "id", filter, joinColumns);
	}
	
	@Override
	protected void evalSearchTerms() {
		addStringTerm("product.rfidNumber", rfidNumber);
		addWildcardTerm("product.serialNumber", serialNumber);
		addStringTerm("product.shopOrder.order.orderNumber", orderNumber);
		addStringTerm("product.purchaseOrder", purchaseOrder);
		addStringTerm("product.customerRefNumber", referenceNumber);
		addWildcardTerm("location", location);
		addSimpleTerm("product.type.id", productTypeId);
		addSimpleTerm("product.productStatus.uniqueID", productStatusId);
		addSimpleTerm("product.assignedUser.uniqueID", assignedUserId);
		addSimpleTerm("type.group.id", inspectionTypeGroupId);
		addSimpleTerm("inspector.uniqueID", inspectorId);
		addSimpleTerm("schedule.project.id", jobId);
		
		// when inspectionBookId is 0, we search for inspections not in a book
		if(inspectionBookId != null && inspectionBookId == 0) {
			addNullTerm("book.id");
		} else {
			addSimpleTerm("book.id", inspectionBookId);
		}
		
		addDateRangeTerm("date", fromDate, toDate);
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

	public Long getProductType() {
		return productTypeId;
	}

	public void setProductType(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	
	public Long getProductStatus() {
		return productStatusId;
	}
	
	public void setProductStatus(Long productStatusId) {
		this.productStatusId = productStatusId;
	}

	public Long getInspectionTypeGroup() {
		return inspectionTypeGroupId;
	}

	public void setInspectionTypeGroup(Long inspectionTypeGroupId) {
		this.inspectionTypeGroupId = inspectionTypeGroupId;
	}

	public Long getInspector() {
		return inspectorId;
	}

	public void setInspector(Long inspectorId) {
		this.inspectorId = inspectorId;
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
	
	public Long getAssingedUser() {
		return assignedUserId;
	}
	
	public void setAssingedUser(Long assignedUserId) {
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
	
}
