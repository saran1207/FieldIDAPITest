package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.orgs.PrimaryOrg;

public class SafetyNetworkCustomer extends SafetyNetwork {

	private PrimaryOrg customer;
	private Long numRegisteredAssets;
	private Long numUnregisteredAssets;

	public SafetyNetworkCustomer(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		customer = getLoaderFactory().createCustomerLinkedOrgLoader().setLinkedOrgId(uniqueID).load();
		numRegisteredAssets = getLoaderFactory().createRegisteredAssetCountLoader().setCustomer(customer).setVendor(getPrimaryOrg()).load();
		numUnregisteredAssets = getLoaderFactory().createUnregisteredAssetCountLoader().setCustomer(customer).setVendor(getPrimaryOrg()).load();
	}

	@Override
	protected void initMemberFields() {
	}

	public String doShow() {
		return SUCCESS;
	}

	public PrimaryOrg getCustomer() {
		return customer;
	}

	public void setCustomer(PrimaryOrg customer) {
		this.customer = customer;
	}

	public Long getNumRegisteredAssets() {
		return numRegisteredAssets;
	}

	public Long getNumUnregisteredAssets() {
		return numUnregisteredAssets;
	}
}
