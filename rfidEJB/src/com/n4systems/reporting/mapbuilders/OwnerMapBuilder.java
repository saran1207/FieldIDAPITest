package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public class OwnerMapBuilder extends AbstractMapBuilder<BaseOrg> {
	private final MapBuilder<PrimaryOrg> primaryOrgBuilder;
	private final MapBuilder<CustomerOrg> customerOrgBuilder;
	private final MapBuilder<DivisionOrg> divisionOrgBuilder;
	
	public OwnerMapBuilder(MapBuilder<PrimaryOrg> primaryOrgBuilder, MapBuilder<CustomerOrg> customerOrgBuilder, MapBuilder<DivisionOrg> divisionOrgBuilder) {
		this.primaryOrgBuilder = primaryOrgBuilder;
		this.customerOrgBuilder = customerOrgBuilder;
		this.divisionOrgBuilder = divisionOrgBuilder;
	}
	
	public OwnerMapBuilder() {
		this(
			new PrimaryOrgMapBuilder(),
			new CustomerMapBuilder(),
			new DivisionMapBuilder()
		);
	}
	
	@Override
	protected void setAllFields(BaseOrg entity, Transaction transaction) {
		setAllFields(primaryOrgBuilder, entity.getPrimaryOrg(), transaction);
		setAllFields(customerOrgBuilder, entity.getCustomerOrg(), transaction);
		setAllFields(divisionOrgBuilder, entity.getDivisionOrg(), transaction);
	}

}
