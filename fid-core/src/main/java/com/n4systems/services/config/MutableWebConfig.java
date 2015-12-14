package com.n4systems.services.config;

public class MutableWebConfig extends WebConfig {

	public void setGoogleAnalyticsEnabled(Boolean googleAnalyticsEnabled) {
		this.googleAnalyticsEnabled = googleAnalyticsEnabled;
	}

	public void setCustomJs(String customJs) {
		this.customJs = customJs;
	}

	public void setHeaderScript(String headerScript) {
		this.headerScript = headerScript;
	}

	public void setFooterScript(String footerScript) {
		this.footerScript = footerScript;
	}

	public void setStrutsHeaderScript(String strutsHeaderScript) {
		this.strutsHeaderScript = strutsHeaderScript;
	}

	public void setStrutsFooterScript(String strutsFooterScript) {
		this.strutsFooterScript = strutsFooterScript;
	}

	public void setApptegicEnabled(Boolean apptegicEnabled) {
		this.apptegicEnabled = apptegicEnabled;
	}

	public void setApptegicDataset(String apptegicDataset) {
		this.apptegicDataset = apptegicDataset;
	}

	public void setMixpanelEnabled(Boolean mixpanelEnabled) {
		this.mixpanelEnabled = mixpanelEnabled;
	}

	public void setMixpanelToken(String mixpanelToken) {
		this.mixpanelToken = mixpanelToken;
	}

	public void setExternalPlansAndPricingUrl(String externalPlansAndPricingUrl) {
		this.externalPlansAndPricingUrl = externalPlansAndPricingUrl;
	}

	public void setHelpSystemUrl(String helpSystemUrl) {
		this.helpSystemUrl = helpSystemUrl;
	}

	public void setRssFeed(String rssFeed) {
		this.rssFeed = rssFeed;
	}

	public void setSafetyNetworkHelpUrl(String safetyNetworkHelpUrl) {
		this.safetyNetworkHelpUrl = safetyNetworkHelpUrl;
	}

	public void setSafetyNetworkVideoUrl(String safetyNetworkVideoUrl) {
		this.safetyNetworkVideoUrl = safetyNetworkVideoUrl;
	}

}
