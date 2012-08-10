package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import com.n4systems.model.safetynetwork.AllPreAssignedAssetsLoader;
import com.n4systems.model.safetynetwork.BulkRegisterData;
import com.n4systems.model.safetynetwork.BulkRegisterHelper;
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
    private Long assetTypeId;
    private BulkRegisterData bulkRegisterData;

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

	@SkipValidation
	public String doBulkRegister() {
        return SUCCESS;
	}

	@SkipValidation
	public String doBulkRegisterList() {
        AllPreAssignedAssetsLoader safetyNetworkPreAssignedAssetLoader = getLoaderFactory().createAllPreAssignedAssetsLoader().setVendor(vendor).setCustomer(getPrimaryOrg());
        BulkRegisterHelper helper = new BulkRegisterHelper(safetyNetworkPreAssignedAssetLoader);
        bulkRegisterData = helper.calculateBulkRegisterAssetTypeCounts();

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

    public BulkRegisterData getBulkRegisterData() {
        return bulkRegisterData;
    }

    public void setBulkRegisterData(BulkRegisterData bulkRegisterData) {
        this.bulkRegisterData = bulkRegisterData;
    }

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }
}
