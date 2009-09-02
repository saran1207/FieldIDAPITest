package com.n4systems.subscription.local;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.ValidatePromoCodeResponse;

public class LocalSubscriptionAgent extends SubscriptionAgent {

	@Override
	public SignUpTenantResponse buy(Subscription subscription, Company company, Person client) {
		return new LocalSignUpTenantResponse();
	}

	@Override
	public ValidatePromoCodeResponse validatePromoCode(String code)	throws CommunicationException {
		return new LocalValidatePromoCodeResponse();
	}

	
	@Override
	public PriceCheckResponse priceCheck(Subscription subscription)	throws CommunicationException {
		if (subscription.getFrequency() == null || subscription.getMonths() == null || 
				subscription.getContractId() == null || subscription.getUsers() < 1) {
			throw new RuntimeException();
		}
		
		
		LocalPriceCheckResponse priceCheckResponse = new LocalPriceCheckResponse(new LocalPricing());
		return priceCheckResponse;
	}

	@Override
	public List<ContractPrice> retrieveContractPrices()	throws CommunicationException {
		List<ContractPrice> contractPrices = new ArrayList<ContractPrice>();
		
		contractPrices.add(populateContractPrice(134L, "FIDFREE", PaymentOption.MONTH_TO_MONTH, 0F));
		contractPrices.add(populateContractPrice(134L, "FIDFREE", PaymentOption.ONE_YEAR_UP_FRONT, 0F));
		contractPrices.add(populateContractPrice(134L, "FIDFREE", PaymentOption.TWO_YEARS_UP_FRONT, 0F));
		
		contractPrices.add(populateContractPrice(146L, "FIDBASIC", PaymentOption.MONTH_TO_MONTH, 40F));
		contractPrices.add(populateContractPrice(146L, "FIDBASIC", PaymentOption.ONE_YEAR_UP_FRONT, 40F));
		contractPrices.add(populateContractPrice(157L, "FIDBASIC", PaymentOption.TWO_YEARS_UP_FRONT, 40F));

		contractPrices.add(populateContractPrice(143L, "FIDPLUS", PaymentOption.MONTH_TO_MONTH, 100F));
		contractPrices.add(populateContractPrice(143L, "FIDPLUS", PaymentOption.ONE_YEAR_UP_FRONT, 100F));
		contractPrices.add(populateContractPrice(159L, "FIDPLUS", PaymentOption.TWO_YEARS_UP_FRONT, 100F));

		contractPrices.add(populateContractPrice(149L, "FIDENTERPRISE", PaymentOption.MONTH_TO_MONTH, 175F));
		contractPrices.add(populateContractPrice(149L, "FIDENTERPRISE", PaymentOption.ONE_YEAR_UP_FRONT, 175F));
		contractPrices.add(populateContractPrice(158L, "FIDENTERPRISE", PaymentOption.TWO_YEARS_UP_FRONT, 175F));

		contractPrices.add(populateContractPrice(151L, "FIDUNLIMITED", PaymentOption.MONTH_TO_MONTH, 225F));
		contractPrices.add(populateContractPrice(151L, "FIDUNLIMITED", PaymentOption.ONE_YEAR_UP_FRONT, 225F));
		contractPrices.add(populateContractPrice(160L, "FIDUNLIMITED", PaymentOption.TWO_YEARS_UP_FRONT, 225F));

		return contractPrices;
	}
	
	private LocalContractPrice populateContractPrice(Long externalId, String syncId, PaymentOption paymentOption, Float price) {
		LocalContractPrice localContractPrice = new LocalContractPrice();
		localContractPrice.setExternalId(externalId);
		localContractPrice.setPaymentOption(paymentOption);
		localContractPrice.setPrice(price);
		localContractPrice.setSyncId(syncId);
		
		return localContractPrice;
	}

}
