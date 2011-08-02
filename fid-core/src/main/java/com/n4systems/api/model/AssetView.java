package com.n4systems.api.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.api.validation.validators.AssetViewAttributesValidator;
import com.n4systems.api.validation.validators.AssetViewToAssetIdentifierLengthValidator;
import com.n4systems.api.validation.validators.DateValidator;
import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.api.validation.validators.OwnerExistsValidator;
import com.n4systems.api.validation.validators.AssetStatusExistsValidator;
import com.n4systems.api.validation.validators.AssetViewToAssetRfidLengthValidator;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.exporting.beanutils.MapSerializationHandler;
import com.n4systems.exporting.beanutils.OwnerSerializationHandler;

@SuppressWarnings("serial")
public class AssetView extends ExternalModelView {
	
	@SerializableField(title = "ID Number", order = 100, validators = { NotNullValidator.class, AssetViewToAssetIdentifierLengthValidator.class })
	private String identifier;

	@SerializableField(title = "RFID Number", order = 200, validators = {AssetViewToAssetRfidLengthValidator.class})
	private String rfidNumber;

	@SerializableField(title = "Reference Number", order = 300)
	private String customerRefNumber;

	@SerializableField(title = "", order = 400, handler = OwnerSerializationHandler.class, validators = { NotNullValidator.class, OwnerExistsValidator.class })
	private final String[] owners = new String[3];

	@SerializableField(title = "Location", order = 600)
	private String location;

	@SerializableField(title = "Asset Status", order = 700, validators = { AssetStatusExistsValidator.class })
	private String status;

	@SerializableField(title = "Purchase Order", order = 800)
	private String purchaseOrder;

	@SerializableField(title = "Order Number", order = 900)
	private String shopOrder;

	@SerializableField(title = "Comments", order = 1000)
	private String comments;

	// this field is an object since it is hard to know exactly what is going to come back from excel
	// if the date is formatted incorrectly, excel will return a String.  We let the date validator ensure we 
	// actually have a date here.
	@SerializableField(title = "Identified", order = 1100, validators = { DateValidator.class })
	private Object identified;

	@SerializableField(title = "A:", order = 1200, handler = MapSerializationHandler.class, validators = { NotNullValidator.class, AssetViewAttributesValidator.class })
	private Map<String, String> attributes = new LinkedHashMap<String, String>();

	public AssetView() {}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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
		owners[OwnerSerializationHandler.ORGANIZATION_INDEX] = organization;
	}
	
	public String getOrganization() {
		return owners[OwnerSerializationHandler.ORGANIZATION_INDEX];
	}
	
	public void setCustomer(String customer) {
		owners[OwnerSerializationHandler.CUSTOMER_ID] = customer;
	}
	
	public String getCustomer() {
		return owners[OwnerSerializationHandler.CUSTOMER_ID];
	}
	
	public void setDivision(String division) {
		owners[OwnerSerializationHandler.DIVISION_INDEX] = division;
	}
	
	public String getDivision() {
		return owners[OwnerSerializationHandler.DIVISION_INDEX];
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
