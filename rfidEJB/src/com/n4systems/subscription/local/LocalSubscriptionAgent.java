package com.n4systems.subscription.local;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.CurrentSubscription;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.PriceCheckResponse;
import com.n4systems.subscription.Response;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.n4systems.subscription.UpgradeSubscription;
import com.n4systems.subscription.ValidatePromoCodeResponse;

public class LocalSubscriptionAgent extends SubscriptionAgent {

	private static final String packageExtension = ".package";
	private static String tempDirectory = "/tmp";
	
	public SignUpTenantResponse buy(Subscription subscription, Company company, Person client) {
		LocalSignUpTenantResponse signUpResponse = new LocalSignUpTenantResponse();
		
		writePackageInfo(signUpResponse.getTenant().getExternalId(), subscription.getContractExternalId(), subscription.isPurchasingPhoneSupport(), company.isUsingCreditCard(), company.isUsingCreditCard()); 
		return signUpResponse;
	}

	private void writePackageInfo(Long externalId, Long contractId, boolean phoneSupport, boolean payViaCC, boolean ccOnFile) {
		File tenantFile = currentContractFile(externalId);
		
		if (tenantFile.exists()) {
			tenantFile.delete();
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(tenantFile);
			fileWriter.write(contractId.toString() + "," + phoneSupport + "," + payViaCC + "," + ccOnFile);
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

	
	public ValidatePromoCodeResponse validatePromoCode(String code)	throws CommunicationException {
		return new LocalValidatePromoCodeResponse();
	}

	
	public PriceCheckResponse priceCheck(Subscription subscription)	throws CommunicationException {
		if (subscription.getFrequency() == null || subscription.getMonths() == null || 
				subscription.getContractExternalId() == null || subscription.getUsers() < 1) {
			throw new RuntimeException();
		}
		
		
		LocalPriceCheckResponse priceCheckResponse = new LocalPriceCheckResponse(new LocalPricing());
		return priceCheckResponse;
	}

	public List<ContractPrice> retrieveContractPrices()	throws CommunicationException {
		List<ContractPrice> contractPrices = new ArrayList<ContractPrice>();
		
		contractPrices.add(populateContractPrice(134L, "FIDFREE", PaymentOption.ONE_YEAR_UP_FRONT, 0F));
		contractPrices.add(populateContractPrice(134L, "FIDFREE", PaymentOption.TWO_YEARS_UP_FRONT, 0F));
		
		contractPrices.add(populateContractPrice(146L, "FIDBASIC", PaymentOption.ONE_YEAR_UP_FRONT, 40F));
		contractPrices.add(populateContractPrice(157L, "FIDBASIC", PaymentOption.TWO_YEARS_UP_FRONT, 40F));

		contractPrices.add(populateContractPrice(143L, "FIDPLUS", PaymentOption.ONE_YEAR_UP_FRONT, 100F));
		contractPrices.add(populateContractPrice(159L, "FIDPLUS", PaymentOption.TWO_YEARS_UP_FRONT, 100F));

		contractPrices.add(populateContractPrice(149L, "FIDENTERPRISE", PaymentOption.ONE_YEAR_UP_FRONT, 175F));
		contractPrices.add(populateContractPrice(158L, "FIDENTERPRISE", PaymentOption.TWO_YEARS_UP_FRONT, 175F));

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

	public Response attachNote(Long tenantExternalId, String title, String note) throws CommunicationException {
		return new LocalResponse();
	}
	


	private CurrentSubscription findContractFromFile(Long tenantExternalId) {
		File tenantFile = currentContractFile(tenantExternalId);
		CurrentSubscription result = null;
		
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
			} catch (Exception e) {		
				return null;
			} finally {
				if (reader != null)
					try { reader.close(); } catch (IOException e) {}
			}
			result = parseCurrentSubscription(readContractId);
			
		}
		return result;
	}

	private CurrentSubscription parseCurrentSubscription(String readContractId) {
		CurrentSubscription result;
		String[] values = readContractId.split(",");
		Long contractId = Long.valueOf(values[0].trim());
		boolean phoneSupport = Boolean.valueOf(values[1].trim());
		boolean payViaCC = Boolean.valueOf(values[2].trim());
		boolean ccOnRecord = Boolean.valueOf(values[3].trim());
		result = new CurrentSubscription(contractId, phoneSupport, ccOnRecord, payViaCC);
		return result;
	}

	private File currentContractFile(Long tenantExternalId) {
		File tenantFile = new File(tempDirectory + "/" + tenantExternalId.toString() + packageExtension);
		return tenantFile;
	}

	public UpgradeResponse upgrade(UpgradeSubscription upgradeSubscription) throws CommunicationException, BillingInfoException {
		writePackageInfo(upgradeSubscription.getTenantExternalId(), upgradeSubscription.getContractExternalId(), upgradeSubscription.getSubscription().isPurchasingPhoneSupport(), upgradeSubscription.isUpdatedBillingInformation() && upgradeSubscription.isUsingCreditCard(), upgradeSubscription.isUpdatedBillingInformation() && upgradeSubscription.isUsingCreditCard());
		return new UpgradeResponse(costToUpgradeTo(upgradeSubscription), upgradeSubscription.getContractExternalId());
	}

	
	public CurrentSubscription currentSubscriptionFor(Long tenantExternalId) throws CommunicationException {
		return findContractFromFile(tenantExternalId);
	}

	public UpgradeCost costToUpgradeTo(UpgradeSubscription upgradeSubscription) throws CommunicationException {
		
		return new UpgradeCost(1000F, 4000F, "JAN. 18, 2009");
	}

}
