package com.n4systems.util;


public enum ConfigEntry {
	
	REPORTING_MAX_REPORTS_PER_FILE				(	"10"					),
	MAIL_MANAGER								( 	"com.n4systems.mail.SMTPMailManager" ),
	MAIL_AUTH_USER								(	""						),
	MAIL_AUTH_PASS								(	""						),
	MAIL_FROM_ADDR								(	"\"Field ID\" <noreply@fieldid.com>"),
	MAIL_REPLY_TO								(	""						),
	MAIL_HOST									(	"localhost"				),
    MAIL_PORT									(	"25"				),
	MAIL_SUBJECT_PREFIX							(	""				),
	MAIL_BODY_PLAIN_HEADER						(	""						),
	MAIL_BODY_PLAIN_FOOTER						(	"\n\n---\nThe Field ID Team\n\n49 Fraser Street, Ground Floor\nToronto, Ontario, Canada, M6K1Y7\ncontact@fieldid.com" +
													"\n\n----\nThis e-mail address is not a valid return address and is not monitored - for questions please email support@fieldid.com\n----"),
	MAIL_BODY_HTML_HEADER						(	""						),
	MAIL_BODY_HTML_FOOTER						(	"<br/><br/>---<br/>The Field ID Team<br/><br/>49 Fraser Street, Ground Floor<br/>" +
													"Toronto, Ontario, Canada, M6K1Y7<br/>contact@fieldid.com<br/><hr/>This e-mail address is not a valid return address and is not monitored - for questions please email support@fieldid.com</hr>"),
	SALES_ADDRESS								(	"sales@fieldid.com"		),										
	GRAPHING_CHART_SIZE_X						(	"400"					),
	GRAPHING_CHART_SIZE_Y						(	"200"					),
	GRAPHING_CHART_PEAK_MARKERS					(	"false"					),
	GRAPHING_CHART_PEAK_ARROWS					(	"true"					),
	GRAPHING_CHART_PEAK_DOTS					(	"true"					),
	MOBILE_PAGESIZE_PRODUCTS					(	"500"					),
	MOBILE_PAGESIZE_INSPECTIONS					(	"500"					),
	MOBLIE_PAGESIZE_SETUPDATA					(	"500"					),
	WEB_TOTAL_INSPECTION_BUTTONS				(	"3"						),
	WEB_FILE_UPLOAD_MAX							(	"50"					),
	PRODUCT_FILE_UPLOAD_MAX						(	"2"						),
	WEB_PAGINATION_PAGE_SIZE					(	"20"					),
	INTEGRATION_PRODUCTTYPE_RESOLVES_BY_CODE	(	"true"					),
	INDEX_JOB_PAGE_SIZE							(	"128"					),
	INDEX_JOB_WORKERS							(	"2"						),
	DEFAULT_PRODUCT_TYPE_NAME					(	"*"						),
	CATALOG_IMPORTER_PAGE_SIZE					(	"100"					),
	ESTIMATED_CATALOG_IMPORT_TIME_IN_MINUTES	(   "10"                    ),
	MAX_SIZE_FOR_EXCEL_EXPORT					(   "10000"					),
	MAX_SIZE_FOR_PDF_PRINT_OUTS					(	"10000"					),
	MAX_SIZE_FOR_SUMMARY_REPORT					(   "6000"					),
	MAX_SIZE_FOR_MASS_UPDATE					(	"1000"					),
	MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS	(	"400"					),
	DEFAULT_EXECUTOR_POOL_SIZE					(	"4"						),
	MINIMUM_MOBILE_MINOR_VERSION                (   "11"                    ),
	MINIMUM_MOBILE_MAJOR_VERSION                (   "1"                     ),
	CURRENT_MOBILE_MAJOR_VERSION                (   "1"                     ),
	CURRENT_MOBILE_MINOR_VERSION                (   "12"                    ),
	CURRENT_MOBILE_BUILD_VERSION                (   "0"                     ),
	CURRENT_MOBILE_FILE_NAME                    (   "Field ID Mobile.CAB"   ),
	MAX_SIZE_FOR_MULTI_INSPECT					(	"250"					),
	MAX_MULTI_ADD_SIZE							(	"250"					),
	UPLOAD_FILE_SIZE_LIMIT_DEFAULT_IN_KB			(	"5120"				),
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_ATTACHMENT_IN_KB	(	"2048"				), 
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_TYPE_IMAGE_IN_KB (	"500"				),
	
	
	// ones below here are never overridden by the tenant.
	ACTIVE_SESSION_TIME_OUT						(	"30"				),
	FIELDID_ADMINISTRATOR_EMAIL					(	"team@fieldid.com"	),
	SYSTEM_USER_USERNAME						(	"n4systems"			),
	SYSTEM_USER_PASSWORD						(	"7c4fd0a53d8aa0d0f15f716cc6324e95223d9902"	),
	SYSTEM_USER_ADDRESS							(	"admin@fieldid.com"		),
	UNBRANDED_SUBDOMAIN							(	"www"										),
	GLOBAL_APPLICATION_ROOT						(	"/var/fieldid"			),
	DEFAULT_TIMEZONE_ID							(	"United States:New York - New York"			),
	SYSTEM_DOMAIN								(   "fieldid.com"			),
	SUBSCRIPTION_AGENT							(	"com.n4systems.subscription.netsuite.NetSuiteSubscriptionAgent"),
	HOUSE_ACCOUNT_NAME							(	"fieldid"				),
	HOUSE_ACCOUNT_PRIMARY_ORG_ID				(	"15511550"				),
	DOWNLOAD_TTL_DAYS							(	"2"						),
	SIGNUP_PATH									(	"/signup"				),
	SAFETY_NETWORK_HELP_URL						(	"https://www.fieldid.com/fieldid_help/?Safety_Network_Overview.html"	),
	SAFETY_NETWORK_VIDEO_URL					(	"http://www.n4systems.com/qwe__wqe/training/safety_network_invite/"		),
	SYSTEM_PROTOCOL								(	"https"					),
	EXTERNAL_PLANS_AND_PRICING_ENABLED			(	"true" 					),
	EXTERNAL_PLANS_AND_PRICING_URL				(	"http://www.n4systems.com/pricing/"	),
	MAX_SERIALS_PER_PROOFTEST					(	"50"					),
    HOUR_TO_RUN_EVENT_SCHED_NOTIFICATIONS       (	"0"	),
	HELP_SYSTEM_URL                             (   "http://n4systems.helpserve.com/index.php?/Tickets/Submit/"),
	CLICKTAIL_START                             (   "<script type=\"text/javascript\">var WRInitTime=(new Date()).getTime();</script>"),
	CLICKTAIL_END                               (   "<div id=\"ClickTaleDiv\" style=\"display: none;\"></div>\n<script type='text/javascript'>\ndocument.write(unescape(\"%3Cscript%20src='\"+\n(document.location.protocol=='https:'?\n  'https://clicktale.pantherssl.com/':\n  'http://s.clicktale.net/')+\n \"WRb.js'%20type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\" src=\"[LOCALLY HOSTED FetchFromWithCookies FILE URL]\"></script>\n<script type=\"text/javascript\">\nFetchFromWithCookies.setFromCookie(\"JSESSIONID\"); \nClickTaleFetchFrom = FetchFromWithCookies.constructFetchFromUrl();\nvar ClickTaleSSL=1;\nif(typeof ClickTale=='function') ClickTale(12764,0.15,\"www03\");\n</script>");
	
	private String defaultValue;
	
	ConfigEntry(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
}
