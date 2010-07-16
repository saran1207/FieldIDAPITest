package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.fieldid.actions.product.LocationWebModel;
import com.n4systems.model.Product;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.persistence.search.SortTerm;

public class ProductSearchContainer extends SearchContainer {
	private static final long serialVersionUID = 1L;
	private String rfidNumber;
	private String serialNumber;
	private LocationWebModel location = new LocationWebModel(this);
	private String orderNumber;
	private String referenceNumber;
	private String purchaseOrder;
	private Long productTypeId;
	private Long productTypeGroupId;
	private Long productStatusId;
	private Long assignedUserId;
	private BaseOrg owner;
	private Date fromDate;
	private Date toDate;
	
	public ProductSearchContainer(SecurityFilter filter, LoaderFactory loaderFactory) {
		super(Product.class, "id", filter, loaderFactory);
	}

	@Override
	protected void evalJoinTerms() {
		addLeftJoinTerms("shopOrder.order", "productStatus", "identifiedBy", "owner.customerOrg", "owner.secondaryOrg", "owner.divisionOrg", "type.group");
		addPredefinedLocationJoin();
	}
	
	@Override
	protected void evalSearchTerms() {
		addStringTerm("rfidNumber", rfidNumber);
		addWildcardTerm("serialNumber", serialNumber);
		addWildcardTerm("advancedLocation.freeformLocation", location.getFreeformLocation());
		addStringTerm("shopOrder.order.orderNumber", orderNumber);
		addStringTerm("customerRefNumber", referenceNumber);
		addStringTerm("purchaseOrder", purchaseOrder);
		addSimpleTerm("type.id", productTypeId);
		addSimpleTerm("type.group.id", productTypeGroupId);
		addSimpleTerm("productStatus.uniqueID", productStatusId);
		addDateRangeTerm("identified", fromDate, toDate);
		
		addPredefinedLocationTerm();
		addAssignedUserTerm();
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

	private void addAssignedUserTerm() {
		if(assignedUserId != null && assignedUserId == 0) {
			addNullTerm("assignedUser.id");
		} else {
			addSimpleTerm("assignedUser.id", assignedUserId);
		}
	}

	@Override
	protected void evalSearchFilters() {
		addOwnerFilter(getOwner());
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
	
	public LocationWebModel getLocation() {
		return location;
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
	
	public Long getAssignedUser() {
		return assignedUserId;
	}
	
	public void setAssignedUser(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public Long getOwnerId() {
		return (owner != null) ? owner.getId() : null;
	}
	
	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public Long getProductTypeGroup() {
		return productTypeGroupId;
	}

	public void setProductTypeGroup(Long productTypeGroupId) {
		this.productTypeGroupId = productTypeGroupId;
	}
}
