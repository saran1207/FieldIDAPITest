package com.n4systems.services.config;

public class RootConfig {
	private final String name;
	private final Long tenantId;
	private final AwsConfig aws;
	private final SystemConfig system;
	private final WebConfig web;
	private final MobileConfig mobile;
	private final IndexingConfig indexing;
	private final MailConfig mail;
	private final ProofTestConfig proofTest;
	private final LimitConfig limit;

	public RootConfig(MutableRootConfig other) {
		this.name = other.getName();
		this.tenantId = other.getTenantId();
		this.aws = new AwsConfig(other.getAws());
		this.system = new SystemConfig(other.getSystem());
		this.web = new WebConfig(other.getWeb());
		this.mobile = new MobileConfig(other.getMobile());
		this.indexing = new IndexingConfig(other.getIndexing());
		this.mail = new MailConfig(other.getMail());
		this.proofTest = new ProofTestConfig(other.getProofTest());
		this.limit = new LimitConfig(other.getLimit());
	}

	public String getName() {
		return name;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public AwsConfig getAws() {
		return aws;
	}

	public SystemConfig getSystem() {
		return system;
	}

	public WebConfig getWeb() {
		return web;
	}

	public MobileConfig getMobile() {
		return mobile;
	}

	public IndexingConfig getIndexing() {
		return indexing;
	}

	public MailConfig getMail() {
		return mail;
	}

	public ProofTestConfig getProofTest() {
		return proofTest;
	}

	public LimitConfig getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "RootConfig: {\n" +
					"\tname: " + name + "\n" +
					"\ttenantId: " + tenantId + "\n" +
					"\taws: {\n" + aws + "\t}\n" +
					"\tsystem: {\n" + system + "\t}\n" +
					"\tweb: {\n" + web + "\t}\n" +
					"\tmobile: {\n" + mobile + "\t}\n" +
					"\tindexing: {\n" + indexing + "\t}\n" +
					"\tmail: {\n" + mail + "\t}\n" +
					"\tproofTest: {\n" + proofTest + "\t}\n" +
					"\tlimit: {\n" + limit + "\t}\n" +
				'}';
	}
}
