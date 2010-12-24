package com.n4systems.fieldid.actions.asset;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;

public class AssetView implements Serializable, HasOwner {
	private static final long serialVersionUID = 1L;

	private BaseOrg owner;
	private Long assignedUser;
	private Long assetStatus;
	private Long assetTypeId;
	private String purchaseOrder;
	private String nonIntegrationOrderNumber;
	private String comments;
	private Date identified;
	private List<InfoOptionInput> assetInfoOptions;
	private Long lineItemId;
	
	public AssetView() {}

	public BaseOrg getOwner() {
		return owner;
	}

	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	public Long getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(Long assignedUser) {
		this.assignedUser = assignedUser;
	}

	public Long getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(Long assetStatus) {
		this.assetStatus = assetStatus;
	}

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}


	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Date getIdentified() {
		return identified;
	}

	public void setIdentified(Date identified) {
		this.identified = identified;
	}

	public String getNonIntegrationOrderNumber() {
		return nonIntegrationOrderNumber;
	}

	public void setNonIntegrationOrderNumber(String nonIntegrationOrderNumber) {
		this.nonIntegrationOrderNumber = nonIntegrationOrderNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<InfoOptionInput> getAssetInfoOptions() {
		return assetInfoOptions;
	}

	public void setAssetInfoOptions(List<InfoOptionInput> assetInfoOptions) {
		this.assetInfoOptions = assetInfoOptions;
	}

	public Tenant getTenant() {
		return null;
	}

	public void setTenant(Tenant tenant) {
		
	}

	public Long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(Long lineItemId) {
		this.lineItemId = lineItemId;
	}
	
}
