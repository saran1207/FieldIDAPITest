package com.n4systems.persistence.savers;

import com.n4systems.model.autoattribute.AutoAttributeDefinitionSaver;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.orders.NonIntegrationLineItemSaver;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.user.UserSaver;

public class SaverFactory {

	public SaverFactory() {}
	
	/* 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */
	
	public AutoAttributeDefinitionSaver createAutoAttributeDefinitionSaver() {
		return new AutoAttributeDefinitionSaver();
	}
	
	public DownloadLinkSaver createDownloadLinkSaver() {
		return new DownloadLinkSaver();
	}
	
	public NonIntegrationLineItemSaver createNonIntegrationLineItemSaver() {
		return new NonIntegrationLineItemSaver();
	}
	
	public OrgSaver createOrgSaver() {
		return new OrgSaver();
	}

	public UserSaver createUserSaver() {
		return new UserSaver();
	}
}
