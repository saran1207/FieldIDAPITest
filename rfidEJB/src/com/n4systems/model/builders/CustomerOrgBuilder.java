package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;

public class CustomerOrgBuilder extends BaseBuilder<CustomerOrg>{

	private String name;
	private String code;
	private Tenant tenant;
	private InternalOrg parent;
	
	public static CustomerOrgBuilder aCustomerOrg() {
		return new CustomerOrgBuilder(null, "Some Customer", "some", null, null);
	}
	
	public CustomerOrgBuilder(Long id, String name, String code, Tenant tenant, InternalOrg parent) {
		super(id);
		this.name = name;
		this.code = code;
		this.tenant = tenant;
		this.parent = parent;
	}
	
	public CustomerOrgBuilder withName(String name) {
		return new CustomerOrgBuilder(id, name, code, tenant, parent);
	}
	
	public CustomerOrgBuilder withCode(String code) {
		return new CustomerOrgBuilder(id, name, code, tenant, parent);
	}
	
	public CustomerOrgBuilder withParent(InternalOrg parent) {
		return new CustomerOrgBuilder(id, name, code, tenant, parent);		
	}
	
	public CustomerOrgBuilder onTenant(Tenant tenant) {
		return new CustomerOrgBuilder(id, name, code, tenant, parent);				
	}
	
	@Override
	public CustomerOrg build() {
		CustomerOrg customerOrg = new CustomerOrg();
		customerOrg.setId(id);
		customerOrg.setName(name);
		customerOrg.setCode(code);
		customerOrg.setTenant(tenant);
		customerOrg.setParent(parent);
		
		return customerOrg;
	}

}
