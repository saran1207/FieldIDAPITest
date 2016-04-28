package com.n4systems.util;


import com.n4systems.services.config.RootConfig;

import java.util.function.Function;

public enum ConfigEntry {
	ACTIVE_SESSION_TIME_OUT					(r -> r.getLimit().getActiveSessionTimeOut()),
	AMAZON_ACCESS_KEY_ID					(r -> r.getAws().getAccessKeyId()),
	AMAZON_S3_BUCKET						(r -> r.getAws().getMainBucket()),
	AMAZON_S3_ENDPOINT						(r -> r.getAws().getEndpoint()),
	AMAZON_S3_LOTO_REPORTS					(r -> r.getAws().getLotoReportsBucket()),
	AMAZON_S3_UPLOAD_MAX_FILE_SIZE_BYTES	(r -> r.getAws().getUploadMaxFileSizeBytes()),
	AMAZON_S3_UPLOAD_TIMEOUT_MILLISECONDS	(r -> r.getAws().getUploadTimeout()),
	AMAZON_SECRET_ACCESS_KEY				(r -> r.getAws().getSecretAccessKey()),
	APPTEGIC_DATASET						(r -> r.getWeb().getApptegicDataset()),
	APPTEGIC_ENABLED						(r -> r.getWeb().getApptegicEnabled()),
	ASSET_INDEX_ENABLED						(r -> r.getIndexing().getAssetIndexEnabled()),
	ASSET_INDEX_SIZE						(r -> r.getIndexing().getAssetIndexSize()),
	CATALOG_IMPORTER_PAGE_SIZE				(r -> r.getLimit().getCatalogImporterPageSize()),
	CURRENT_MOBILE_BUILD_VERSION			(r -> r.getMobile().getCurrentMobileBuildVersion()),
	CURRENT_MOBILE_FILE_NAME				(r -> r.getMobile().getCurrentMobileFileName()),
	CURRENT_MOBILE_MAJOR_VERSION			(r -> r.getMobile().getCurrentMobileMajorVersion()),
	CURRENT_MOBILE_MINOR_VERSION			(r -> r.getMobile().getCurrentMobileMinorVersion()),
	CUSTOM_JS								(r -> r.getWeb().getCustomJs()),
	DEFAULT_EXECUTOR_POOL_SIZE				(r -> r.getSystem().getDefaultExecutorPoolSize()),
	DEFAULT_PRODUCT_TYPE_NAME				(r -> r.getSystem().getDefaultProductTypeName()),
	DEFAULT_TIMEZONE_ID						(r -> r.getSystem().getDefaultTimezoneId()),
	DOWNLOAD_TTL_DAYS						(r -> r.getSystem().getDownloadTTLDays()),
	ESTIMATED_CATALOG_IMPORT_TIME_IN_MINUTES(r -> r.getLimit().getEstimatedCatalogImportTimeInMinutes()),
	EVENT_INDEX_ENABLED						(r -> r.getIndexing().getEventIndexEnabled()),
	EVENT_INDEX_SIZE						(r -> r.getIndexing().getEventIndexSize()),
	EXTERNAL_PLANS_AND_PRICING_URL			(r -> r.getWeb().getExternalPlansAndPricingUrl()),
	FIELDID_ADMINISTRATOR_EMAIL				(r -> r.getMail().getAdminAddr()),
	FIELDID_SALES_MANAGERS_EMAIL			(r -> r.getMail().getSalesManagerAddr()),
	FOOTER_SCRIPT							(r -> r.getWeb().getFooterScript()),
	GLOBAL_APPLICATION_ROOT					(r -> r.getSystem().getGlobalApplicationRoot()),
	GOOGLE_ANALYTICS_ENABLED				(r -> r.getWeb().getGoogleAnalyticsEnabled()),
	GRAPHING_CHART_PEAK_ARROWS				(r -> r.getProofTest().getChartPeakArrows()),
	GRAPHING_CHART_PEAK_DOTS				(r -> r.getProofTest().getChartPeakDots()),
	GRAPHING_CHART_PEAK_MARKERS				(r -> r.getProofTest().getChartPeakMarkers()),
	GRAPHING_CHART_SIZE_X					(r -> r.getProofTest().getChartSizeX()),
	GRAPHING_CHART_SIZE_Y					(r -> r.getProofTest().getChartSizeY()),
	HEADER_SCRIPT							(r -> r.getWeb().getHeaderScript()),
	HELP_SYSTEM_URL							(r -> r.getWeb().getHelpSystemUrl()),
	HOUR_TO_RUN_EVENT_SCHED_NOTIFICATIONS	(r -> r.getSystem().getHourToRunEventSchedNotifications()),
	HOUSE_ACCOUNT_NAME						(r -> r.getSystem().getHouseAccountName()),
	HOUSE_ACCOUNT_PRIMARY_ORG_ID			(r -> r.getSystem().getHouseAccountPrimaryOrgId()),
	MAIL_AUTH_PASS							(r -> r.getMail().getAuthPass()),
	MAIL_AUTH_USER							(r -> r.getMail().getAuthUser()),
	MAIL_BODY_HTML_FOOTER					(r -> r.getMail().getHtmlFooter()),
	MAIL_BODY_HTML_HEADER					(r -> r.getMail().getHtmlHeader()),
	MAIL_BODY_PLAIN_FOOTER					(r -> r.getMail().getPlainFooter()),
	MAIL_BODY_PLAIN_HEADER					(r -> r.getMail().getPlainHeader()),
	MAIL_FROM_ADDR							(r -> r.getMail().getFromAddr()),
	MAIL_HOST								(r -> r.getMail().getHost()),
	MAIL_MANAGER							(r -> r.getMail().getManagerClass()),
	MAIL_PORT								(r -> r.getMail().getPort()),
	MAIL_REPLY_TO							(r -> r.getMail().getReplyTo()),
	MAIL_SUBJECT_PREFIX						(r -> r.getMail().getSubjectPrefix()),
	MASS_ACTIONS_LIMIT						(r -> r.getLimit().getMassActionsLimit()),
	MAX_MULTI_ADD_SIZE						(r -> r.getLimit().getMaxMultiAddSize()),
	MAX_SERIALS_PER_PROOFTEST				(r -> r.getLimit().getMaxSerialsPerProoftest()),
	MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS(r -> r.getLimit().getMaxSizeForAssigningInspectionsToJobs()),
	MAX_SIZE_FOR_SUMMARY_REPORT				(r -> r.getLimit().getMaxSizeForSummaryReport()),
	MINIMUM_MOBILE_MAJOR_VERSION			(r -> r.getMobile().getMinimumMobileMajorVersion()),
	MINIMUM_MOBILE_MINOR_VERSION			(r -> r.getMobile().getMinimumMobileMinorVersion()),
	MIXPANEL_ENABLED						(r -> r.getWeb().getMixpanelEnabled()),
	MIXPANEL_TOKEN							(r -> r.getWeb().getMixpanelToken()),
	MOBILE_PAGESIZE_INSPECTIONS				(r -> r.getMobile().getMobilePagesizeInspections()),
	MOBILE_PAGESIZE_PRODUCTS				(r -> r.getMobile().getMobilePagesizeProducts()),
	MOBLIE_PAGESIZE_SETUPDATA				(r -> r.getMobile().getMobliePagesizeSetupdata()),
	PRODUCT_FILE_UPLOAD_MAX					(r -> r.getLimit().getProductFileUploadMax()),
	RSS_FEED								(r -> r.getWeb().getRssFeed()),
	SAFETY_NETWORK_HELP_URL					(r -> r.getWeb().getSafetyNetworkHelpUrl()),
	SAFETY_NETWORK_VIDEO_URL				(r -> r.getWeb().getSafetyNetworkVideoUrl()),
	SALES_ADDRESS							(r -> r.getMail().getSalesAddr()),
	STRUTS_FOOTER_SCRIPT					(r -> r.getWeb().getStrutsFooterScript()),
	STRUTS_HEADER_SCRIPT					(r -> r.getWeb().getStrutsHeaderScript()),
	SYSTEM_DOMAIN							(r -> r.getSystem().getDomain()),
	SYSTEM_PROTOCOL							(r -> r.getSystem().getProtocol()),
	SYSTEM_USER_ADDRESS						(r -> r.getSystem().getSystemUserAddress()),
	SYSTEM_USER_PASSWORD					(r -> r.getSystem().getSystemUserPassword()),
	SYSTEM_USER_USERNAME					(r -> r.getSystem().getSystemUserUserId()),
	TRENDS_INDEX_ENABLED					(r -> r.getIndexing().getTrendsIndexEnabled()),
	TRENDS_INDEX_SIZE						(r -> r.getIndexing().getTrendsIndexSize()),
	UNBRANDED_SUBDOMAIN						(r -> r.getSystem().getUnbrandedSubdomain()),
	UPLOAD_FILE_SIZE_LIMIT_DEFAULT_IN_KB	(r -> r.getLimit().getUploadFileSizeLimitDefaultInKb()),
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_ATTACHMENT_IN_KB(r -> r.getLimit().getUploadFileSizeLimitProductAttachmentInKb()),
	UPLOAD_FILE_SIZE_LIMIT_PRODUCT_TYPE_IMAGE_IN_KB(r -> r.getLimit().getUploadFileSizeLimitProductTypeImageInKb()),
	USAGE_BASED_EVENT_COUNT_THRESHOLD		(r -> r.getLimit().getUsageBasedEventCountThreshold()),
	WEB_FILE_UPLOAD_MAX						(r -> r.getLimit().getWebFileUploadMax()),
	WEB_PAGINATION_PAGE_SIZE				(r -> r.getLimit().getWebPaginationPageSize()),
	WEB_TOTAL_INSPECTION_BUTTONS			(r -> r.getLimit().getWebTotalInspectionButtons()),
	REPORTING_MAX_REPORTS_PER_FILE			(r -> r.getLimit().getReportingMaxReportsPerFile()),
	REGION									(r -> r.getAws().getRegion());


	private final Function<RootConfig, ?> rootConfigAdapter;

	ConfigEntry(Function<RootConfig, ?> rootConfigAdapter) {
		this.rootConfigAdapter = rootConfigAdapter;
	}

	public Object getValue(RootConfig config) {
		return rootConfigAdapter.apply(config);
	}
}
