package com.n4systems.model.signuppackage;

import com.n4systems.model.ContractPricing;
import com.n4systems.netsuite.model.ContractLength;

public class ContractPricingPopulator {

	private ContractPricing contractPricing;
	
	public ContractPricingPopulator(ContractPricing contractPricing) {
		this.contractPricing = contractPricing;
	}
	
	public ContractPricing populateContractPricing(ContractLength contractLength, String syncId) {
		contractPricing.setContractLength(contractLength.getMonths());
		contractPricing.setNetsuiteRecordId(contractLength.getNsrecordid());
		contractPricing.setPrice(contractLength.getPrice());
		contractPricing.setSyncId(syncId);
		
		return contractPricing;
	}
}
