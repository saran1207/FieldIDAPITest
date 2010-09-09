package com.n4systems.fieldid.actions.safetyNetwork;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Pager;

@SuppressWarnings("serial")
public class SafetyNetworkVendor extends AbstractCrud{
	
	private PrimaryOrg vendor;
	private Pager<Product> page;

	public SafetyNetworkVendor(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		vendor = getLoaderFactory().createVendorLinkedOrgLoader().setLinkedOrgId(uniqueID).load();
	}
		
	@SkipValidation
	public String doListPreAssigned() {
		page = getLoaderFactory().createSafetyNetworkPreAssignedAssetLoader().setVendor(vendor).setOwner(getPrimaryOrg()).load();
		return SUCCESS;
	}
	
	public String doShow() {
		return SUCCESS;
	}
	
	public Pager<Product> getPage() {	
		return page;
	}
	
	public PrimaryOrg getVendor() {		
		return vendor;
	}

	public String getLogo() {
		return PathHandler.getTenantLogo(vendor.getTenant()).getAbsolutePath();
	}
	
}
