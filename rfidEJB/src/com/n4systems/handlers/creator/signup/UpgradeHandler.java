package com.n4systems.handlers.creator.signup;

import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.persistence.Transaction;

public interface UpgradeHandler {

	public void upgradeTo(SignUpPackageDetails upgradePackage, Transaction transaction);

}