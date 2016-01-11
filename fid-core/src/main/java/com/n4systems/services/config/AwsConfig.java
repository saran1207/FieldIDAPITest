package com.n4systems.services.config;

public class AwsConfig {
	protected String accessKeyId;
	protected String secretAccessKey;
	protected String endpoint;
	protected String mainBucket;
	protected String lotoReportsBucket;
	protected String uploadMaxFileSizeBytes;
	protected String uploadTimeout;

	public AwsConfig() {}

	public AwsConfig(AwsConfig orig) {
		this.accessKeyId = orig.accessKeyId;
		this.mainBucket = orig.mainBucket;
		this.endpoint = orig.endpoint;
		this.lotoReportsBucket = orig.lotoReportsBucket;
		this.uploadMaxFileSizeBytes = orig.uploadMaxFileSizeBytes;
		this.uploadTimeout = orig.uploadTimeout;
		this.secretAccessKey = orig.secretAccessKey;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public String getMainBucket() {
		return mainBucket;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getLotoReportsBucket() {
		return lotoReportsBucket;
	}

	public String getUploadMaxFileSizeBytes() {
		return uploadMaxFileSizeBytes;
	}

	public String getUploadTimeout() {
		return uploadTimeout;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	@Override
	public String toString() {
		return "\t\taccessKeyId: '" + accessKeyId + "'\n" +
				"\t\tsecretAccessKey: '" + secretAccessKey + "'\n" +
				"\t\tendpoint: '" + endpoint + "'\n" +
				"\t\tmainBucket: '" + mainBucket + "'\n" +
				"\t\tlotoReportsBucket: '" + lotoReportsBucket + "'\n" +
				"\t\tuploadMaxFileSizeBytes: '" + uploadMaxFileSizeBytes + "'\n" +
				"\t\tuploadTimeout: '" + uploadTimeout + "'\n";
	}
}
