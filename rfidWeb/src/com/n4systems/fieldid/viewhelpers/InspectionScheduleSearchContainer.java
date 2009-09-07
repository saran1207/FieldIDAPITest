package com.n4systems.fieldid.viewhelpers;


import java.util.Date;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.util.persistence.search.SortTerm;

public class InspectionScheduleSearchContainer extends SearchContainer {
	private static final long serialVersionUID = 1L;
	private static final String[] joinColumns = {"product.shopOrder.order", "product.productStatus", "product.identifiedBy"};
	
	private String rfidNumber;
	private String serialNumber;
	private String orderNumber;
	private String purchaseOrder;
	private String location;
	private String referenceNumber;
	private Long productStatusId;
	private Long productTypeId;
	private Long assignedUserId;
	private Long inspectionTypeId;
	private Long jobId;
	private Long jobAndNullId;
	private Long ownerId;
	private Date toDate;
	private Date fromDate;
	private CompressedScheduleStatus status = CompressedScheduleStatus.INCOMPLETE;
	
	public InspectionScheduleSearchContainer(SecurityFilter securityFilter) {
		super(InspectionSchedule.class, "id", securityFilter, joinColumns);
		
		if (securityFilter.hasOwner()) {
			setOwner(securityFilter.getOwner().getId());
		}
	}

	@Override
	protected void evalSearchTerms() {
		addStringTerm("product.rfidNumber", rfidNumber);
		addWildcardTerm("product.serialNumber", serialNumber);
		addWildcardTerm("product.shopOrder.order.orderNumber", orderNumber);
		addWildcardTerm("location", location);
		addStringTerm("product.purchaseOrder", purchaseOrder);
		addStringTerm("product.customerRefNumber", referenceNumber);
		addSimpleTerm("product.productStatus.uniqueID", productStatusId);
		addSimpleTerm("product.type.id", productTypeId);
		addSimpleTerm("owner.id", ownerId);
		addSimpleTerm("product.assignedUser.uniqueID", assignedUserId);
		addSimpleTerm("inspectionType.group.id", inspectionTypeId);
		addSimpleTerm("project.id", jobId);
		addSimpleTermOrNull("project.id", jobAndNullId);
		addDateRangeTerm("nextDate", fromDate, toDate);
		addSimpleInTerm("status", status.getScheduleStatuses());
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
	
	public Long getProductStatus() {
		return productStatusId;
	}

	public void setProductStatus(Long productStatus) {
		this.productStatusId = productStatus;
	}
	
	public Long getOwner() {
		return ownerId;
	}
	
	public void setOwner(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getProductType() {
		return productTypeId;
	}

	public void setProductType(Long productType) {
		this.productTypeId = productType;
	}

	public Long getInspectionType() {
		return inspectionTypeId;
	}
	
	public void setInspectionType(Long inspectionType) {
		this.inspectionTypeId = inspectionType;
	}
	
	public Long getAssingedUser() {
		return assignedUserId;
	}
	
	public void setAssingedUser(Long assignedUser) {
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
