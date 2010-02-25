package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.orgs.CustomerOrg;

public class CustomerOrgToViewConverter extends ExternalOrgToViewConverter<CustomerOrg> {

	public CustomerOrgToViewConverter() {
		super();
	}

	@Override
	public void copyProperties(CustomerOrg from, FullExternalOrgView to) throws ConversionException {
		super.copyProperties(from, to);
		to.setTypeToCustomer();
		to.setParentOrg(from.getParent().getName());
	}
	
}
