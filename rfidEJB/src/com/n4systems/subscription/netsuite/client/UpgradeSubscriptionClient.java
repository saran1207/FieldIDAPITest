package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.UpgradeSubscription;
import com.n4systems.subscription.netsuite.model.UpgradeSubscriptionResponse;

public class UpgradeSubscriptionClient extends AbstractNetsuiteClient<UpgradeSubscriptionResponse> {

	private UpgradeSubscription upgradeSubscription;
	
	public UpgradeSubscriptionClient() {
		super(UpgradeSubscriptionResponse.class, "upgradesubscription");
	}
	
	@Override
	protected void addRequestParameters() {
		addUpgradeRequestParameters();
		addUpgradePriceOnlyFlag();
		addSubscriptionRequestParameters(upgradeSubscription.getSubscription());
	}

	protected void addUpgradePriceOnlyFlag() {
		String doNotUpgradeRespondWithPriceOnly = "F"; 
		if (overrideShowPriceFlag()) {
			doNotUpgradeRespondWithPriceOnly = "F";
		}
		addRequestParameter("showpriceonly", doNotUpgradeRespondWithPriceOnly);  
	}

	private boolean overrideShowPriceFlag() {
		return System.getProperty(TESTING_FLAG_PROPERTY) == null || System.getProperty(TESTING_FLAG_PROPERTY).equalsIgnoreCase("T");
	}

	private void addUpgradeRequestParameters() {
		addRequestParameter("tenantid", upgradeSubscription.getTenantExternalId().toString());
		if (upgradeSubscription.getContractExternalId() != null) {
			addRequestParameter("itemid", upgradeSubscription.getContractExternalId().toString());
		}
		addRequestParameter("newusers", upgradeSubscription.getNewUsers().toString());
		addRequestParameter("storageinc", upgradeSubscription.getStorageIncrement().toString());
		addRequestParameter("phonesupport", upgradeSubscription.getSubscription().isPurchasingPhoneSupport() ? "T" : "F");
	}

	private void addSubscriptionRequestParameters(Subscription subscription) {
		 
		if (subscription != null) {
			addRequestParameter("months", subscription.getMonths().toString());
			addRequestParameter("frequency", subscription.getFrequency().getCode());
			applyBillingInformation();
		}
	}

	protected void applyBillingInformation() {
		if (upgradeSubscription.isUpdatedBillingInformation()) {
			addRequestParameter("prefercc", upgradeSubscription.isUsingCreditCard() ? "T" : "F");			
			if (upgradeSubscription.isUsingCreditCard()) {
				CreditCard creditCard = upgradeSubscription.getCreditCard();
				addRequestParameter("ccnumber", creditCard.getNumber());
				addRequestParameter("cctype", creditCard.getType().getCode());
				addRequestParameter("ccexp", creditCard.getExpiry());
				addRequestParameter("ccname", creditCard.getName());
			} else {
				addRequestParameter("ponumber", upgradeSubscription.getPurchaseOrderNumber());
			}
		}
	}

	public void setUpgradeSubscription(UpgradeSubscription upgradeSubscription) {
		this.upgradeSubscription = upgradeSubscription;
	}
}
