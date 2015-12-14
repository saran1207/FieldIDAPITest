package com.n4systems.util;

import com.n4systems.services.config.ConfigService;

import java.util.HashMap;
import java.util.Map;

public class ConfigContextOverridableTestDouble extends ConfigService {

	private final Map<ConfigEntry, Object> testValues;

	public ConfigContextOverridableTestDouble() {
		super();
		testValues = new HashMap<>();
		addConfigurationValue(ConfigEntry.REPORTING_MAX_REPORTS_PER_FILE, 10);
		addConfigurationValue(ConfigEntry.MAIL_MANAGER, "com.n4systems.mail.SMTPMailManager");
		addConfigurationValue(ConfigEntry.MAIL_AUTH_USER, "");
		addConfigurationValue(ConfigEntry.MAIL_AUTH_PASS, "");
		addConfigurationValue(ConfigEntry.MAIL_FROM_ADDR, "\"Field ID\" <noreply@fieldid.com>");
		addConfigurationValue(ConfigEntry.MAIL_REPLY_TO, "");
		addConfigurationValue(ConfigEntry.MAIL_HOST, "localhost");
		addConfigurationValue(ConfigEntry.MAIL_PORT, 25);
		addConfigurationValue(ConfigEntry.MAIL_SUBJECT_PREFIX, "");
		addConfigurationValue(ConfigEntry.MAIL_BODY_PLAIN_HEADER, "");
		addConfigurationValue(ConfigEntry.MAIL_BODY_PLAIN_FOOTER, "\n\n---\nThe Field ID Team\n\n111 Queen Street East\nSuite 240\nToronto, Ontario, Canada, M5C 1S6\ncontact@fieldid.com\n\n\n----\nThis e-mail address is not a valid return address and is not monitored - for questions please email support@fieldid.com\n----\n");
		addConfigurationValue(ConfigEntry.MAIL_BODY_HTML_HEADER, "");
		addConfigurationValue(ConfigEntry.MAIL_BODY_HTML_FOOTER, "<br/><br/>---<br/>\nThe Field ID Team<br/><br/>\n111 Queen Street East<br>\nSuite 240<br/>\nToronto, Ontario, Canada, M5C 1S6<br/>\ncontact@fieldid.com<br/>\n<hr/>\nThis e-mail address is not a valid return address and is not monitored - for questions please email support@fieldid.com\n<hr/>\n");
		addConfigurationValue(ConfigEntry.SALES_ADDRESS, "sales@fieldid.com");
		addConfigurationValue(ConfigEntry.GRAPHING_CHART_SIZE_X, 400);
		addConfigurationValue(ConfigEntry.GRAPHING_CHART_SIZE_Y, 200);
		addConfigurationValue(ConfigEntry.GRAPHING_CHART_PEAK_MARKERS, false);
		addConfigurationValue(ConfigEntry.GRAPHING_CHART_PEAK_ARROWS, true);
		addConfigurationValue(ConfigEntry.GRAPHING_CHART_PEAK_DOTS, true);
		addConfigurationValue(ConfigEntry.MOBILE_PAGESIZE_PRODUCTS, 500);
		addConfigurationValue(ConfigEntry.MOBILE_PAGESIZE_INSPECTIONS, 500);
		addConfigurationValue(ConfigEntry.MOBLIE_PAGESIZE_SETUPDATA, 500);
		addConfigurationValue(ConfigEntry.WEB_TOTAL_INSPECTION_BUTTONS, 14);
		addConfigurationValue(ConfigEntry.WEB_FILE_UPLOAD_MAX, 50);
		addConfigurationValue(ConfigEntry.PRODUCT_FILE_UPLOAD_MAX, 5);
		addConfigurationValue(ConfigEntry.WEB_PAGINATION_PAGE_SIZE, 20);
		addConfigurationValue(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME, "*");
		addConfigurationValue(ConfigEntry.CATALOG_IMPORTER_PAGE_SIZE, 100);
		addConfigurationValue(ConfigEntry.ESTIMATED_CATALOG_IMPORT_TIME_IN_MINUTES, 10);
		addConfigurationValue(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, 6000);
		addConfigurationValue(ConfigEntry.MASS_ACTIONS_LIMIT, 250L);
		addConfigurationValue(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, 400);
		addConfigurationValue(ConfigEntry.DEFAULT_EXECUTOR_POOL_SIZE, 4);
		addConfigurationValue(ConfigEntry.MINIMUM_MOBILE_MINOR_VERSION, 11);
		addConfigurationValue(ConfigEntry.MINIMUM_MOBILE_MAJOR_VERSION, 1);
		addConfigurationValue(ConfigEntry.CURRENT_MOBILE_MAJOR_VERSION, 1);
		addConfigurationValue(ConfigEntry.CURRENT_MOBILE_MINOR_VERSION, 12);
		addConfigurationValue(ConfigEntry.CURRENT_MOBILE_BUILD_VERSION, 0);
		addConfigurationValue(ConfigEntry.CURRENT_MOBILE_FILE_NAME, "Field ID Mobile.CAB");
		addConfigurationValue(ConfigEntry.MAX_MULTI_ADD_SIZE, 250);
		addConfigurationValue(ConfigEntry.UPLOAD_FILE_SIZE_LIMIT_DEFAULT_IN_KB, 5120);
		addConfigurationValue(ConfigEntry.UPLOAD_FILE_SIZE_LIMIT_PRODUCT_ATTACHMENT_IN_KB, 2048);
		addConfigurationValue(ConfigEntry.UPLOAD_FILE_SIZE_LIMIT_PRODUCT_TYPE_IMAGE_IN_KB, 500);
		addConfigurationValue(ConfigEntry.USAGE_BASED_EVENT_COUNT_THRESHOLD, 500);
		addConfigurationValue(ConfigEntry.AMAZON_ACCESS_KEY_ID, "AKIAJYAZBOFFRZAZSIGA");
		addConfigurationValue(ConfigEntry.AMAZON_SECRET_ACCESS_KEY, "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");
		addConfigurationValue(ConfigEntry.AMAZON_S3_ENDPOINT, "s3.amazonaws.com");
		addConfigurationValue(ConfigEntry.AMAZON_S3_BUCKET, "fieldid_dev");
		addConfigurationValue(ConfigEntry.AMAZON_S3_LOTO_REPORTS, "dev");
		addConfigurationValue(ConfigEntry.AMAZON_S3_UPLOAD_TIMEOUT_MILLISECONDS, 120000);
		addConfigurationValue(ConfigEntry.AMAZON_S3_UPLOAD_MAX_FILE_SIZE_BYTES, 10485760);
		addConfigurationValue(ConfigEntry.ACTIVE_SESSION_TIME_OUT, 30);
		addConfigurationValue(ConfigEntry.FIELDID_ADMINISTRATOR_EMAIL, "team@fieldid.com");
		addConfigurationValue(ConfigEntry.SYSTEM_USER_USERNAME, "n4systems");
		addConfigurationValue(ConfigEntry.SYSTEM_USER_PASSWORD, "7c4fd0a53d8aa0d0f15f716cc6324e95223d9902");
		addConfigurationValue(ConfigEntry.SYSTEM_USER_ADDRESS, "admin@fieldid.com");
		addConfigurationValue(ConfigEntry.UNBRANDED_SUBDOMAIN, "www");
		addConfigurationValue(ConfigEntry.GLOBAL_APPLICATION_ROOT, "/var/fieldid");
		addConfigurationValue(ConfigEntry.DEFAULT_TIMEZONE_ID, "United States:New York - New York");
		addConfigurationValue(ConfigEntry.SYSTEM_DOMAIN, "fieldid.com");
		addConfigurationValue(ConfigEntry.HOUSE_ACCOUNT_NAME, "fieldid");
		addConfigurationValue(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID, 15511550);
		addConfigurationValue(ConfigEntry.DOWNLOAD_TTL_DAYS, 2);
		addConfigurationValue(ConfigEntry.SAFETY_NETWORK_HELP_URL, "http://help.fieldid.com/The-Safety-Network-Overview.html");
		addConfigurationValue(ConfigEntry.SAFETY_NETWORK_VIDEO_URL, "http://www.n4systems.com/qwe__wqe/training/safety_network_invite/");
		addConfigurationValue(ConfigEntry.SYSTEM_PROTOCOL, "https");
		addConfigurationValue(ConfigEntry.EXTERNAL_PLANS_AND_PRICING_URL, "http://www.n4systems.com/pricing/");
		addConfigurationValue(ConfigEntry.MAX_SERIALS_PER_PROOFTEST, 50);
		addConfigurationValue(ConfigEntry.HOUR_TO_RUN_EVENT_SCHED_NOTIFICATIONS, 0);
		addConfigurationValue(ConfigEntry.HELP_SYSTEM_URL, "http://n4systems.helpserve.com/index.php?/Tickets/Submit/");
		addConfigurationValue(ConfigEntry.RSS_FEED, "http://feeds.feedburner.com/FieldIdCustomerBlog");
		addConfigurationValue(ConfigEntry.GOOGLE_ANALYTICS_ENABLED, false);
		addConfigurationValue(ConfigEntry.APPTEGIC_ENABLED, false);
		addConfigurationValue(ConfigEntry.APPTEGIC_DATASET, "test");
		addConfigurationValue(ConfigEntry.FIELDID_SALES_MANAGERS_EMAIL, "salesmanagers@fieldid.com");
		addConfigurationValue(ConfigEntry.CUSTOM_JS, "");
		addConfigurationValue(ConfigEntry.ASSET_INDEX_ENABLED, true);
		addConfigurationValue(ConfigEntry.ASSET_INDEX_SIZE, 50);
		addConfigurationValue(ConfigEntry.TRENDS_INDEX_ENABLED, true);
		addConfigurationValue(ConfigEntry.TRENDS_INDEX_SIZE, 50);
		addConfigurationValue(ConfigEntry.MIXPANEL_ENABLED, false);
		addConfigurationValue(ConfigEntry.MIXPANEL_TOKEN, "4c3e64a8211844a634f163b8b1eb5844");
		addConfigurationValue(ConfigEntry.EVENT_INDEX_ENABLED, true);
		addConfigurationValue(ConfigEntry.EVENT_INDEX_SIZE, 50);
		addConfigurationValue(ConfigEntry.HEADER_SCRIPT, "");
		addConfigurationValue(ConfigEntry.FOOTER_SCRIPT, "");
		addConfigurationValue(ConfigEntry.STRUTS_HEADER_SCRIPT, "");
		addConfigurationValue(ConfigEntry.STRUTS_FOOTER_SCRIPT, "");
	}

	@Override
	protected Object getValue(ConfigEntry entry, Long tenantId) {
		return testValues.get(entry);
	}

	@Override
	public synchronized void reloadConfigurations() {

	}

	public void addConfigurationValue(ConfigEntry entry, Object value) {
		testValues.put(entry, value);
	}

}
