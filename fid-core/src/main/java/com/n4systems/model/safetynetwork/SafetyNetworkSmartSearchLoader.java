package com.n4systems.model.safetynetwork;

import com.n4systems.model.Asset;
import com.n4systems.model.asset.SmartSearchWhereClause;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.utils.PostFetcher;

import javax.persistence.EntityManager;
import java.util.List;

public class SafetyNetworkSmartSearchLoader extends ListLoader<Asset> {
	private final VendorLinkedOrgLoader linkedOrgLoader;
	private String searchText;
	private boolean useIdentifier = true;
	private boolean useRfidNumber = true;
	private boolean useRefNumber = true;
	private PrimaryOrg customer;
	private PrimaryOrg vendor;
	private UnregisteredAssetQueryHelper unregisteredAssetQueryHelper;

	public SafetyNetworkSmartSearchLoader(SecurityFilter filter, VendorLinkedOrgLoader linkedOrgLoader) {
		super(filter);
		this.linkedOrgLoader = linkedOrgLoader;
	}

	public SafetyNetworkSmartSearchLoader(SecurityFilter filter) {
		this(filter, new VendorLinkedOrgLoader(filter));
	}

	@Override
	protected List<Asset> load(EntityManager em, SecurityFilter filter) {

		createUnregisteredAssetLoader();
		List<Asset> unsecuredAssets = unregisteredAssetQueryHelper.getList(em);
		
		PostFetcher.postFetchFields(unsecuredAssets, "infoOptions");
		
		// our asset list may contain assets which are set to a customer who is not me.
		SafetyNetworkAssetSecurityManager securityManager = new SafetyNetworkAssetSecurityManager(filter.getOwner());
		List<Asset> securedAssets = securityManager.filterOutExternalNotAssignedAssets(unsecuredAssets);

		return securedAssets;
	}

	private void createUnregisteredAssetLoader() {
		unregisteredAssetQueryHelper = new UnregisteredAssetQueryHelper(vendor, customer, false);
		unregisteredAssetQueryHelper.setSmartSearchParameters(new SmartSearchWhereClause(searchText, useIdentifier, useRfidNumber, useRefNumber));
	}
	
	public SafetyNetworkSmartSearchLoader setVendorOrgId(Long vendorOrgId) {
		linkedOrgLoader.setLinkedOrgId(vendorOrgId);
		return this;
	}

	public SafetyNetworkSmartSearchLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyIdentifier() {
		setUseIdentifier(true);
		setUseRfidNumber(false);
		setUseRefNumber(false);
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyRfidNumber() {
		setUseRfidNumber(true);
		setUseIdentifier(false);
		setUseRefNumber(false);
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyRefNumber() {
		setUseRefNumber(true);
		setUseRfidNumber(false);
		setUseIdentifier(false);
		return this;
	}

	private void setUseIdentifier(boolean useIdentifier) {
		this.useIdentifier = useIdentifier;
	}

	private void setUseRfidNumber(boolean useRfidNumber) {
		this.useRfidNumber = useRfidNumber;
	}

	private void setUseRefNumber(boolean useRefNumber) {
		this.useRefNumber = useRefNumber;
	}

	public void setCustomer(PrimaryOrg customer) {
		this.customer = customer;
	}

	public void setVendor(PrimaryOrg vendor) {
		this.vendor = vendor;
	}
}
