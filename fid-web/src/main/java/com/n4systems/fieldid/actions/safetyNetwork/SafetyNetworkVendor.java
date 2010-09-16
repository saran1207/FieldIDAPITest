package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.SafetyNetworkSmartSearchLoader;
import com.n4systems.persistence.SimplePager;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Pager;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
@SuppressWarnings("serial")
public class SafetyNetworkVendor extends AbstractCrud{
	
	private PrimaryOrg vendor;
	private Pager<Product> page;
	private String searchText;
	private int pageSize = 10;

	public SafetyNetworkVendor(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		setVendorId(uniqueId);
	}
		
	@SkipValidation
	public String doListPreAssigned() {
		page = getLoaderFactory().createSafetyNetworkPreAssignedAssetLoader().setVendor(vendor).setCustomer(getPrimaryOrg()).setPage(getCurrentPage()).load();
		return SUCCESS;
	}
	
	public String doSearch() {
		page = getPagedSearchResults();
		return SUCCESS;		
	}
	
	private Pager<Product> getPagedSearchResults() {
		SafetyNetworkSmartSearchLoader smartSearchLoader = setupLoader();
		List<Product> list = smartSearchLoader.load();
		return new SimplePager<Product>(1, pageSize, list.size(), list);
	}

	@SkipValidation
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
	
	private SafetyNetworkSmartSearchLoader setupLoader() {
		SafetyNetworkSmartSearchLoader smartSearchLoader = getLoaderFactory().createSafetyNetworkSmartSearchLoader();
		smartSearchLoader.setVendorOrgId(vendor.getId());
		smartSearchLoader.setSearchText(searchText);
		return smartSearchLoader;
	}
	
	@RequiredStringValidator(type = ValidatorType.SIMPLE, message = "", key = "error.search_text_required")
	public String getSearchText() {
		return searchText;
	}
	
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public void setVendorId(Long vendorId) {
		vendor = getLoaderFactory().createVendorLinkedOrgLoader().setLinkedOrgId(vendorId).load();
	}

}
