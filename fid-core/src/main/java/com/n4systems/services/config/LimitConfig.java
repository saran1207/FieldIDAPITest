package com.n4systems.services.config;

public class LimitConfig {
	protected Integer activeSessionTimeOut;
	protected Integer catalogImporterPageSize;
	protected Long massActionsLimit;
	protected Integer maxMultiAddSize;
	protected Integer maxSerialsPerProoftest;
	protected Integer maxSizeForAssigningInspectionsToJobs;
	protected Integer maxSizeForSummaryReport;
	protected Long uploadFileSizeLimitDefaultInKb;
	protected Long uploadFileSizeLimitProductAttachmentInKb;
	protected Long uploadFileSizeLimitProductTypeImageInKb;
	protected Integer usageBasedEventCountThreshold;
	protected Integer productFileUploadMax;
	protected Integer reportingMaxReportsPerFile;
	protected Integer estimatedCatalogImportTimeInMinutes;
	protected Integer webFileUploadMax;
	protected Integer webPaginationPageSize;
	protected Long webTotalInspectionButtons;

	public LimitConfig() {}

	public LimitConfig(LimitConfig other) {
		this.activeSessionTimeOut = other.activeSessionTimeOut;
		this.catalogImporterPageSize = other.catalogImporterPageSize;
		this.massActionsLimit = other.massActionsLimit;
		this.maxMultiAddSize = other.maxMultiAddSize;
		this.maxSerialsPerProoftest = other.maxSerialsPerProoftest;
		this.maxSizeForAssigningInspectionsToJobs = other.maxSizeForAssigningInspectionsToJobs;
		this.maxSizeForSummaryReport = other.maxSizeForSummaryReport;
		this.uploadFileSizeLimitDefaultInKb = other.uploadFileSizeLimitDefaultInKb;
		this.uploadFileSizeLimitProductAttachmentInKb = other.uploadFileSizeLimitProductAttachmentInKb;
		this.uploadFileSizeLimitProductTypeImageInKb = other.uploadFileSizeLimitProductTypeImageInKb;
		this.usageBasedEventCountThreshold = other.usageBasedEventCountThreshold;
		this.productFileUploadMax = other.productFileUploadMax;
		this.reportingMaxReportsPerFile = other.reportingMaxReportsPerFile;
		this.estimatedCatalogImportTimeInMinutes = other.estimatedCatalogImportTimeInMinutes;
		this.webFileUploadMax = other.webFileUploadMax;
		this.webPaginationPageSize = other.webPaginationPageSize;
		this.webTotalInspectionButtons = other.webTotalInspectionButtons;
	}

	public Integer getActiveSessionTimeOut() {
		return activeSessionTimeOut;
	}

	public Integer getCatalogImporterPageSize() {
		return catalogImporterPageSize;
	}

	public Long getMassActionsLimit() {
		return massActionsLimit;
	}

	public Integer getMaxMultiAddSize() {
		return maxMultiAddSize;
	}

	public Integer getMaxSerialsPerProoftest() {
		return maxSerialsPerProoftest;
	}

	public Integer getMaxSizeForAssigningInspectionsToJobs() {
		return maxSizeForAssigningInspectionsToJobs;
	}

	public Integer getMaxSizeForSummaryReport() {
		return maxSizeForSummaryReport;
	}

	public Long getUploadFileSizeLimitDefaultInKb() {
		return uploadFileSizeLimitDefaultInKb;
	}

	public Long getUploadFileSizeLimitProductAttachmentInKb() {
		return uploadFileSizeLimitProductAttachmentInKb;
	}

	public Long getUploadFileSizeLimitProductTypeImageInKb() {
		return uploadFileSizeLimitProductTypeImageInKb;
	}

	public Integer getUsageBasedEventCountThreshold() {
		return usageBasedEventCountThreshold;
	}

	public Integer getProductFileUploadMax() {
		return productFileUploadMax;
	}

	public Integer getReportingMaxReportsPerFile() {
		return reportingMaxReportsPerFile;
	}

	public Integer getEstimatedCatalogImportTimeInMinutes() {
		return estimatedCatalogImportTimeInMinutes;
	}

	public Integer getWebFileUploadMax() {
		return webFileUploadMax;
	}

	public Integer getWebPaginationPageSize() {
		return webPaginationPageSize;
	}

	public Long getWebTotalInspectionButtons() {
		return webTotalInspectionButtons;
	}

	@Override
	public String toString() {
		return "\t\tactiveSessionTimeOut: " + activeSessionTimeOut + '\n' +
				"\t\tcatalogImporterPageSize: " + catalogImporterPageSize + '\n' +
				"\t\tmassActionsLimit: " + massActionsLimit + '\n' +
				"\t\tmaxMultiAddSize: " + maxMultiAddSize + '\n' +
				"\t\tmaxSerialsPerProoftest: " + maxSerialsPerProoftest + '\n' +
				"\t\tmaxSizeForAssigningInspectionsToJobs: " + maxSizeForAssigningInspectionsToJobs + '\n' +
				"\t\tmaxSizeForSummaryReport: " + maxSizeForSummaryReport + '\n' +
				"\t\tuploadFileSizeLimitDefaultInKb: " + uploadFileSizeLimitDefaultInKb + '\n' +
				"\t\tuploadFileSizeLimitProductAttachmentInKb: " + uploadFileSizeLimitProductAttachmentInKb + '\n' +
				"\t\tuploadFileSizeLimitProductTypeImageInKb: " + uploadFileSizeLimitProductTypeImageInKb + '\n' +
				"\t\tusageBasedEventCountThreshold: " + usageBasedEventCountThreshold + '\n' +
				"\t\tproductFileUploadMax: " + productFileUploadMax + '\n' +
				"\t\treportingMaxReportsPerFile: " + reportingMaxReportsPerFile + '\n' +
				"\t\testimatedCatalogImportTimeInMinutes: " + estimatedCatalogImportTimeInMinutes + '\n' +
				"\t\twebFileUploadMax: " + webFileUploadMax + '\n' +
				"\t\twebPaginationPageSize: " + webPaginationPageSize + '\n' +
				"\t\twebTotalInspectionButtons: " + webTotalInspectionButtons + '\n';
	}
}
