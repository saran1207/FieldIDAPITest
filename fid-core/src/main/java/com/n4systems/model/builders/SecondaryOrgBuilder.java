package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;


public class SecondaryOrgBuilder extends BaseBuilder<SecondaryOrg> {

	private String name;
	private PrimaryOrg primaryOrg;
	
	public static SecondaryOrgBuilder aSecondaryOrg() {
		return new SecondaryOrgBuilder(null, "Some Org", aPrimaryOrg().build());
	}
	
	public SecondaryOrgBuilder(Long id, String name, PrimaryOrg primaryOrg) {
		super(id);
		this.name = name;
		this.primaryOrg = primaryOrg;
	}
	
	public SecondaryOrgBuilder withName(String name) {
		return new SecondaryOrgBuilder(id, name, primaryOrg);
	}
	
	public SecondaryOrgBuilder onTenant(Tenant tenant) {
		return new SecondaryOrgBuilder(id, name, primaryOrg);		
	}
	
	public SecondaryOrgBuilder withPrimaryOrg(PrimaryOrg primaryOrg) {
		return new SecondaryOrgBuilder(id, name, primaryOrg);				
	}
	
	@Override
	public SecondaryOrg build() {
		SecondaryOrg secondaryOrg = new SecondaryOrg();
		secondaryOrg.setName(name);
		secondaryOrg.setTenant(primaryOrg.getTenant());
		secondaryOrg.setPrimaryOrg(primaryOrg);
		secondaryOrg.setId(id);
		
		return secondaryOrg;
	}

}
