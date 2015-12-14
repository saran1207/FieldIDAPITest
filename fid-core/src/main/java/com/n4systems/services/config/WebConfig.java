package com.n4systems.services.config;

public class WebConfig {
	protected Boolean googleAnalyticsEnabled;
	protected String customJs;
	protected String headerScript;
	protected String footerScript;
	protected String strutsHeaderScript;
	protected String strutsFooterScript;
	protected Boolean apptegicEnabled;
	protected String apptegicDataset;
	protected Boolean mixpanelEnabled;
	protected String mixpanelToken;
	protected String externalPlansAndPricingUrl;
	protected String helpSystemUrl;
	protected String rssFeed;
	protected String safetyNetworkHelpUrl;
	protected String safetyNetworkVideoUrl;

	public WebConfig() {}

	public WebConfig(WebConfig other) {
		this.googleAnalyticsEnabled = other.googleAnalyticsEnabled;
		this.customJs = other.customJs;
		this.headerScript = other.headerScript;
		this.footerScript = other.footerScript;
		this.strutsHeaderScript = other.strutsHeaderScript;
		this.strutsFooterScript = other.strutsFooterScript;
		this.apptegicEnabled = other.apptegicEnabled;
		this.apptegicDataset = other.apptegicDataset;
		this.mixpanelEnabled = other.mixpanelEnabled;
		this.mixpanelToken = other.mixpanelToken;
		this.externalPlansAndPricingUrl = other.externalPlansAndPricingUrl;
		this.helpSystemUrl = other.helpSystemUrl;
		this.rssFeed = other.rssFeed;
		this.safetyNetworkHelpUrl = other.safetyNetworkHelpUrl;
		this.safetyNetworkVideoUrl = other.safetyNetworkVideoUrl;
	}

	public Boolean getGoogleAnalyticsEnabled() {
		return googleAnalyticsEnabled;
	}

	public String getCustomJs() {
		return customJs;
	}

	public String getHeaderScript() {
		return headerScript;
	}

	public String getFooterScript() {
		return footerScript;
	}

	public String getStrutsHeaderScript() {
		return strutsHeaderScript;
	}

	public String getStrutsFooterScript() {
		return strutsFooterScript;
	}

	public Boolean getApptegicEnabled() {
		return apptegicEnabled;
	}

	public String getApptegicDataset() {
		return apptegicDataset;
	}

	public Boolean getMixpanelEnabled() {
		return mixpanelEnabled;
	}

	public String getMixpanelToken() {
		return mixpanelToken;
	}

	public String getExternalPlansAndPricingUrl() {
		return externalPlansAndPricingUrl;
	}

	public String getHelpSystemUrl() {
		return helpSystemUrl;
	}

	public String getRssFeed() {
		return rssFeed;
	}

	public String getSafetyNetworkHelpUrl() {
		return safetyNetworkHelpUrl;
	}

	public String getSafetyNetworkVideoUrl() {
		return safetyNetworkVideoUrl;
	}

	@Override
	public String toString() {
		return "\t\tgoogleAnalyticsEnabled: " + googleAnalyticsEnabled + '\n' +
				"\t\tcustomJs: '" + customJs + "'\n" +
				"\t\theaderScript: '" + headerScript + "'\n" +
				"\t\tfooterScript: '" + footerScript + "'\n" +
				"\t\tstrutsHeaderScript: '" + strutsHeaderScript + "'\n" +
				"\t\tstrutsFooterScript: '" + strutsFooterScript + "'\n" +
				"\t\tapptegicEnabled: " + apptegicEnabled + '\n' +
				"\t\tapptegicDataset: '" + apptegicDataset + "'\n" +
				"\t\tmixpanelEnabled: " + mixpanelEnabled + '\n' +
				"\t\tmixpanelToken: '" + mixpanelToken + "'\n" +
				"\t\texternalPlansAndPricingUrl: '" + externalPlansAndPricingUrl + "'\n" +
				"\t\thelpSystemUrl: '" + helpSystemUrl + "'\n" +
				"\t\trssFeed: '" + rssFeed + "'\n" +
				"\t\tsafetyNetworkHelpUrl: '" + safetyNetworkHelpUrl + "'\n" +
				"\t\tsafetyNetworkVideoUrl: '" + safetyNetworkVideoUrl + "'\n";
	}
}
