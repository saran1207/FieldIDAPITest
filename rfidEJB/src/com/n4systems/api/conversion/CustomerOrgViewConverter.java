package com.n4systems.api.conversion;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.internal.InternalOrgByNameLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public class CustomerOrgViewConverter extends ExternalOrgViewConverter<CustomerOrg> {
	private InternalOrgByNameLoader orgLoader;
	
	public CustomerOrgViewConverter(SecurityFilter filter) {
		super(filter, CustomerOrg.class);
		orgLoader = new InternalOrgByNameLoader(filter);
	}

	public CustomerOrgViewConverter(GlobalIdLoader<CustomerOrg> externalIdLoader, SecurityFilter filter, ViewValidator<FullExternalOrgView> viewValidator) {
		super(externalIdLoader, filter, CustomerOrg.class, viewValidator);
		orgLoader = new InternalOrgByNameLoader(filter);
	}

	@Override
	public void copyProperties(CustomerOrg from, FullExternalOrgView to) throws ConversionException {
		super.copyProperties(from, to);
		to.setTypeToCustomer();
		to.setParentOrg(from.getParent().getName());
	}

	@Override
	public void copyProperties(FullExternalOrgView from, CustomerOrg to, boolean isEdit) throws ConversionException {
		super.copyProperties(from, to, isEdit);
		
		// we only want to set the parent org on an add, or if the parent org has changed
		if (!isEdit || !to.getParent().getName().equals(from.getParentOrg())) {
			setParentOrg(from, to);
		}
	}

	private void setParentOrg(FullExternalOrgView from, CustomerOrg to) throws ConversionException {
		InternalOrg parent = orgLoader.setName(from.getParentOrg()).load();

		if (parent == null) {
			throw new ConversionException("Could not find Organization named [" + from.getParentOrg() + "]");
		}

		to.setParent(parent);
	}
	
}
