package com.n4systems.fieldid.actions.webStoreSSO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.orgs.PrimaryOrg;

public class WebStoreSSOAction extends AbstractAction {
	private static Logger logger = Logger.getLogger(WebStoreSSOAction.class);
	private static final long serialVersionUID = 1L;
	
	private final String STORE_URL = "https://checkout.netsuite.com/s.nl?c=723761&sc=10&login=T&reset=T&redirect_count=1&did_javascript_redirect=T";
	
	private String externalAuthKey;
	private String callback;
	private String redirectUrl;
	private ExternalCredentials externalCredentials = new ExternalCredentials();
	
	public WebStoreSSOAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doRedirectToStore() {
		externalAuthKey = UUID.randomUUID().toString();
		getSessionUser().setExternalAuthKey(externalAuthKey);
		
		try {
			redirectUrl = STORE_URL + "&externalAuthKey="+URLEncoder.encode(externalAuthKey,"UTF-8");
			redirectUrl += "&baseDomain="+URLEncoder.encode(getBaseBrandedUrl(getTenant().getName()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Problem generating web store url",e);
		}
				
		return SUCCESS;
	}
	
	public String doCheckExternalAuthKey() {
		if (isExternalAuthKeyValid()) {
			PrimaryOrg primaryOrg = getPrimaryOrg();
			externalCredentials.setUserName(primaryOrg.getExternalUserName());
			externalCredentials.setPassword(primaryOrg.getExternalPassword());			
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
