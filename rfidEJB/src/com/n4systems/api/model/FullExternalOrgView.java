package com.n4systems.api.model;

import com.n4systems.api.validation.validators.NotNullValidator;
import com.n4systems.exporting.beanutils.ExportField;

public class FullExternalOrgView extends ExternalModelView {
	private static final long serialVersionUID = 1L;
	
	@ExportField(title = "Type", order = 0, validators = {NotNullValidator.class})
	private String type;

	@ExportField(title="Name", order = 100, validators = {NotNullValidator.class})
	private String name;

	@ExportField(title="Code", order = 150)
	private String code;

	@ExportField(title="Organization", order = 200, validators = {NotNullValidator.class})
	private String parentOrg;
	
	@ExportField(title="Contact Name", order = 225)
	private String contactName;
	
	@ExportField(title="Contact Email", order = 275)
	private String contactEmail;
	
	@ExportField(title="Address", order = 300)
	private String streetAddress;
	
	@ExportField(title="City", order = 400)
	private String city;
	
	@ExportField(title="State", order = 500)
	private String state;
	
	@ExportField(title="Country", order = 600)
	private String country;
	
	@ExportField(title="Postal/Zip Code", order = 700)
	private String zip;
	
	@ExportField(title="Phone 1", order = 800)
	private String phone1;
	
	@ExportField(title="Phone 2", order = 900)
	private String phone2;
	
	@ExportField(title="Fax", order = 1000)
	private String fax1;
	
	public FullExternalOrgView() {}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isCustomer() {
		return type.equals("C");
	}

	public boolean isDivision() {
		return type.equals("D");
	}

	public void setTypeToCustomer() {
		this.type = "C";
	}
	
	public void setTypeToDivision() {
		this.type = "D";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentOrg() {
		return parentOrg;
	}

	public void setParentOrg(String parentOrg) {
		this.parentOrg = parentOrg;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

}
