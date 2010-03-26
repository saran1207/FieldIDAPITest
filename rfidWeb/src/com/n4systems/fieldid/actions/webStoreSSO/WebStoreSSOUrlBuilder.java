package com.n4systems.fieldid.actions.webStoreSSO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public class WebStoreSSOUrlBuilder {

	public final static String STORE_URL = "https://checkout.netsuite.com/s.nl?c=723761&sc=10&login=T&reset=T&redirect_count=1&did_javascript_redirect=T";
	private String baseDomain;
	private UUID authKey;
	private String namespacedActionName;

	public String build() {
		return WebStoreSSOUrlBuilder.STORE_URL + 
			"&externalAuthKey=" + encode(authKey.toString()) +
			"&baseDomain=" + encode(baseDomain) +
			"&targetAction=" + encode(namespacedActionName);
		
	}


	public WebStoreSSOUrlBuilder withBaseDomain(String baseDomain) {
		this.baseDomain = baseDomain;
		return this;
	}
	
	private String encode(String toEncode) {
		try {
			return URLEncoder.encode(toEncode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("UTF-8, is no longer an encoding in our url encoder", e);
		}
	}


	public WebStoreSSOUrlBuilder authKey(UUID authKey) {
		
		this.authKey = authKey;
		return this;
	}


	public WebStoreSSOUrlBuilder targetAction(String namespacedActionName) {
		this.namespacedActionName = namespacedActionName;
		return this;
		
	}

}
