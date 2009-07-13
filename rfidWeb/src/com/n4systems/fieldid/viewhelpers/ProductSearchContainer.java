package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.model.Product;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.search.SortTerm;

public class ProductSearchContainer extends SearchContainer {
	private static final long serialVersionUID = 1L;
	private static final String[] joinColumns = {"shopOrder.order", "productStatus", "identifiedBy", "owner", "division", "organization"};
	
	private String rfidNumber;
	private String serialNumber;
	private String location;
	private String orderNumber;
	private String referenceNumber;
	private String purchaseOrder;
	private Long customerId;
	private Long divisionId;
	private Long productTypeId;
	private Long productStatusId;
	private Long assignedUserId;
	private Long jobSiteId;
	private Date fromDate;
	private Date toDate;
	
	public ProductSearchContainer(SecurityFilter filter) {
		super(Product.class, "id", filter, joinColumns);
	}

	@Override
	protected void evalSearchTerms() {
		addStringTerm("rfidNumber", rfidNumber);
		addWildcardTerm("serialNumber", serialNumber);
		addWildcardTerm("location", location);
		addStringTerm("shopOrder.order.orderNumber", orderNumber);
		addStringTerm("customerRefNumber", referenceNumber);
		addStringTerm("purchaseOrder", purchaseOrder);
		addSimpleTerm("owner.id", customerId);
		addSimpleTerm("division.id", divisionId);
		addSimpleTerm("type.id", productTypeId);
		addSimpleTerm("productStatus.uniqueID", productStatusId);
		addSimpleTerm("assignedUser.uniqueID", assignedUserId);
		addSimpleTerm("jobSite.id", jobSiteId);
		addDateRangeTerm("identified", fromDate, toDate);
	}
	
	@Override
	protected String defaultSortColumn() {
		return "identified";
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
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getCustomer() {
		return customerId;
	}

	public void setCustomer(Long customerId) {
		this.customerId = customerId;
	}

	public Long getDivision() {
		return divisionId;
	}

	public void setDivision(Long divisionId) {
		this.divisionId = divisionId;
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

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
	public Long getJobSite() {
		return jobSiteId;
	}
	
	public void setJobSite(Long jobSiteId) {
		this.jobSiteId = jobSiteId;
	}
	
	public Long getAssingedUser() {
		return assignedUserId;
	}
	
	public void setAssingedUser(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
}
