package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.persistence.Transaction;

public class DivisionMapBuilder extends AbstractMapBuilder<DivisionOrg> {
	private final MapBuilder<AddressInfo> addressMapBuilder;
	private final MapBuilder<Contact> contactMapBuilder;
	
	public DivisionMapBuilder(MapBuilder<AddressInfo> addressMapBuilder, MapBuilder<Contact> contactMapBuilder) {
		super(ReportField.DIVISION_NAME, ReportField.DIVISION_CODE);
		
		this.addressMapBuilder = addressMapBuilder;
		this.contactMapBuilder = contactMapBuilder;
	}
	
	public DivisionMapBuilder() {
		this(
			new DivisionAddressMapBuilder(),
			new DivisionContactMapBuilder()
		);
	}
	
	@Override
	protected void setAllFields(DivisionOrg entity, Transaction transaction) {
		setField(ReportField.DIVISION_NAME, entity.getName());
		setField(ReportField.DIVISION_CODE, entity.getCode());

		setAllFields(addressMapBuilder, entity.getAddressInfo(), transaction);
		setAllFields(contactMapBuilder, entity.getContact(), transaction);
	}

}
