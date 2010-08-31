package com.n4systems.fieldid.actions.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;

public class ProductView implements Serializable, HasOwner {
	private static final long serialVersionUID = 1L;

	private BaseOrg owner;
	private Long assignedUser;
	private Long productStatus;
	private Long productTypeId;
	private String purchaseOrder;
	private String nonIntegrationOrderNumber;
	private String comments;
	private Date identified;
	private List<ProductExtensionValueInput> productExtentionValues;
	private List<InfoOptionInput> productInfoOptions;
	
	public ProductView() {}

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

	public Long getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Long productStatus) {
		this.productStatus = productStatus;
	}

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
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

	public List<ProductExtensionValueInput> getProductExtentionValues() {
		return productExtentionValues;
	}

	public void setProductExtentionValues(List<ProductExtensionValueInput> productExtentionValues) {
		this.productExtentionValues = productExtentionValues;
	}

	public List<InfoOptionInput> getProductInfoOptions() {
		return productInfoOptions;
	}

	public void setProductInfoOptions(List<InfoOptionInput> productInfoOptions) {
		this.productInfoOptions = productInfoOptions;
	}

	public Tenant getTenant() {
		return null;
	}

	public void setTenant(Tenant tenant) {
		
	}
	
}
