package com.n4systems.subscription.netsuite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.netsuite.client.PricingDetailsClient;
import com.n4systems.subscription.netsuite.client.ProductDetailsClient;
import com.n4systems.subscription.netsuite.model.ContractLength;
import com.n4systems.subscription.netsuite.model.GetItemDetailsResponse;
import com.n4systems.subscription.netsuite.model.GetPricingDetailsResponse;
import com.n4systems.subscription.netsuite.model.NetSuiteContractPrice;
import com.n4systems.subscription.netsuite.model.NetSuiteProductInformation;
import com.n4systems.subscription.netsuite.model.NetsuiteSubscription;

public class NetSuiteContractPriceHandler {

	private final ProductDetailsClient productDetailsClient;
	private final PricingDetailsClient pricingDetailsClient;
	
	public NetSuiteContractPriceHandler(ProductDetailsClient productDetailsClient, PricingDetailsClient pricingDetailsClient) {
		this.productDetailsClient = productDetailsClient;
		this.pricingDetailsClient = pricingDetailsClient;
	}
	
	public List<ContractPrice> retrieveContractPrices()	throws CommunicationException {
		List<NetSuiteProductInformation> productInformations = getProductInformationFromNetSuite();
		
		ContractLength contractLength;
		List<ContractPrice> contractPrices = new ArrayList<ContractPrice>();
		
		for (PaymentOption paymentOption : PaymentOption.values()) {
			for (NetSuiteProductInformation productInformation : productInformations) {
				productInformation.getSyncId();

				if (productInformation.getContractlengths() != null) {										
					contractLength = productInformation.retrieveContractLengthMap().get(paymentOption.getTerm());
					
					if (contractLength != null) {						
						GetPricingDetailsResponse response = getPricingDetailsFromNetsuite(populateSubscription(paymentOption, contractLength));									
						contractPrices.add(populateNetSuiteContractPrice(contractLength.getNsrecordid(), paymentOption, productInformation.getSyncId(), response.getPricing().getPrice()));
					}
				} else {
					contractPrices.add(populateNetSuiteContractPrice(productInformation.getExternalId(), paymentOption, productInformation.getSyncId(), 0F));
				}
			}
		}				
		
		return contractPrices;
	}

	private NetsuiteSubscription populateSubscription(PaymentOption paymentOption, ContractLength contractLength) {
		NetsuiteSubscription subscription = new NetsuiteSubscription();						
		subscription.setFrequency(paymentOption.getFrequency());
		subscription.setMonths(paymentOption.getTerm());
		subscription.setNsrecordid(contractLength.getNsrecordid());
		subscription.setPurchasingPhoneSupport(false);
		subscription.setUsers(1);	
		
		return subscription;
	}
	
	private List<NetSuiteProductInformation> getProductInformationFromNetSuite() throws CommunicationException {
		try {
			GetItemDetailsResponse response = productDetailsClient.execute();
			return response.getItemlist();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
	}

	private GetPricingDetailsResponse getPricingDetailsFromNetsuite(NetsuiteSubscription subscription) throws CommunicationException {
		pricingDetailsClient.setSubscription(subscription);						
		GetPricingDetailsResponse response = null;						
		try {
			response = pricingDetailsClient.execute();
		} catch (IOException e) {
			throw new CommunicationException();
		}
		
		return response;
	}
	
	private NetSuiteContractPrice populateNetSuiteContractPrice(Long externalId, PaymentOption paymentOption, String syncId, Float price) {
		NetSuiteContractPrice contractPrice = new NetSuiteContractPrice();
		contractPrice.setExternalId(externalId);
		contractPrice.setPaymentOption(paymentOption);
		contractPrice.setSyncId(syncId);
		contractPrice.setPrice(price);
		
		return contractPrice;
	}
}
