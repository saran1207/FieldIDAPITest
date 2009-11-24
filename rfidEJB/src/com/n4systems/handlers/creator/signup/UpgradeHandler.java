package com.n4systems.handlers.creator.signup;

import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.UpgradeCost;

public interface UpgradeHandler {

	public boolean upgradeTo(UpgradeRequest upgradeRequest, Transaction transaction);

	public UpgradeCost priceForUpgrade(UpgradeRequest upgradeRequest) throws CommunicationException;

}