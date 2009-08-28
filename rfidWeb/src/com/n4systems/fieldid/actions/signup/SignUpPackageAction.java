package com.n4systems.fieldid.actions.signup;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.signup.view.model.SignUpPackage;

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
			packages.add(new SignUpPackage("Free"));
			packages.add(new SignUpPackage("Basic"));
			packages.add(new SignUpPackage("Plus"));
			packages.add(new SignUpPackage("Enterprise"));
			packages.add(new SignUpPackage("Unlimited"));
		}
		return packages;
	}
}
