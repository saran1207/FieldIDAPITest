package com.n4systems.reporting.mapbuilders;


public enum ReportField {
	N4_LOGO_IMAGE					("n4LogoImage"),
	ORGANIZATION_LOGO_IMAGE			("logoImage"),
	PRIMARY_ORG_NAME				("manName"),
	PRIMARY_ORG_ADDRESS				("manAddress"),
	CUSTOMER_NAME					("endUserName"),
	CUSTOMER_CODE					("customerNumber"),
	CUSTOMER_CONTACT_NAME			("customerContactName"),
	CUSTOMER_CONTACT_EMAIL			("customerContactEmail"),
	CUSTOMER_ADDRESS_STREET			("customerAddress"),
	CUSTOMER_ADDRESS_CITY			("customerCity"),
	CUSTOMER_ADDRESS_STATE			("customerState"),
	CUSTOMER_ADDRESS_POSTAL			("customerPostalCode"),
	CUSTOMER_ADDRESS_PHONE1			("customerPhoneNumber"),
	CUSTOMER_ADDRESS_PHONE2			("customerPhoneNumber2"),
	CUSTOMER_ADDRESS_FAX			("customerFaxNumber"),
	DIVISION_NAME					("division"),
	DIVISION_CODE					("divisionID"),
	DIVISION_CONTACT_NAME			("divisionContactName"),
	DIVISION_CONTACT_EMAIL			("divisionContactEmail"),
	DIVISION_ADDRESS_STREET			("divisionAddress"),
	DIVISION_ADDRESS_CITY			("divisionCity"),
	DIVISION_ADDRESS_STATE			("divisionState"),
	DIVISION_ADDRESS_POSTAL			("divisionPostalCode"),
	DIVISION_ADDRESS_PHONE1			("divisionPhoneNumber"),
	DIVISION_ADDRESS_PHONE2			("divisionPhoneNumber2"),
	DIVISION_ADDRESS_FAX			("divisionFaxNumber"),
	CERTIFICATE_NAME				("organizationalPrintName"),
	ORGANIZATION_ADDRESS_STREET		("organizationalAddress"),
	ORGANIZATION_ADDRESS_CITY		("organizationalCity"),
	ORGANIZATION_ADDRESS_STATE		("organizationalState"),
	ORGANIZATION_ADDRESS_POSTAL		("organizationalPostalCode"),
	ORGANIZATION_ADDRESS_PHONE1		("organizationalPhoneNumber"),
	ORGANIZATION_ADDRESS_PHONE2		("organizationalPhoneNumber2"),
	ORGANIZATION_ADDRESS_FAX		("organizationalFaxNumber"),
	PRODUCT_TYPE_NAME				("productType"),
	PRODUCT_TYPE_NAME_LEGACY		("itemNumber"),
	PRODUCT_TYPE_WARNING			("productWarning"),
	PRODUCT_TYPE_CERT_TEXT			("certificateText"),
	PRODUCT_STATUS					("productStatus"),
	ORDER_NUMBER					("orderNumber"),
	LINE_ITEM_DESC					("customerPartDescription"),
	LINE_ITEM_PRODUCT_CODE			("productName"),
	INSPECTOR_IDENTIFIED_BY			("identifiedBy"),
	INSPECTOR_NAME					("inspectorName"),
	INSPECTOR_POSITION				("position"),
	INSPECTOR_INITIALS				("initials"),
	USER_SIGNATURE_IMAGE			("signatureImage"),
	PROOF_TEST_PEAK_LOAD			("peakLoad"),
	PROOF_TEST_DURATION				("testDuration"),
	PROOF_TEST_PEAK_LOAD_DURATION	("peakLoadDuration"),
	REPORT_TITLE					("reportTitle"),
	NEXT_DATE_STRING				("nextDate"),
	NEXT_DATE						("nextDate_date"),
	JOB								("job");
	private final String paramKey;
	
	ReportField(String paramKey) {
		this.paramKey = paramKey;
	}
	
	public String getParamKey() {
		return paramKey;
	}
}
