package com.n4systems.fieldid.viewhelpers;

import java.util.Date;

import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.model.Asset;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.persistence.search.SortTerm;

public class AssetSearchContainer extends SearchContainer {
	private static final long serialVersionUID = 1L;
	private String rfidNumber;
	private String serialNumber;
	private LocationWebModel location = new LocationWebModel(this);
	private String orderNumber;
	private String referenceNumber;
	private String purchaseOrder;
	private Long assetTypeId;
	private Long assetTypeGroupId;
	private Long assetStatusId;
	private Long assignedUserId;
	private BaseOrg owner;
	private Date fromDate;
	private Date toDate;
	
	public AssetSearchContainer(SecurityFilter filter, LoaderFactory loaderFactory) {
		super(Asset.class, "id", filter, loaderFactory);
	}

	@Override
	protected void evalJoinTerms() {
		addLeftJoinTerms("shopOrder.order", "assetStatus", "identifiedBy", "owner.customerOrg", "owner.secondaryOrg", "owner.divisionOrg", "type.group");
		addPredefinedLocationJoin();
	}
	
	@Override
	protected void evalSearchTerms() {
		addWildcardOrStringTerm("rfidNumber", rfidNumber);
		addWildcardOrStringTerm("serialNumber", serialNumber);
		addWildcardOrStringTerm("advancedLocation.freeformLocation", location.getFreeformLocation());
		addWildcardOrStringTerm("shopOrder.order.orderNumber", orderNumber);
		addWildcardOrStringTerm("customerRefNumber", referenceNumber);
		addWildcardOrStringTerm("purchaseOrder", purchaseOrder);
		addSimpleTerm("type.id", assetTypeId);
		addSimpleTerm("type.group.id", assetTypeGroupId);
		addSimpleTerm("assetStatus.id", assetStatusId);
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

	public Long getAssetType() {
		return assetTypeId;
	}

	public void setAssetType(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Long getAssetStatus() {
		return assetStatusId;
	}

	public void setAssetStatus(Long assetStatusId) {
		this.assetStatusId = assetStatusId;
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

	public Long getAssetTypeGroup() {
		return assetTypeGroupId;
	}

	public void setAssetTypeGroup(Long assetTypeGroupId) {
		this.assetTypeGroupId = assetTypeGroupId;
	}
}
