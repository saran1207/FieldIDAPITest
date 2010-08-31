package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.internal.InternalOrgByNameLoader;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public class CustomerOrgToModelConverter extends ExternalOrgToModelConverter<CustomerOrg> {
	private final InternalOrgByNameLoader orgLoader;
	
	public CustomerOrgToModelConverter(GlobalIdLoader<CustomerOrg> externalIdLoader, InternalOrgByNameLoader orgLoader) {
		super(externalIdLoader);
		this.orgLoader = orgLoader;
	}

	@Override
	public void copyProperties(FullExternalOrgView from, CustomerOrg to, boolean isEdit) throws ConversionException {
		super.copyProperties(from, to, isEdit);
		
		// only modify the ownership information if it's an Add or if the parent org has changed
		if (!isEdit || !from.getParentOrg().equals(to.getParent().getName())) {
			InternalOrg parent = orgLoader.setName(from.getParentOrg()).load(transaction);
			if (parent == null) {
				throw new ConversionException("Could not find Organization named [" + from.getParentOrg() + "]");
			}
	
			to.setTenant(parent.getTenant());
			to.setParent(parent);
		}
	}

	@Override
	protected CustomerOrg createModelBean() {
		return new CustomerOrg();
	}
	
}
