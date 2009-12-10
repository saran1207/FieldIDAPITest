package com.n4systems.handlers.creator.signup;

import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;

public interface UpgradeAccountHandler {
	public UpgradeCost priceForUpgrade(UpgradeRequest upgradeRequest) throws CommunicationException;

	public UpgradeResponse upgradeTo(UpgradeRequest upgradeRequest, Transaction transaction) throws CommunicationException, UpgradeCompletionException;
}
