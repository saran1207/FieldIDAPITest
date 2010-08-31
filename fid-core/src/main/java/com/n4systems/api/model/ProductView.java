package com.n4systems.api.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.api.validation.validators.DateValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.api.validation.validators.OwnerExistsValidator;
import com.n4systems.api.validation.validators.ProductStatusExistsValidator;
import com.n4systems.api.validation.validators.ProductViewAttributesValidator;
import com.n4systems.api.validation.validators.ProductViewToProductRfidLengthValidator;
import com.n4systems.api.validation.validators.ProductViewToProductSerialLengthValidator;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.exporting.beanutils.MapSerializationHandler;
import com.n4systems.exporting.beanutils.OwnerSerializationHandler;

@SuppressWarnings("serial")
public class ProductView extends ExternalModelView {
	
	@ExportField(title = "Serial Number", order = 100, validators = { NotNullValidator.class, ProductViewToProductSerialLengthValidator.class })
	private String serialNumber;

	@ExportField(title = "RFID Number", order = 200, validators = {ProductViewToProductRfidLengthValidator.class})
	private String rfidNumber;

	@ExportField(title = "Reference Number", order = 300)
	private String customerRefNumber;

	@ExportField(title = "", order = 400, handler = OwnerSerializationHandler.class, validators = { NotNullValidator.class, OwnerExistsValidator.class })
	private final String[] owners = new String[3];

	@ExportField(title = "Location", order = 600)
	private String location;

	@ExportField(title = "Product Status", order = 700, validators = { ProductStatusExistsValidator.class })
	private String status;

	@ExportField(title = "Purchase Order", order = 800)
	private String purchaseOrder;

	@ExportField(title = "Order Number", order = 900)
	private String shopOrder;

	@ExportField(title = "Comments", order = 1000)
	private String comments;

	// this field is an object since it is hard to know exactly what is going to come back from excel
	// if the date is formatted incorrectly, excel will return a String.  We let the date validator ensure we 
	// actually have a date here.
	@ExportField(title = "Identified", order = 1100, validators = { DateValidator.class })
	private Object identified;

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

	public String[] getOwners() {
		return owners;
	}
	
	public void setOrganization(String organization) {
		owners[OwnerSerializationHandler.OWNER_ORGANIZATION] = organization;
	}
	
	public String getOrganization() {
		return owners[OwnerSerializationHandler.OWNER_ORGANIZATION];
	}
	
	public void setCustomer(String customer) {
		owners[OwnerSerializationHandler.OWNER_CUSTOMER] = customer;
	}
	
	public String getCustomer() {
		return owners[OwnerSerializationHandler.OWNER_CUSTOMER];
	}
	
	public void setDivision(String division) {
		owners[OwnerSerializationHandler.OWNER_DIVISION] = division;
	}
	
	public String getDivision() {
		return owners[OwnerSerializationHandler.OWNER_DIVISION];
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
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

	public Object getIdentified() {
		return identified;
	}

	public void setIdentified(Object identified) {
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
