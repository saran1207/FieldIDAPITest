package com.n4systems.handlers.creator.signup;

import com.n4systems.persistence.Transaction;

public interface UpgradeHandler {

	public void upgradeTo(UpgradeRequest upgradeRequst, Transaction transaction);

}