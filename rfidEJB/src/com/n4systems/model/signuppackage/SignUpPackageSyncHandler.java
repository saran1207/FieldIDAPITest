package com.n4systems.model.signuppackage;

import java.util.List;

import com.n4systems.model.ContractPricing;
import com.n4systems.netsuite.model.ContractLength;
import com.n4systems.netsuite.model.ProductInformation;

public class SignUpPackageSyncHandler {
	
	private final ContractPricingSaver contractPricingSaver;
	private final ContractPricingByNsRecordIdLoader contractPricingByNsRecordIdLoader;
	private List<ProductInformation> productInformations;
	
	public SignUpPackageSyncHandler(ContractPricingByNsRecordIdLoader contractPricingByNsRecordIdLoader, 
			ContractPricingSaver contractPricingSaver) {
		this.contractPricingByNsRecordIdLoader = contractPricingByNsRecordIdLoader;
		this.contractPricingSaver = contractPricingSaver;
	}
	
	public void sync() {
		
		ContractPricing contractPricing;
		
		for (ProductInformation productInformation : productInformations) {
			if (productInformation.getContractlengths() != null) {
				for (ContractLength contractLength : productInformation.getContractlengths()) {
					contractPricing = lookupOrCreateNewContractPricing(contractLength, productInformation.getFieldid());
					contractPricingSaver.saveOrUpdate(contractPricing);
				}
			}
			
		}		
	}
	
	private ContractPricing lookupOrCreateNewContractPricing(ContractLength contractLength, String syncId) {
		contractPricingByNsRecordIdLoader.setNetsuiteRecordId(contractLength.getNsrecordid());
		contractPricingByNsRecordIdLoader.setSyncId(syncId);
		ContractPricing contractPricing = contractPricingByNsRecordIdLoader.load();
		if (contractPricing == null) {
			contractPricing = new ContractPricing();
		}
		
		ContractPricingPopulator populator = new ContractPricingPopulator(contractPricing);
		contractPricing = populator.populateContractPricing(contractLength, syncId);
		
		return contractPricing;
	}

	public void setProductInformations(List<ProductInformation> productInformations) {
		this.productInformations = productInformations;
	}		
}
