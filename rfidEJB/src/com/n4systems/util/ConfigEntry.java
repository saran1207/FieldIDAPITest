package com.n4systems.util;


public enum ConfigEntry {
	
	REPORTING_MAX_REPORTS_PER_FILE				(	"10"					),
	MAIL_AUTH_USER								(	""						),
	MAIL_AUTH_PASS								(	""						),
	MAIL_FROM_ADDR								(	"\"FieldID\" <noreply@n4systems.com>"),
	MAIL_REPLY_TO								(	""						),
	MAIL_HOST									(	"localhost"				),
	MAIL_SUBJECT_PREFIX							(	"FieldID: "				),
	MAIL_BODY_PLAIN_HEADER						(	""						),
	MAIL_BODY_PLAIN_FOOTER						(	"\n\n---\nThe Field ID Team\n\nsupport@n4systems.com \n179 John Street, Suite 101\n" +
													"Toronto, Ontario, Canada \nM5T1X4 \n\n----\nThis e-mail address as it is not a valid return address and is not monitored - for questions please email support@n4systems.com\n----"),
	MAIL_BODY_HTML_HEADER						(	""						),
	MAIL_BODY_HTML_FOOTER						(	"<br/><br/>---<br/>The Field ID Team<br/><br/>support@n4systems.com <br/>179 John Street, Suite 101<br/>" +
													"Toronto, Ontario, Canada <br/>M5T1X4 <br/><br/><hr/>This e-mail address as it is not a valid return address and is not monitored - for questions please email support@n4systems.com</hr>"),
	MAIL_ATTACHMENT_LIST						(	""						),
	GRAPHING_CHART_SIZE_X						(	"400"					),
	GRAPHING_CHART_SIZE_Y						(	"200"					),
	GRAPHING_CHART_PEAK_MARKERS					(	"false"					),
	GRAPHING_CHART_PEAK_ARROWS					(	"true"					),
	GRAPHING_CHART_PEAK_DOTS					(	"true"					),
	MOBILE_PAGESIZE_PRODUCTS					(	"500"					),
	MOBILE_PAGESIZE_INSPECTIONS					(	"500"					),
	MOBLIE_PAGESIZE_SETUPDATA					(	"500"					),
	WEB_TOTAL_INSPECTION_BUTTONS				(	"3"						),
	WEB_FILE_UPLOAD_MAX							(	"5"						),
	PRODUCT_FILE_UPLOAD_MAX						(	"2"						),
	WEB_PAGINATION_PAGE_SIZE					(	"20"					),
	INTEGRATION_PRODUCTTYPE_RESOLVES_BY_CODE	(	"true"					),
	INDEX_JOB_PAGE_SIZE							(	"128"					),
	INDEX_JOB_WORKERS							(	"2"						),
	DEFAULT_PRODUCT_TYPE_NAME					(	"*"						),
	CATALOG_IMPORTER_PAGE_SIZE					(	"100"					),
	ESTIMATED_CATALOG_IMPORT_TIME_IN_MINUTES	(   "30"                    ),
	MAX_SIZE_FOR_EXCEL_EXPORT					(   "10000"					),
	MAX_SIZE_FOR_PDF_PRINT_OUTS					(	"1000"					),
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
	MAX_MULTI_ADD_SIZE							(	"250"					),
	SETUPDATA_MIN_UPDATE_INTERVAL_MS			(	"30000"					),
	UPLOAD_FILE_SIZE_LIMIT_DEFAULT_IN_KB			(	"5120"				),
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_ATTACHMENT_IN_KB	(	"2048"				), 
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_TYPE_IMAGE_IN_KB (	"100"				),
	
	
	// ones below here are never overridden by the tenant.
	FIELDID_ADMINISTRATOR_EMAIL					(	"team@n4systems.com"	),
	SYSTEM_USER_USERNAME						(	"n4systems"			),
	SYSTEM_USER_PASSWORD						(	"2a3bb4bc2f0cf8f2c09cd8aef2824a901d186fe7"	),
	SYSTEM_USER_ADDRESS							(	"admin@n4systems.com"						),
	UNBRANDED_SUBDOMAIN							(	"www"										),
	GLOBAL_APPLICATION_ROOT						(	"/var/fieldid"			),
	DEFAULT_TIMEZONE_ID							(	"United States:New York - New York"			),
	SYSTEM_DOMAIN								(   "fieldid.com"),
	SUBSCRIPTION_AGENT							(	"com.n4systems.subscription.netsuite.NetSuiteSubscriptionAgent");
	
	
	private String defaultValue;
	
	ConfigEntry(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
}
