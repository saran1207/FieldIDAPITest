package com.n4systems.plugins.integration;


public class ShopOrderTransfer extends OrderTransfer {
	private static final long serialVersionUID = -6769840845159752381L;
	
	private long orderQuantity = 0L;
	private String assetCode;
	private String lineItemId;
	private String lineItemDescription;
	
	public long getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(long orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getLineItemDescription() {
		return lineItemDescription;
	}

	public void setLineItemDescription(String lineItemDescription) {
		this.lineItemDescription = lineItemDescription;
	}
}
