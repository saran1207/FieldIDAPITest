package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class PrimaryOrgMapBuilder extends AbstractMapBuilder<PrimaryOrg> {
	private final MapBuilder<AddressInfo> addressMapBuilder;
	
	public PrimaryOrgMapBuilder(MapBuilder<AddressInfo> addressMapBuilder) {
		super(ReportField.PRIMARY_ORG_NAME);
		
		this.addressMapBuilder = addressMapBuilder;
	}
	
	public PrimaryOrgMapBuilder() {
		this(new PrimaryOrgAddressMapBuilder());
	}
	
	@Override
	protected void setAllFields(PrimaryOrg entity, Transaction transaction) {
		setField(ReportField.PRIMARY_ORG_NAME, entity.getName());
		
		setAllFields(addressMapBuilder, entity.getAddressInfo(), transaction);
	}

}
