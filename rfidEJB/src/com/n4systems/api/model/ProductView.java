package com.n4systems.api.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.api.validation.validators.DateValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.api.validation.validators.OwnerExistsValidator;
import com.n4systems.api.validation.validators.ProductStatusExistsValidator;
import com.n4systems.api.validation.validators.ProductViewAttributesValidator;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.exporting.beanutils.MapSerializationHandler;

@SuppressWarnings("serial")
public class ProductView extends ExternalModelView {

	@ExportField(title = "Serial Number", order = 100, validators = { NotNullValidator.class })
	private String serialNumber;

	@ExportField(title = "RFID Number", order = 200)
	private String rfidNumber;

	@ExportField(title = "Reference Number", order = 300)
	private String customerRefNumber;

	@ExportField(title = "Owner Name", order = 400, validators = { NotNullValidator.class, OwnerExistsValidator.class })
	private String owner;

	@ExportField(title = "Location", order = 600)
	private String location;

	@ExportField(title = "Product Status", order = 700, validators = { ProductStatusExistsValidator.class })
	private String status;

	@ExportField(title = "Purchase Order", order = 800)
	private String purchaseOrder;

	@ExportField(title = "Order Numberr", order = 900)
	private String shopOrder;

	@ExportField(title = "Comments", order = 1000)
	private String comments;

	@ExportField(title = "Identified", order = 1100, validators = { DateValidator.class })
	private Date identified;

	@ExportField(title = "A:", order = 1200, handler = MapSerializationHandler.class, validators = { NotNullValidator.class, ProductViewAttributesValidator.class })
	private Map<String, String> attributes = new LinkedHashMap<String, String>();

	public ProductView() {}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProductStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getShopOrder() {
		return shopOrder;
	}

	public void setShopOrder(String shopOrder) {
		this.shopOrder = shopOrder;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getIdentified() {
		return identified;
	}

	public void setIdentified(Date identified) {
		this.identified = identified;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getGlobalId() {
		return null;
	}

	@Override
	public void setGlobalId(String globalId) {

	}

}
