package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.persistence.Transaction;

public class OrganizationAddressMapBuilder extends AbstractMapBuilder<AddressInfo> {

	public OrganizationAddressMapBuilder() {
		super(
			ReportField.ORGANIZATION_ADDRESS_STREET,
			ReportField.ORGANIZATION_ADDRESS_CITY,
			ReportField.ORGANIZATION_ADDRESS_STATE,
			ReportField.ORGANIZATION_ADDRESS_POSTAL,
			ReportField.ORGANIZATION_ADDRESS_PHONE1,
			ReportField.ORGANIZATION_ADDRESS_PHONE2,
			ReportField.ORGANIZATION_ADDRESS_FAX
		);
	}
	
	@Override
	protected void setAllFields(AddressInfo entity, Transaction transaction) {
		setField(ReportField.ORGANIZATION_ADDRESS_STREET, entity.getStreetAddress());
		setField(ReportField.ORGANIZATION_ADDRESS_CITY, entity.getCity());
		setField(ReportField.ORGANIZATION_ADDRESS_STATE, entity.getState());
		setField(ReportField.ORGANIZATION_ADDRESS_POSTAL, entity.getZip());
		setField(ReportField.ORGANIZATION_ADDRESS_PHONE1, entity.getPhone1());
		setField(ReportField.ORGANIZATION_ADDRESS_PHONE2, entity.getPhone2());
		setField(ReportField.ORGANIZATION_ADDRESS_FAX, entity.getFax1());
	}

}
