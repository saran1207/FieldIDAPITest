package com.n4systems.ejb.legacy;

import java.util.Collection;

import com.n4systems.model.AssetType;
import rfid.ejb.entity.IdentifierCounterBean;

import com.n4systems.model.orgs.PrimaryOrg;

public interface IdentifierCounter {

	public void updateIdentifierCounter(IdentifierCounterBean identifierCounter);
	public Collection<IdentifierCounterBean> getIdentifierCounters();
	public IdentifierCounterBean getIdentifierCounter(Long tenantId);
	public String getNextCounterValue(Long tenantId);
	public String generateIdentifier(PrimaryOrg primaryOrg, AssetType assetType);
	
}
