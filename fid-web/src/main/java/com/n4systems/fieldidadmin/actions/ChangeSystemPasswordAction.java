package com.n4systems.fieldidadmin.actions;

import org.apache.log4j.Logger;

import com.n4systems.ejb.ConfigManager;
import com.n4systems.model.user.InsecurePasswordException;
import com.n4systems.model.user.SystemAccountPasswordSynchronizer;
import com.n4systems.security.PasswordComplexityChecker;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class ChangeSystemPasswordAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ChangeSystemPasswordAction.class);

	private ConfigManager configEJBContainer;

	private String pass1;
	private String pass2;

	public String doLoad() {
		return SUCCESS;
	}
	
	public String doSave() {
		int accountsUpdated = 0;
		
		if (!pass1.equals(pass2)) {
			addActionError("Passwords did not match");
			return ERROR;
		}
		
		try {
			
			SystemAccountPasswordSynchronizer passSync = new SystemAccountPasswordSynchronizer(persistenceEJBContainer, configEJBContainer, new PasswordComplexityChecker(8, 1, 1, 1, 1));
			passSync.setNewPassword(pass1);
			accountsUpdated = passSync.synchronize();
			
			// force cache the updated password
			ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD);
			
		} catch(InsecurePasswordException e) {
			addActionError("Password did not meet minimum requirements.  Password must be at least 8 characters, containing 1 upper case, 1 lower case, 1 number and 1 punctuation");
			return ERROR;
		} catch(Exception e) {
			logger.error("Failed to update system passwords", e);
			addActionError("Unable to save update system passwords");
			return ERROR;
		}
		
		addActionMessage(accountsUpdated + " system accounts updated");
		return SUCCESS;
	}

	public void setConfigEJBContainer(ConfigManager configManager) {
		this.configEJBContainer = configManager;
	}

	
	public String getPass1() {
		return pass1;
	}

	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}

	public String getPass2() {
		return pass2;
	}

	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}
	
}
