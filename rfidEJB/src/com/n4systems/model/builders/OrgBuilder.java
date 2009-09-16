package com.n4systems.model.builders;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;

public class OrgBuilder extends BaseBuilder<BaseOrg> {
	private enum OrgType { PRIMARY, SECONDARY, CUSTOMER, DIVISION }
	
	private String name;
	private Tenant tenant;
	private OrgType type;
	private AddressInfo addressInfo;
	private String code;
	private Contact contact;
	private BaseOrg parent;
	
	public static OrgBuilder aPrimaryOrg() {
		return new OrgBuilder(OrgType.PRIMARY, TenantBuilder.aTenant().build(), "first_primary", null, null, null, null);
	}
	
	public static OrgBuilder aSecondaryOrg() {
		return new OrgBuilder(OrgType.SECONDARY, null, "first_secondary", null, null, null, aPrimaryOrg().build());
	}
	
	public static OrgBuilder aCustomerOrg() {
		return new OrgBuilder(OrgType.CUSTOMER, null, "first_customer", null, "fcust", null, aPrimaryOrg().build());
	}
	
	public static OrgBuilder aDivisionOrg() {
		return new OrgBuilder(OrgType.DIVISION, null, "first_division", null, "fdiv", null, aCustomerOrg().build());
	}
	

	public OrgBuilder(OrgType type, Tenant tenant, String name, AddressInfo addressInfo, String code, Contact contact, BaseOrg parent) {
		super();
		this.type = type;
		this.tenant = tenant;
		this.name = name;
		this.addressInfo = addressInfo;
		this.code = code;
		this.contact = contact;
		this.parent = parent;
	}
	
	

	public OrgBuilder withParent(BaseOrg parent) {
		return new OrgBuilder(type, tenant, name, addressInfo, code, contact, parent);
	}
	
	public OrgBuilder withName(String name) {
		return new OrgBuilder(type, tenant, name, addressInfo, code, contact, parent);
	}

	
	
	@Override
	public BaseOrg build() {
		BaseOrg org = null;
		
		switch (type) {
			case PRIMARY:
				org = new PrimaryOrg();
				org.setTenant(tenant);
				break;
			case SECONDARY:
				org = new SecondaryOrg();
				((SecondaryOrg)org).setPrimaryOrg((PrimaryOrg)parent);
				org.setTenant(parent.getTenant());
				break;
			case CUSTOMER:
				org = new CustomerOrg();
				((CustomerOrg)org).setCode(code);
				((CustomerOrg)org).setContact(contact);
				((CustomerOrg)org).setParent((InternalOrg)parent);
				org.setTenant(parent.getTenant());
				break;
			case DIVISION:
				org = new DivisionOrg();
				((DivisionOrg)org).setCode(code);
				((DivisionOrg)org).setContact(contact);
				((DivisionOrg)org).setParent((CustomerOrg)parent);
				org.setTenant(parent.getTenant());
				break;
		}
		
		org.setId(id);
		org.setName(name);
		org.setAddressInfo(addressInfo);
		
		return org;
	}
}
