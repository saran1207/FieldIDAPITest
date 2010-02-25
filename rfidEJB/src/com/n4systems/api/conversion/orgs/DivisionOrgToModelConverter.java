package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public class DivisionOrgToModelConverter extends ExternalOrgToModelConverter<DivisionOrg> {
	
	private CustomerOrg parentCustomer;
	
	public DivisionOrgToModelConverter(SecurityFilter filter) {
		this(new GlobalIdLoader<DivisionOrg>(filter, DivisionOrg.class));
	}
	
	public DivisionOrgToModelConverter(GlobalIdLoader<DivisionOrg> externalIdLoader) {
		super(externalIdLoader);
	}

	@Override
	public void copyProperties(FullExternalOrgView from, DivisionOrg to, boolean isEdit) throws ConversionException {
		super.copyProperties(from, to, isEdit);
		
		// you're not allowed to change the parent customer of a division on edit
		if (!isEdit) {
			if (parentCustomer == null) {
				throw new ConversionException("No parent Customer set for Division [" + from.getName() + "]");
			}
			
			to.setTenant(parentCustomer.getTenant());
			to.setParent(parentCustomer);
		}
	}

	@Override
	protected DivisionOrg createModelBean() {
		return new DivisionOrg();
	}
	
	public void setParentCustomer(CustomerOrg parentCustomer) {
		this.parentCustomer = parentCustomer;
	}
	
}
