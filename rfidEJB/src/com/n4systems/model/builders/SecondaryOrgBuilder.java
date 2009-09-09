package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;

public class SecondaryOrgBuilder extends BaseBuilder<SecondaryOrg> {

	private String name;
	private Tenant tenant;
	private PrimaryOrg primaryOrg;
	
	public static SecondaryOrgBuilder aSecondaryOrg() {
		return new SecondaryOrgBuilder(null, "Some Org", null, PrimaryOrgBuilder.aPrimaryOrg().build());
	}
	
	public SecondaryOrgBuilder(Long id, String name, Tenant tenant, PrimaryOrg primaryOrg) {
		super(id);
		this.name = name;
		this.tenant = tenant;
		this.primaryOrg = primaryOrg;
	}
	
	public SecondaryOrgBuilder withName(String name) {
		return new SecondaryOrgBuilder(id, name, tenant, primaryOrg);
	}
	
	public SecondaryOrgBuilder onTenant(Tenant tenant) {
		return new SecondaryOrgBuilder(id, name, tenant, primaryOrg);		
	}
	
	public SecondaryOrgBuilder withPrimaryOrg(PrimaryOrg primaryOrg) {
		return new SecondaryOrgBuilder(id, name, tenant, primaryOrg);				
	}
	
	@Override
	public SecondaryOrg build() {
		SecondaryOrg secondaryOrg = new SecondaryOrg();
		secondaryOrg.setName(name);
		secondaryOrg.setTenant(tenant);
		secondaryOrg.setPrimaryOrg(primaryOrg);
		secondaryOrg.setId(id);
		
		return secondaryOrg;
	}

}
