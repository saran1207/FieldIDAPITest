package com.n4systems.model.signuppackage;

import java.util.List;

import com.n4systems.subscription.ContractPrice;

public class SignUpPackageSyncHandler {
	
	private final ContractPricingSaver contractPricingSaver;
	private final ContractPricingByExternalIdLoader contractPricingByExternalIdLoader;
	private List<ContractPrice> contractPrices;
	
	public SignUpPackageSyncHandler(ContractPricingByExternalIdLoader contractPricingByExternalIdLoader, ContractPricingSaver contractPricingSaver) {
		this.contractPricingByExternalIdLoader = contractPricingByExternalIdLoader;
		this.contractPricingSaver = contractPricingSaver;
	}
	
	public void sync() {		
		for (ContractPrice contractPrice : contractPrices) {
			lookupOrCreateNewContractPricing(contractPrice);
		}
	}
	
	private void lookupOrCreateNewContractPricing(ContractPrice contractPrice) {
		SignUpPackageDetails signUpPackage = SignUpPackageDetails.retrieveBySyncId(contractPrice.getSyncId());
		
		contractPricingByExternalIdLoader.setExternalId(contractPrice.getExternalId());
		contractPricingByExternalIdLoader.setSignUpPackage(signUpPackage);
		contractPricingByExternalIdLoader.setPaymentOption(contractPrice.getPaymentOption());
		ContractPricing contractPricing = contractPricingByExternalIdLoader.load();
		if (contractPricing == null) {
			contractPricing = new ContractPricing();
		}
		
		contractPricing.setExternalId(contractPrice.getExternalId());
		contractPricing.setSignUpPackage(signUpPackage);					
		contractPricing.setPaymentOption(contractPrice.getPaymentOption());
		contractPricing.setPricePerUserPerMonth(contractPrice.getPrice());
		contractPricingSaver.saveOrUpdate(contractPricing);				
	}
	
	public void setContractPrices(List<ContractPrice> contractPrices) {
		this.contractPrices = contractPrices;
	}		

}
