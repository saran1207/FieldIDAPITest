package com.n4systems.services.config;

public class MutableRootConfig {
	private String name;
	private Long tenantId;
	private MutableAwsConfig aws = new MutableAwsConfig();
	private MutableSystemConfig system = new MutableSystemConfig();
	private MutableWebConfig web = new MutableWebConfig();
	private MutableMobileConfig mobile = new MutableMobileConfig();
	private MutableIndexingConfig indexing = new MutableIndexingConfig();
	private MutableMailConfig mail = new MutableMailConfig();
	private MutableProofTestConfig proofTest = new MutableProofTestConfig();
	private MutableLimitConfig limit = new MutableLimitConfig();

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
