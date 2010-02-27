package com.n4systems.persistence.savers;

import com.n4systems.model.autoattribute.AutoAttributeDefinitionSaver;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.signuppackage.ContractPricingSaver;

public class SaverFactory {

	public SaverFactory() {}
	
	/* 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */
	
	public AutoAttributeDefinitionSaver createAutoAttributeDefinitionSaver() {
		return new AutoAttributeDefinitionSaver();
	}
	
	public ContractPricingSaver createContractPricingSaver() {
		return new ContractPricingSaver();
	}
	
	public DownloadLinkSaver createDownloadLinkSaver() {
		return new DownloadLinkSaver();
	}
	
	public OrgSaver createOrgSaver() {
		return new OrgSaver();
	}
}
