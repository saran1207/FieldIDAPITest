package com.n4systems.services.config;

public class MutableAwsConfig extends AwsConfig {

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setMainBucket(String mainBucket) {
		this.mainBucket = mainBucket;
	}

	public void setLotoReportsBucket(String lotoReportsBucket) {
		this.lotoReportsBucket = lotoReportsBucket;
	}

	public void setUploadMaxFileSizeBytes(String uploadMaxFileSizeBytes) {
		this.uploadMaxFileSizeBytes = uploadMaxFileSizeBytes;
	}

	public void setUploadTimeout(String uploadTimeout) {
		this.uploadTimeout = uploadTimeout;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
