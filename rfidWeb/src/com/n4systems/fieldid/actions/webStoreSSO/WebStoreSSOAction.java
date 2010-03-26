package com.n4systems.fieldid.actions.webStoreSSO;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.AccessWebStore})
public class WebStoreSSOAction extends AbstractAction {
	private static Logger logger = Logger.getLogger(WebStoreSSOAction.class);
	private static final long serialVersionUID = 1L;
	
	private String externalAuthKey;
	private String callback;
	private String redirectUrl;
	private ExternalCredentials externalCredentials = new ExternalCredentials();
	
	public WebStoreSSOAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doRedirectToStore() {
		UUID authKey = UUID.randomUUID();
		
		getSessionUser().setExternalAuthKey(authKey.toString());
		
		createUrl(authKey);
				
		return SUCCESS;
	}

	private void createUrl(UUID authKey) {
		redirectUrl = new WebStoreSSOUrlBuilder()
			.withBaseDomain(createActionURI(getTenant(), "") + "external/")
			.targetAction("ajax/externalAuthVerification.action")
			.authKey(authKey).build();
	}
	
	public String doCheckExternalAuthKey() {
		if (isExternalAuthKeyValid()) {
			externalCredentials.setCredentials(getPrimaryOrg());
			logger.info(getLogLinePrefix() + " signing into the web store");
		}	else {
			logger.info(getLogLinePrefix() + " did not find correct authkey");
		}
		
		return SUCCESS;
	}
	
	private boolean isExternalAuthKeyValid() {
		return (externalAuthKey != null && externalAuthKey.trim().length() > 0 && 
				getSessionUser().getExternalAuthKey().equals(externalAuthKey));
	}

	public String getExternalAuthKey() {
		return externalAuthKey;
	}

	public void setExternalAuthKey(String externalAuthKey) {
		this.externalAuthKey = externalAuthKey;
	}

	public ExternalCredentials getExternalCredentials() {
		return externalCredentials;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}
}
