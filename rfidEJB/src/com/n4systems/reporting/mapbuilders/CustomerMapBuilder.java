package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.Transaction;

public class CustomerMapBuilder extends AbstractMapBuilder<CustomerOrg> {
	private final MapBuilder<AddressInfo> addressMapBuilder;
	private final MapBuilder<Contact> contactMapBuilder;
	
	public CustomerMapBuilder(MapBuilder<AddressInfo> addressMapBuilder, MapBuilder<Contact> contactMapBuilder) {
		super(ReportField.CUSTOMER_NAME, ReportField.CUSTOMER_CODE);
		this.addressMapBuilder = addressMapBuilder;
		this.contactMapBuilder = contactMapBuilder;
	}
	
	public CustomerMapBuilder() {
		this(
			new CustomerAddressMapBuilder(),
			new CustomerContactMapBuilder()
		);
	}
	
	@Override
	protected void setAllFields(CustomerOrg entity, Transaction transaction) {
		setField(ReportField.CUSTOMER_NAME, entity.getName());
		setField(ReportField.CUSTOMER_CODE, entity.getCode());

		setAllFields(addressMapBuilder, entity.getAddressInfo(), transaction);
		setAllFields(contactMapBuilder, entity.getContact(), transaction);
	}
}
