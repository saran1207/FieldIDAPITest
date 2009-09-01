package com.n4systems.model.signuppackage;

import com.n4systems.subscription.netsuite.model.ContractLength;

public class ContractPricingPopulator {

	private ContractPricing contractPricing;
	
	public ContractPricingPopulator(ContractPricing contractPricing) {
		this.contractPricing = contractPricing;
	}
	
	public ContractPricing populateContractPricing(ContractLength contractLength, String syncId) {
		contractPricing.setNetsuiteRecordId(contractLength.getNsrecordid());
		
		return contractPricing;
	}
}
