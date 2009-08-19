package com.n4systems.fieldid.actions.signup;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.view.model.SignUpPackage;
import com.n4systems.model.tenant.TenantLimit;

public class SignUpPackageAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SignUpPackageAction.class);

	private List<SignUpPackage> packages;
	
	public SignUpPackageAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	public String doList() {
		return SUCCESS;
	}


	public List<SignUpPackage> getPackages() {
		if (packages == null) {
			packages = new ArrayList<SignUpPackage>();
			packages.add(new SignUpPackage(1L, "Free", 0, false, 1L));
			packages.add(new SignUpPackage(2L, "Basic", 40, false, 5L));
			packages.add(new SignUpPackage(3L, "Plus", 100, false, TenantLimit.UNLIMITED));
			packages.add(new SignUpPackage(4L, "Enterprise", 175, true, TenantLimit.UNLIMITED));
			packages.add(new SignUpPackage(5L,  "Unlimited", 225, false, TenantLimit.UNLIMITED));
		}
		return packages;
	}
}
