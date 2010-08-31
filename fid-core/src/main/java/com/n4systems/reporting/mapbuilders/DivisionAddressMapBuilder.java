package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.persistence.Transaction;

public class DivisionAddressMapBuilder extends AbstractMapBuilder<AddressInfo> {

	public DivisionAddressMapBuilder() {
		super(
			ReportField.DIVISION_ADDRESS_STREET,
			ReportField.DIVISION_ADDRESS_CITY,
			ReportField.DIVISION_ADDRESS_STATE,
			ReportField.DIVISION_ADDRESS_POSTAL,
			ReportField.DIVISION_ADDRESS_PHONE1,
			ReportField.DIVISION_ADDRESS_PHONE2,
			ReportField.DIVISION_ADDRESS_FAX
		);
	}
	
	@Override
	protected void setAllFields(AddressInfo entity, Transaction transaction) {
		setField(ReportField.DIVISION_ADDRESS_STREET,	entity.getStreetAddress());
		setField(ReportField.DIVISION_ADDRESS_CITY,		entity.getCity());
		setField(ReportField.DIVISION_ADDRESS_STATE,	entity.getState());
		setField(ReportField.DIVISION_ADDRESS_POSTAL,	entity.getZip());
		setField(ReportField.DIVISION_ADDRESS_PHONE1,	entity.getPhone1());
		setField(ReportField.DIVISION_ADDRESS_PHONE2,	entity.getPhone2());
		setField(ReportField.DIVISION_ADDRESS_FAX,		entity.getFax1());
	}

}
