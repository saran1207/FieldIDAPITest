package com.n4systems.model.builders;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;

public class DivisionOrgBuilder extends BaseBuilder<DivisionOrg> {

	private String name;
	private CustomerOrg customerOrg;
	
	public static DivisionOrgBuilder aDivisionOrg() {
		return new DivisionOrgBuilder(null, "A Division", null);
	}
	
	public DivisionOrgBuilder(Long id, String name, CustomerOrg customerOrg) {
		super(id);
		this.name = name;
		this.customerOrg = customerOrg;
	}
	
	public DivisionOrgBuilder withName(String name) {
		return new DivisionOrgBuilder(id, name, customerOrg);
	}
	
	public DivisionOrgBuilder withCustomerOrg(CustomerOrg customerOrg) {
		return new DivisionOrgBuilder(id, name, customerOrg);
	}
	
	@Override
	public DivisionOrg build() {
		DivisionOrg divisionOrg = new DivisionOrg();
		divisionOrg.setId(id);
		divisionOrg.setName(name);
		divisionOrg.setParent(customerOrg);
		return divisionOrg;
	}
	

}
