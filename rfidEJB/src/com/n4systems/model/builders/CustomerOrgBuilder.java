package com.n4systems.model.builders;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import static com.n4systems.model.builders.SecondaryOrgBuilder.*;



public class CustomerOrgBuilder extends BaseBuilder<CustomerOrg>{

	private String name;
	private String code;
	private InternalOrg parent;
	
	public static CustomerOrgBuilder aCustomerOrg() {
		return new CustomerOrgBuilder(null, "Some Customer", "some", null);
	}
	
	public static CustomerOrgBuilder aPrimaryCustomerOrg() {
		return new CustomerOrgBuilder(null, "some customer", "some", aPrimaryOrg().build());
	}
	
	public static CustomerOrgBuilder aSecondaryCustomerOrg() {
		return new CustomerOrgBuilder(null, "some customer", "some", aSecondaryOrg().build());
	}
	
	
	public CustomerOrgBuilder(Long id, String name, String code, InternalOrg parent) {
		super(id);
		this.name = name;
		this.code = code;
		this.parent = parent;
	}
	
	public CustomerOrgBuilder withName(String name) {
		return new CustomerOrgBuilder(id, name, code, parent);
	}
	
	public CustomerOrgBuilder withCode(String code) {
		return new CustomerOrgBuilder(id, name, code, parent);
	}
	
	public CustomerOrgBuilder withParent(InternalOrg parent) {
		return new CustomerOrgBuilder(id, name, code, parent);		
	}
	
	
	
	@Override
	public CustomerOrg build() {
		CustomerOrg customerOrg = new CustomerOrg();
		customerOrg.setId(id);
		customerOrg.setName(name);
		customerOrg.setCode(code);
		customerOrg.setTenant(parent.getTenant());
		customerOrg.setParent(parent);
		
		return customerOrg;
	}

}
