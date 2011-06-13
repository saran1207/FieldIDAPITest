package com.n4systems.model.user;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.security.PasswordComplexityChecker;
import com.n4systems.security.PasswordValidator;
import com.n4systems.security.UserType;
import com.n4systems.util.ConfigEntry;

public class SystemAccountPasswordSynchronizer {
	private final PersistenceManager pm;
	private final ConfigManager cm;
	private final PasswordValidator passValidator;
	private String newPassword;

	public SystemAccountPasswordSynchronizer(PersistenceManager pm, ConfigManager cm) {
		this(pm, cm, null);
	}

	public SystemAccountPasswordSynchronizer(PersistenceManager pm, ConfigManager cm, PasswordValidator passValidator) {
		this.pm = pm;
		this.cm = cm;

		if (passValidator == null) {
			passValidator = PasswordComplexityChecker.createDefault();
		}
		this.passValidator = passValidator;
	}

	public int synchronize() throws InsecurePasswordException {
		// check that our password passes validation tests
		if (!passValidator.isValid(newPassword)) {
			throw new InsecurePasswordException("Password did not meet minimum requirements");
		}

		// hash the password as we will be setting it directly on the user
		String hashPassword = User.hashPassword(newPassword);

		// save the hash password against the global config so that it is
		// updated for new users
		cm.saveEntry(ConfigEntry.SYSTEM_USER_PASSWORD, hashPassword);

		// construct our update statement
		String update = "UPDATE " + User.class.getName() + " SET hashPassword = :hashPassword WHERE usertype = '" + UserType.SYSTEM + "'";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("hashPassword", hashPassword);

		// exeucte the update
		return pm.executeUpdate(update, param);
	}

	public void setNewPassword(String pass) {
		this.newPassword = pass;
	}

}
