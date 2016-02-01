package com.n4systems.services.config;

public class MutableLimitConfig extends LimitConfig {

	public void setActiveSessionTimeOut(Integer activeSessionTimeOut) {
		this.activeSessionTimeOut = activeSessionTimeOut;
	}

	public void setCatalogImporterPageSize(Integer catalogImporterPageSize) {
		this.catalogImporterPageSize = catalogImporterPageSize;
	}

	public void setMassActionsLimit(Long massActionsLimit) {
		this.massActionsLimit = massActionsLimit;
	}

	public void setMaxMultiAddSize(Integer maxMultiAddSize) {
		this.maxMultiAddSize = maxMultiAddSize;
	}

	public void setMaxSerialsPerProoftest(Integer maxSerialsPerProoftest) {
		this.maxSerialsPerProoftest = maxSerialsPerProoftest;
	}

	public void setMaxSizeForAssigningInspectionsToJobs(Integer maxSizeForAssigningInspectionsToJobs) {
		this.maxSizeForAssigningInspectionsToJobs = maxSizeForAssigningInspectionsToJobs;
	}

	public void setMaxSizeForSummaryReport(Integer maxSizeForSummaryReport) {
		this.maxSizeForSummaryReport = maxSizeForSummaryReport;
	}

	public void setUploadFileSizeLimitDefaultInKb(Long uploadFileSizeLimitDefaultInKb) {
		this.uploadFileSizeLimitDefaultInKb = uploadFileSizeLimitDefaultInKb;
	}

	public void setUploadFileSizeLimitProductAttachmentInKb(Long uploadFileSizeLimitProductAttachmentInKb) {
		this.uploadFileSizeLimitProductAttachmentInKb = uploadFileSizeLimitProductAttachmentInKb;
	}

	public void setUploadFileSizeLimitProductTypeImageInKb(Long uploadFileSizeLimitProductTypeImageInKb) {
		this.uploadFileSizeLimitProductTypeImageInKb = uploadFileSizeLimitProductTypeImageInKb;
	}

	public void setUsageBasedEventCountThreshold(Integer usageBasedEventCountThreshold) {
		this.usageBasedEventCountThreshold = usageBasedEventCountThreshold;
	}

	public void setProductFileUploadMax(Integer productFileUploadMax) {
		this.productFileUploadMax = productFileUploadMax;
	}

	public void setReportingMaxReportsPerFile(Integer reportingMaxReportsPerFile) {
		this.reportingMaxReportsPerFile = reportingMaxReportsPerFile;
	}

	public void setEstimatedCatalogImportTimeInMinutes(Integer estimatedCatalogImportTimeInMinutes) {
		this.estimatedCatalogImportTimeInMinutes = estimatedCatalogImportTimeInMinutes;
	}

	public void setWebFileUploadMax(Integer webFileUploadMax) {
		this.webFileUploadMax = webFileUploadMax;
	}

	public void setWebPaginationPageSize(Integer webPaginationPageSize) {
		this.webPaginationPageSize = webPaginationPageSize;
	}

	public void setWebTotalInspectionButtons(Long webTotalInspectionButtons) {
		this.webTotalInspectionButtons = webTotalInspectionButtons;
	}

	public void setExportPageSize(Integer exportPageSize) {
		this.exportPageSize = exportPageSize;
	}
}
