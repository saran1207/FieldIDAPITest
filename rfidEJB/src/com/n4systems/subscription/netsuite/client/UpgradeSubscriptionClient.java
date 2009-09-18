package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.netsuite.model.NetsuiteSubscription;
import com.n4systems.subscription.netsuite.model.UpgradeSubscription;
import com.n4systems.subscription.netsuite.model.UpgradeSubscriptionResponse;

public class UpgradeSubscriptionClient extends AbstractNetsuiteClient<UpgradeSubscriptionResponse> {

	private UpgradeSubscription upgradeSubscription;
	
	public UpgradeSubscriptionClient() {
		super(UpgradeSubscriptionResponse.class, "upgradesubscription");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("tenantid", upgradeSubscription.getTenantExternalId().toString());
		addRequestParameter("itemid", upgradeSubscription.getContractExternalId().toString());
		addRequestParameter("newusers", upgradeSubscription.getNewUsers().toString());
		addRequestParameter("storageinc", upgradeSubscription.getStorageIncrement().toString());
		addRequestParameter("showpriceonly", upgradeSubscription.isShowPriceOnly() ? "T" : "F");
		
		NetsuiteSubscription subscription = upgradeSubscription.getSubscription(); 
		if (subscription != null) {
			addRequestParameter("months", subscription.getMonths().toString());
			addRequestParameter("frequency", subscription.getFrequency().getCode());
			addRequestParameter("prefercc", upgradeSubscription.isUsingCreditCard() ? "T" : "F");			
			if (upgradeSubscription.isUsingCreditCard()) {
				CreditCard creditCard = upgradeSubscription.getCreditCard();
				addRequestParameter("ccnumber", creditCard.getName());
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
