package com.n4systems.api;

import java.util.UUID;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.builders.BaseBuilder;

public class FullExternalOrgViewBuilder extends BaseBuilder<FullExternalOrgView> {
	private boolean isCustomer;
	private boolean withTestData;
	private boolean forEdit;
	
	public static FullExternalOrgViewBuilder aCustomerView() {
		return new FullExternalOrgViewBuilder(true, false, false);
	}

	public static FullExternalOrgViewBuilder aDivisionView() {
		return new FullExternalOrgViewBuilder(false, false, false);
	}
	
	protected FullExternalOrgViewBuilder(boolean isCustomer, boolean withTestData, boolean forEdit) {
		this.isCustomer = isCustomer;
		this.withTestData = withTestData;
		this.forEdit = forEdit;
	}
	
	public FullExternalOrgViewBuilder withTestData() {
		return new FullExternalOrgViewBuilder(isCustomer, true, forEdit);
	}
	
	public FullExternalOrgViewBuilder withoutTestData() {
		return new FullExternalOrgViewBuilder(isCustomer, false, forEdit);
	}
	
	public FullExternalOrgViewBuilder forEdit() {
		return new FullExternalOrgViewBuilder(isCustomer, withTestData, true);
	}
	
	public FullExternalOrgViewBuilder forAdd() {
		return new FullExternalOrgViewBuilder(isCustomer, withTestData, false);
	}
	
	@Override
	public FullExternalOrgView createObject() {
		FullExternalOrgView view = (isCustomer) ? FullExternalOrgView.newCustomer() : FullExternalOrgView.newDivision();
		
		if (withTestData) {
			setupTestData(view);
		}
		
		if (forEdit) {
			view.setGlobalId(UUID.randomUUID().toString());
		}
		
		return view;
	}
	
	private void setupTestData(FullExternalOrgView view) {
		view.setName("name");
		view.setCode("code");
		view.setParentOrg("parent_org");
		view.setContactName("contact_name");
		view.setContactEmail("contact_email");
		view.setStreetAddress("street_address");
		view.setCity("city");
		view.setState("state");
		view.setCountry("country");
		view.setZip("zip");
		view.setPhone1("phone1");
		view.setPhone2("phone2");
		view.setFax1("fax1");
	}
}
