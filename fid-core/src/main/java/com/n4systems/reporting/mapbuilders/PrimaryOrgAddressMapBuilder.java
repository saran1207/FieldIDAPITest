package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.persistence.Transaction;

public class PrimaryOrgAddressMapBuilder extends AbstractMapBuilder<AddressInfo> {

	public PrimaryOrgAddressMapBuilder() {
		super(ReportField.PRIMARY_ORG_ADDRESS);
	}
	
	@Override
	protected void setAllFields(AddressInfo entity, Transaction transaction) {
		setField(ReportField.PRIMARY_ORG_ADDRESS, entity.getDisplay());
	}

}
