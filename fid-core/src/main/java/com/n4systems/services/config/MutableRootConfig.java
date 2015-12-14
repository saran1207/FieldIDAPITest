package com.n4systems.services.config;

public class MutableRootConfig {
	private String name;
	private Long tenantId;
	private MutableAwsConfig aws;
	private MutableSystemConfig system;
	private MutableWebConfig web;
	private MutableMobileConfig mobile;
	private MutableIndexingConfig indexing;
	private MutableMailConfig mail;
	private MutableProofTestConfig proofTest;
	private MutableLimitConfig limit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public MutableAwsConfig getAws() {
		return aws;
	}

	public void setAws(MutableAwsConfig aws) {
		this.aws = aws;
	}

	public MutableSystemConfig getSystem() {
		return system;
	}

	public void setSystem(MutableSystemConfig system) {
		this.system = system;
	}

	public MutableWebConfig getWeb() {
		return web;
	}

	public void setWeb(MutableWebConfig web) {
		this.web = web;
	}

	public MutableMobileConfig getMobile() {
		return mobile;
	}

	public void setMobile(MutableMobileConfig mobile) {
		this.mobile = mobile;
	}

	public MutableIndexingConfig getIndexing() {
		return indexing;
	}

	public void setIndexing(MutableIndexingConfig indexing) {
		this.indexing = indexing;
	}

	public MutableMailConfig getMail() {
		return mail;
	}

	public void setMail(MutableMailConfig mail) {
		this.mail = mail;
	}

	public MutableProofTestConfig getProofTest() {
		return proofTest;
	}

	public void setProofTest(MutableProofTestConfig proofTest) {
		this.proofTest = proofTest;
	}

	public MutableLimitConfig getLimit() {
		return limit;
	}

	public void setLimit(MutableLimitConfig limit) {
		this.limit = limit;
	}
}
