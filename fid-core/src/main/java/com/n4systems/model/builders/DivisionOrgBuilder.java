package com.n4systems.model.builders;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import static com.n4systems.model.builders.CustomerOrgBuilder.*;


public class DivisionOrgBuilder extends BaseBuilder<DivisionOrg> {

	private static final String DEFAULT_DIVISION_NAME = "A Division";
	private String name;
	private CustomerOrg customerOrg;
	
	public static DivisionOrgBuilder aDivisionOrg() {
		return new DivisionOrgBuilder(null, DEFAULT_DIVISION_NAME, null);
	}
	
	public static DivisionOrgBuilder aPrimaryDivisionOrg() {
		return new DivisionOrgBuilder(null, DEFAULT_DIVISION_NAME, aPrimaryCustomerOrg().build());
	}
	
	public static DivisionOrgBuilder aSecondaryDivisionOrg() {
		return new DivisionOrgBuilder(null, DEFAULT_DIVISION_NAME, aSecondaryCustomerOrg().build());
	}
	
	public DivisionOrgBuilder(Long id, String name, CustomerOrg customerOrg) {
		super(id);
		this.name = name;
		this.customerOrg = customerOrg;
	}
	
	public DivisionOrgBuilder withName(String name) {
		return new DivisionOrgBuilder(getId(), name, customerOrg);
	}
	
	public DivisionOrgBuilder withCustomerOrg(CustomerOrg customerOrg) {
		return new DivisionOrgBuilder(getId(), name, customerOrg);
	}
	
	@Override
	public DivisionOrg createObject() {
		DivisionOrg divisionOrg = new DivisionOrg();
		divisionOrg.setId(getId());
		divisionOrg.setName(name);
		divisionOrg.setParent(customerOrg);
		divisionOrg.setTenant(customerOrg.getTenant());
		return divisionOrg;
	}

}
