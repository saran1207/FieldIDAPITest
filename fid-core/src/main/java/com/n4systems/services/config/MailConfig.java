package com.n4systems.services.config;

public class MailConfig {
	protected String managerClass;
	protected String host;
	protected String port;
	protected String authUser;
	protected String authPass;
	protected String subjectPrefix;
	protected String fromAddr;
	protected String replyTo;
	protected String salesAddr;
	protected String adminAddr;
	protected String salesManagerAddr;
	protected String htmlHeader;
	protected String htmlFooter;
	protected String plainHeader;
	protected String plainFooter;

	public MailConfig() {}

	public MailConfig(MailConfig other) {
		this.authPass = other.authPass;
		this.authUser = other.authUser;
		this.htmlHeader = other.htmlHeader;
		this.htmlFooter = other.htmlFooter;
		this.plainHeader = other.plainHeader;
		this.plainFooter = other.plainFooter;
		this.fromAddr = other.fromAddr;
		this.managerClass = other.managerClass;
		this.host = other.host;
		this.port = other.port;
		this.subjectPrefix = other.subjectPrefix;
		this.replyTo = other.replyTo;
		this.salesAddr = other.salesAddr;
		this.adminAddr = other.adminAddr;
		this.salesManagerAddr = other.salesManagerAddr;
	}

	public String getAuthPass() {
		return authPass;
	}

	public String getAuthUser() {
		return authUser;
	}

	public String getHtmlFooter() {
		return htmlFooter;
	}

	public String getHtmlHeader() {
		return htmlHeader;
	}

	public String getPlainFooter() {
		return plainFooter;
	}

	public String getPlainHeader() {
		return plainHeader;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public String getHost() {
		return host;
	}

	public String getManagerClass() {
		return managerClass;
	}

	public String getPort() {
		return port;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public String getSubjectPrefix() {
		return subjectPrefix;
	}

	public String getSalesAddr() {
		return salesAddr;
	}

	public String getAdminAddr() {
		return adminAddr;
	}

	public String getSalesManagerAddr() {
		return salesManagerAddr;
	}

	@Override
	public String toString() {
		return "\t\tmanagerClass: '" + managerClass + "'\n" +
				"\t\thost: '" + host + "'\n" +
				"\t\tport: '" + port + "'\n" +
				"\t\tauthUser: '" + authUser + "'\n" +
				"\t\tauthPass: '" + authPass + "'\n" +
				"\t\tsubjectPrefix: '" + subjectPrefix + "'\n" +
				"\t\tfromAddr: '" + fromAddr + "'\n" +
				"\t\treplyTo: '" + replyTo + "'\n" +
				"\t\tsalesAddr: '" + salesAddr + "'\n" +
				"\t\tadminAddr: '" + adminAddr + "'\n" +
				"\t\tsalesManagerAddr: '" + salesManagerAddr + "'\n" +
				"\t\thtmlHeader: '" + htmlHeader + "'\n" +
				"\t\thtmlFooter: '" + htmlFooter + "'\n" +
				"\t\tplainHeader: '" + plainHeader + "'\n" +
				"\t\tplainFooter: '" + plainFooter + "'\n";
	}
}
