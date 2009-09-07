package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.search.SortTerm;

public class ProductSearchContainer extends SearchContainer {
	private static final long serialVersionUID = 1L;
	private static final String[] joinColumns = {"shopOrder.order", "productStatus", "identifiedBy"};
	
	private String rfidNumber;
	private String serialNumber;
	private String location;
	private String orderNumber;
	private String referenceNumber;
	private String purchaseOrder;
	private Long productTypeId;
	private Long productStatusId;
	private Long assignedUserId;
	private Long ownerId;
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
		addSimpleTerm("type.id", productTypeId);
		addSimpleTerm("productStatus.uniqueID", productStatusId);
		addSimpleTerm("assignedUser.uniqueID", assignedUserId);
		addSimpleTerm("owner.id", ownerId);
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
	
	public Long getAssingedUser() {
		return assignedUserId;
	}
	
	public void setAssingedUser(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	
	public Long getOwner() {
		return ownerId;
	}
	
	public void setOwner(Long ownerId) {
		this.ownerId = ownerId;
	}
}
