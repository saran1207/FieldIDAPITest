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
	MAIL_BODY_PLAIN_FOOTER						(	"\n\n---\nThe Field ID Team\n\n111 Queen Street East\n Suite 240 \nToronto, Ontario, Canada, M5C 1S6\ncontact@fieldid.com" +
													"\n\n----\nThis e-mail address is not a valid return address and is not monitored - for questions please email support@fieldid.com\n----"),
	MAIL_BODY_HTML_HEADER						(	""						),
	MAIL_BODY_HTML_FOOTER						(	"<br/><br/>---<br/>The Field ID Team<br/><br/>111 Queen Street East <br>Suite 240<br/>" +
													"Toronto, Ontario, Canada, M5C 1S6<br/>contact@fieldid.com<br/><hr/>This e-mail address is not a valid return address and is not monitored - for questions please email support@fieldid.com</hr>"),
	SALES_ADDRESS								(	"sales@fieldid.com"		),										
	GRAPHING_CHART_SIZE_X						(	"400"					),
	GRAPHING_CHART_SIZE_Y						(	"200"					),
	GRAPHING_CHART_PEAK_MARKERS					(	"false"					),
	GRAPHING_CHART_PEAK_ARROWS					(	"true"					),
	GRAPHING_CHART_PEAK_DOTS					(	"true"					),
	MOBILE_PAGESIZE_PRODUCTS					(	"500"					),
	MOBILE_PAGESIZE_INSPECTIONS					(	"500"					),
	MOBLIE_PAGESIZE_SETUPDATA					(	"500"					),
	WEB_TOTAL_INSPECTION_BUTTONS				(	"14"					),
	WEB_FILE_UPLOAD_MAX							(	"50"					),
	PRODUCT_FILE_UPLOAD_MAX						(	"5"						),
	WEB_PAGINATION_PAGE_SIZE					(	"20"					),
	INTEGRATION_PRODUCTTYPE_RESOLVES_BY_CODE	(	"true"					),
	INDEX_JOB_PAGE_SIZE							(	"128"					),
	INDEX_JOB_WORKERS							(	"2"						),
	DEFAULT_PRODUCT_TYPE_NAME					(	"*"						),
	CATALOG_IMPORTER_PAGE_SIZE					(	"100"					),
	ESTIMATED_CATALOG_IMPORT_TIME_IN_MINUTES	(   "10"                    ),
	MAX_SIZE_FOR_SUMMARY_REPORT					(   "6000"					),
	MASS_ACTIONS_LIMIT			        		(	"250"					),
    MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS	(	"400"					),
	DEFAULT_EXECUTOR_POOL_SIZE					(	"4"						),
	MINIMUM_MOBILE_MINOR_VERSION                (   "11"                    ),
	MINIMUM_MOBILE_MAJOR_VERSION                (   "1"                     ),
	CURRENT_MOBILE_MAJOR_VERSION                (   "1"                     ),
	CURRENT_MOBILE_MINOR_VERSION                (   "12"                    ),
	CURRENT_MOBILE_BUILD_VERSION                (   "0"                     ),
	CURRENT_MOBILE_FILE_NAME                    (   "Field ID Mobile.CAB"   ),
	MAX_MULTI_ADD_SIZE							(	"250"					),
	UPLOAD_FILE_SIZE_LIMIT_DEFAULT_IN_KB			(	"5120"				),
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_ATTACHMENT_IN_KB	(	"2048"				), 
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_TYPE_IMAGE_IN_KB (	"500"				),
    USAGE_BASED_EVENT_COUNT_THRESHOLD           (	"500"			    	),

    AMAZON_ACCESS_KEY_ID                        (   "AKIAJYAZBOFFRZAZSIGA"),
    AMAZON_SECRET_ACCESS_KEY                    (   "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"),
    AMAZON_S3_SERVER_HOSTNAME                   (   "s3.amazonaws.com"), //only overwritten if a tenant can't use amazon s3
    AMAZON_S3_BUCKET                            (   "fieldid_dev"),      //overwritten depending on environment
    AMAZON_S3_UPLOAD_TIMEOUT_MILLISECONDS       (   "120000"),
    AMAZON_S3_UPLOAD_MAX_FILE_SIZE_BYTES        (   "10485760"),
	
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
	SAFETY_NETWORK_HELP_URL						(	"http://help.fieldid.com/The-Safety-Network-Overview.html"	),
	SAFETY_NETWORK_VIDEO_URL					(	"http://www.n4systems.com/qwe__wqe/training/safety_network_invite/"		),
	SYSTEM_PROTOCOL								(	"https"					),
	EXTERNAL_PLANS_AND_PRICING_URL				(	"http://www.n4systems.com/pricing/"	),
	MAX_SERIALS_PER_PROOFTEST					(	"50"					),
    HOUR_TO_RUN_EVENT_SCHED_NOTIFICATIONS       (	"0"	),
	HELP_SYSTEM_URL                             (   "http://n4systems.helpserve.com/index.php?/Tickets/Submit/"),
	RSS_FEED                                    (   "http://feeds.feedburner.com/FieldIdCustomerBlog" ),
    GOOGLE_ANALYTICS_ENABLED                    (   "false" ),
    APPTEGIC_ENABLED                            (   "false" ),
    APPTEGIC_DATASET                            (   "test" ),
    FIELDID_SALES_MANAGERS_EMAIL                (   "salesmanagers@fieldid.com"),
    CUSTOM_JS                                   (   ""),

    ASSET_INDEX_ENABLED                         (   "true"),
    ASSET_INDEX_SIZE                            (   "50"),

    TRENDS_INDEX_ENABLED                         (   "true"),
    TRENDS_INDEX_SIZE                            (   "50"),

    MIXPANEL_ENABLED                            ( "false" ),
    MIXPANEL_TOKEN                              ( "4c3e64a8211844a634f163b8b1eb5844" ),
    EVENT_INDEX_ENABLED                         (   "true"),
    EVENT_INDEX_SIZE                            (   "50");

	
	private String defaultValue;
	
	ConfigEntry(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
}
