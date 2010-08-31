package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.persistence.Transaction;

public class CustomerAddressMapBuilder extends AbstractMapBuilder<AddressInfo> {

	public CustomerAddressMapBuilder() {
		super(
			ReportField.CUSTOMER_ADDRESS_STREET,
			ReportField.CUSTOMER_ADDRESS_CITY,
			ReportField.CUSTOMER_ADDRESS_STATE,
			ReportField.CUSTOMER_ADDRESS_POSTAL,
			ReportField.CUSTOMER_ADDRESS_PHONE1,
			ReportField.CUSTOMER_ADDRESS_PHONE2,
			ReportField.CUSTOMER_ADDRESS_FAX
		);
	}
	
	@Override
	protected void setAllFields(AddressInfo entity, Transaction transaction) {
		setField(ReportField.CUSTOMER_ADDRESS_STREET,	entity.getStreetAddress());
		setField(ReportField.CUSTOMER_ADDRESS_CITY,		entity.getCity());
		setField(ReportField.CUSTOMER_ADDRESS_STATE,	entity.getState());
		setField(ReportField.CUSTOMER_ADDRESS_POSTAL,	entity.getZip());
		setField(ReportField.CUSTOMER_ADDRESS_PHONE1,	entity.getPhone1());
		setField(ReportField.CUSTOMER_ADDRESS_PHONE2,	entity.getPhone2());
		setField(ReportField.CUSTOMER_ADDRESS_FAX,		entity.getFax1());
	}

}
