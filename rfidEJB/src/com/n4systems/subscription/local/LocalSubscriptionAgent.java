package com.n4systems.subscription.local;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.Response;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeSubscription;
import com.n4systems.subscription.ValidatePromoCodeResponse;

public class LocalSubscriptionAgent extends SubscriptionAgent {

	private static final String packageExtension = ".package";
	private static String tempDirectory = "/tmp";
	
	@Override
	public SignUpTenantResponse buy(Subscription subscription, Company company, Person client) {
		LocalSignUpTenantResponse signUpResponse = new LocalSignUpTenantResponse();
		
		writePackageInfo(signUpResponse.getTenant().getExternalId(), subscription.getContractExternalId()); 
		return signUpResponse;
	}

	private void writePackageInfo(Long externalId, Long contractId) {
		File tenantFile = currentContractFile(externalId);
		
		if (tenantFile.exists()) {
			tenantFile.delete();
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(tenantFile);
			fileWriter.write(contractId.toString());
			fileWriter.close();
		} catch (IOException e) {}
		finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {}
			}
		}
		
	}

	
	@Override
	public ValidatePromoCodeResponse validatePromoCode(String code)	throws CommunicationException {
		return new LocalValidatePromoCodeResponse();
	}

	
	@Override
	public PriceCheckResponse priceCheck(Subscription subscription)	throws CommunicationException {
		if (subscription.getFrequency() == null || subscription.getMonths() == null || 
				subscription.getContractExternalId() == null || subscription.getUsers() < 1) {
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
		contractPrices.add(populateContractPrice(151L, "FIDUNLIMITED", PaymentOption.ONE_YEAR_UP_FRONT, 225.5F));
		contractPrices.add(populateContractPrice(160L, "FIDUNLIMITED", PaymentOption.TWO_YEARS_UP_FRONT, 202.1F));

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

	@Override
	public Response attachNote(Long tenantExternalId, String title, String note) throws CommunicationException {
		return new LocalResponse();
	}
	


	private Long findContractFromFile(Long tenantExternalId) {
		File tenantFile = currentContractFile(tenantExternalId);
		Long contractId = null;
		
		if (tenantFile.exists()) {
			FileReader reader = null;
			String readContractId = "";
			try {
				char[] buffer = new char[1];
				reader = new FileReader(tenantFile);
				while(reader.ready()) {
					reader.read(buffer);
					readContractId += buffer[0];
				}
				contractId = Long.valueOf(readContractId);
				
			} catch (Exception e) {			
			} finally {
				if (reader != null)
					try { reader.close(); } catch (IOException e) {}
			}
			
		}
		return contractId;
	}

	private File currentContractFile(Long tenantExternalId) {
		File tenantFile = new File(tempDirectory + "/" + tenantExternalId.toString() + packageExtension);
		return tenantFile;
	}

	@Override
	public boolean upgrade(UpgradeSubscription upgradeSubscription) throws CommunicationException {
		writePackageInfo(upgradeSubscription.getTenantExternalId(), upgradeSubscription.getContractExternalId());
		return true;
	}

	@Override
	public Long contractIdFor(Long tenantExternalId) throws CommunicationException {
		return findContractFromFile(tenantExternalId);
	}

	@Override
	public UpgradeCost costToUpgradeTo(UpgradeSubscription upgradeSubscription) throws CommunicationException {
		return new UpgradeCost(1000F, 4000F, "JAN. 18, 2009");
	}

	
	
	

}
