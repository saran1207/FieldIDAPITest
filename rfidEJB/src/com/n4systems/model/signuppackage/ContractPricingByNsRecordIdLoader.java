package com.n4systems.model.signuppackage;

import javax.persistence.EntityManager;

import com.n4systems.model.ContractPricing;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class ContractPricingByNsRecordIdLoader extends Loader<ContractPricing> {

	private String netsuiteRecordId;
	private String syncId;
	
	@Override
	protected ContractPricing load(EntityManager em) {
		QueryBuilder<ContractPricing> builder = new QueryBuilder<ContractPricing>(ContractPricing.class);
		builder.addSimpleWhere("netsuiteRecordId", netsuiteRecordId);
		
		if (syncId != null) {
			builder.addSimpleWhere("syncId", syncId);
		}
		
		ContractPricing contractPricing = builder.getSingleResult(em);
		
		return contractPricing;
	}

	public void setNetsuiteRecordId(String netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}
}
