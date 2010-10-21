package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Asset;
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
public class SafetyNetworkVendor extends SafetyNetwork {

	private PrimaryOrg vendor;
	private Pager<Asset> page;
	private String searchText;
	private int pageSize = 10;
	private Long assetID;

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
		return (isSingleAssetResult()) ? "oneFound" : SUCCESS;
	}

	private boolean isSingleAssetResult() {

		if (page.getList().size() == 1) {
			assetID = page.getList().get(0).getId();
			return true;
		}
		return false;
	}

	private Pager<Asset> getPagedSearchResults() {
		SafetyNetworkSmartSearchLoader smartSearchLoader = setupLoader();
		List<Asset> list = smartSearchLoader.load();
		return new SimplePager<Asset>(1, pageSize, list.size(), list);
	}

	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}

	public Pager<Asset> getPage() {
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
		smartSearchLoader.setVendor(vendor);
		smartSearchLoader.setCustomer(getPrimaryOrg());
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

	public Long getAssetID() {
		return assetID;
	}

	
}
