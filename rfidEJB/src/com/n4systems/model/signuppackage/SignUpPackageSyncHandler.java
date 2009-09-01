package com.n4systems.model.signuppackage;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.netsuite.client.PricingDetailsClient;
import com.n4systems.subscription.netsuite.model.ContractLength;
import com.n4systems.subscription.netsuite.model.GetPricingDetailsResponse;
import com.n4systems.subscription.netsuite.model.NetsuiteSubscription;
import com.n4systems.subscription.netsuite.model.ProductInformation;

public class SignUpPackageSyncHandler {
	
	private static final Logger logger = Logger.getLogger(SignUpPackageSyncHandler.class);
	
	private final ContractPricingSaver contractPricingSaver;
	private final PricingDetailsClient pricingClient;		
	private final ContractPricingByNsRecordIdLoader contractPricingByNsRecordIdLoader;
	private List<ProductInformation> productInformations;
	
	public SignUpPackageSyncHandler(ContractPricingByNsRecordIdLoader contractPricingByNsRecordIdLoader, 
			ContractPricingSaver contractPricingSaver) {
		this.contractPricingByNsRecordIdLoader = contractPricingByNsRecordIdLoader;
		this.contractPricingSaver = contractPricingSaver;
		this.pricingClient = new PricingDetailsClient();
	}
	
	public void sync() {
		ContractLength contractLength;
		
		for (PaymentOption paymentOption : PaymentOption.values()) {
			for (ProductInformation productInformation : productInformations) {
				SignUpPackageDetails signUpPackage = SignUpPackageDetails.retrieveBySyncId(productInformation.getFieldid());

				if (productInformation.getContractlengths() != null) {
										
					contractLength = productInformation.retrieveContractLengthMap().get(paymentOption.getTerm());
					
					if (contractLength != null) {
						
						GetPricingDetailsResponse response = getPricingDetailsFromNetsuite(populateSubscription(paymentOption, contractLength));
						
						
						lookupOrCreateNewContractPricing(contractLength.getNsrecordid(), signUpPackage, paymentOption, response.getPricing().getStandardPrice());
					}
				} else {
					lookupOrCreateNewContractPricing(productInformation.getNsrecordid(), signUpPackage, paymentOption, 0F);
				}
			}
		}		
	}
	
	private GetPricingDetailsResponse getPricingDetailsFromNetsuite(NetsuiteSubscription subscription) {
		pricingClient.setSubscription(subscription);						
		GetPricingDetailsResponse response = null;						
		try {
			response = pricingClient.execute();
		} catch (IOException e) {
			logger.error("Problem pulling payment option specific prices: "+e);
		}
		
		return response;
	}
	
	private void lookupOrCreateNewContractPricing(Long netSuiteRecordId, SignUpPackageDetails signUpPackage, PaymentOption paymentOption, Float price) {
		contractPricingByNsRecordIdLoader.setNetsuiteRecordId(netSuiteRecordId);
		contractPricingByNsRecordIdLoader.setSignUpPackage(signUpPackage);
		contractPricingByNsRecordIdLoader.setPaymentOption(paymentOption);
		ContractPricing contractPricing = contractPricingByNsRecordIdLoader.load();
		if (contractPricing == null) {
			contractPricing = new ContractPricing();
		}
		
		contractPricing.setNetsuiteRecordId(netSuiteRecordId);
		contractPricing.setPaymentOption(paymentOption);
		contractPricing.setPricePerUserPerMonth(price);
		contractPricing.setSignUpPackage(signUpPackage);					
		contractPricingSaver.saveOrUpdate(contractPricing);				
	}
	
	public NetsuiteSubscription populateSubscription(PaymentOption paymentOption, ContractLength contractLength) {
		NetsuiteSubscription subscription = new NetsuiteSubscription();						
		subscription.setFrequency(paymentOption.getFrequency());
		subscription.setMonths(paymentOption.getTerm());
		subscription.setNsrecordid(contractLength.getNsrecordid());
		subscription.setPurchasingPhoneSupport(false);
		subscription.setUsers(1);	
		
		return subscription;
	}
		
	public void setProductInformations(List<ProductInformation> productInformations) {
		this.productInformations = productInformations;
	}		
}
