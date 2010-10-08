package com.n4systems.model.safetynetwork;

import java.util.List;
import javax.persistence.EntityManager;
import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.SmartSearchWhereClause;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.utils.PostFetcher;

public class SafetyNetworkSmartSearchLoader extends ListLoader<Product> {
	private final VendorLinkedOrgLoader linkedOrgLoader;
	private String searchText;
	private boolean useSerialNumber = true;
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
	protected List<Product> load(EntityManager em, SecurityFilter filter) {

		createUnregisteredAssetLoader();
		List<Product> unsecuredProducts = unregisteredAssetQueryHelper.getList(em);
		
		PostFetcher.postFetchFields(unsecuredProducts, "infoOptions");
		
		// our product list may contain products which are set to a customer who is not me.
		SafetyNetworkProductSecurityManager securityManager = new SafetyNetworkProductSecurityManager(filter.getOwner());
		List<Product> securedProducts = securityManager.filterOutExternalNotAssignedProducts(unsecuredProducts);

		return securedProducts;
	}

	private void createUnregisteredAssetLoader() {
		unregisteredAssetQueryHelper = new UnregisteredAssetQueryHelper(vendor, customer, false);
		unregisteredAssetQueryHelper.setSmartSearchParameters(new SmartSearchWhereClause(searchText, useSerialNumber, useRfidNumber, useRefNumber));
	}
	
	public SafetyNetworkSmartSearchLoader setVendorOrgId(Long vendorOrgId) {
		linkedOrgLoader.setLinkedOrgId(vendorOrgId);
		return this;
	}

	public SafetyNetworkSmartSearchLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlySerialNumber() {
		setUseSerialNumber(true);
		setUseRfidNumber(false);
		setUseRefNumber(false);
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyRfidNumber() {
		setUseRfidNumber(true);
		setUseSerialNumber(false);
		setUseRefNumber(false);
		return this;
	}

	public SafetyNetworkSmartSearchLoader useOnlyRefNumber() {
		setUseRefNumber(true);
		setUseRfidNumber(false);
		setUseSerialNumber(false);
		return this;
	}

	private void setUseSerialNumber(boolean useSerialNumber) {
		this.useSerialNumber = useSerialNumber;
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
