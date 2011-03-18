package com.n4systems.model.builders;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.ExternalOrg;
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
		BaseOrg parent = aPrimaryOrg().build();
		return new OrgBuilder(OrgType.SECONDARY, parent.getTenant(), "first_secondary", null, null, null, parent);
	}
	
	public static OrgBuilder aCustomerOrg() {
		BaseOrg parent = aPrimaryOrg().build();
		return new OrgBuilder(OrgType.CUSTOMER, parent.getTenant(), "first_customer", null, "fcust", null, parent);
	}
	
	public static OrgBuilder aDivisionOrg() {
		BaseOrg parent = aCustomerOrg().build();
		return new OrgBuilder(OrgType.DIVISION, parent.getTenant(), "first_division", null, "fdiv", null, parent);
	}
	
	public OrgBuilder(OrgType type, Tenant tenant, String name, AddressInfo addressInfo, String code, Contact contact, BaseOrg parent) {
		this.type = type;
		this.tenant = tenant;
		this.name = name;
		this.addressInfo = addressInfo;
		this.code = code;
		this.contact = contact;
		this.parent = parent;
	}
	
	public OrgBuilder withParent(BaseOrg parent) {
		return makeBuilder(new OrgBuilder(type, tenant, name, addressInfo, code, contact, parent));
	}
	
	public OrgBuilder withName(String name) {
		return makeBuilder(new OrgBuilder(type, tenant, name, addressInfo, code, contact, parent));
	}
	
	public OrgBuilder withTestAddress() {
		return makeBuilder(new OrgBuilder(type, tenant, name, AddressInfoBuilder.anAddressWithTestData().build(), code, contact, parent));
	}
	
	public OrgBuilder withTestContact() {
		return makeBuilder(new OrgBuilder(type, tenant, name, addressInfo, code, ContactBuilder.aContact().build(), parent));
	}

    public OrgBuilder tenant(Tenant tenant) {
        return makeBuilder(new OrgBuilder(type, tenant, name, addressInfo, code, ContactBuilder.aContact().build(), parent));
    }

	public OrgBuilder withAllTestData() {
		return withTestContact().withTestAddress();
	}
	
	@Override
	public BaseOrg createObject() {
		BaseOrg org = null;
		
		switch (type) {
			case PRIMARY:
				org = buildPrimary();
				break;
			case SECONDARY:
				org = buildSecondary();
				break;
			case CUSTOMER:
				org = buildCustomer();
				break;
			case DIVISION:
				org = buildDivision();
				break;
		}
		
		return org;
	}
	
	private void build(BaseOrg org) {
		org.setId(getId());
		org.setName(name);
		org.setAddressInfo(addressInfo);
	}
	
	public PrimaryOrg buildPrimary() {
		PrimaryOrg org = new PrimaryOrg();
		build(org);
		org.setTenant(tenant);
		return org;
	}
	
	public SecondaryOrg buildSecondary() {
		SecondaryOrg org = new SecondaryOrg();
		build(org);
		org.setPrimaryOrg((PrimaryOrg)parent);
		org.setTenant(parent.getTenant());
		org.setDefaultTimeZone("United States:New York - New York");
		return org;
	}
	
	public CustomerOrg buildCustomer() {
		CustomerOrg org = new CustomerOrg();
		build(org);
		org.setCode(code);
		org.setContact(contact);
		org.setParent((InternalOrg)parent);
		org.setTenant(parent.getTenant());
		return org;
	}
	
	public DivisionOrg buildDivision() {
		DivisionOrg org = new DivisionOrg();
		build(org);
		org.setCode(code);
		org.setContact(contact);
		org.setParent((CustomerOrg)parent);
		org.setTenant(parent.getTenant());
		return org;
	}
	
	public ExternalOrg buildCustomerAsExternal() {
		return buildCustomer();
	}
	
	public ExternalOrg buildDivisionAsExternal() {
		return buildDivision();
	}

	public OrgBuilder setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
		return this;
	}
	
}
