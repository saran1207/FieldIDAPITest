package com.n4systems.api.conversion;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public class DivisionOrgViewConverter extends ExternalOrgViewConverter<DivisionOrg> {
	private CustomerOrg parentCustomer;
	
	public DivisionOrgViewConverter(SecurityFilter filter) {
		super(filter, DivisionOrg.class);
	}

	public DivisionOrgViewConverter(GlobalIdLoader<DivisionOrg> externalIdLoader, SecurityFilter filter) {
		super(externalIdLoader, filter, DivisionOrg.class);
	}

	@Override
	public void copyProperties(DivisionOrg from, FullExternalOrgView to) throws ConversionException {
		super.copyProperties(from, to);
		to.setTypeToDivision();
	}

	@Override
	public void copyProperties(FullExternalOrgView from, DivisionOrg to, boolean isEdit) throws ConversionException {
		super.copyProperties(from, to, isEdit);
		
		// you're not allowed to change the parent customer of a division on edit
		if (!isEdit) {
			if (parentCustomer == null) {
				throw new ConversionException("No parent Customer set for Division [" + from.getName() + "]");
			}
			
			to.setParent(parentCustomer);
		}
	}

	public void setParentCustomer(CustomerOrg parentCustomer) {
		this.parentCustomer = parentCustomer;
	}
	
}
