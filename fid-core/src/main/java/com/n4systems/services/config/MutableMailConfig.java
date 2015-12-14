package com.n4systems.services.config;

public class MutableMailConfig extends MailConfig {

	public void setManagerClass(String managerClass) {
		this.managerClass = managerClass;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

	public void setAuthPass(String authPass) {
		this.authPass = authPass;
	}

	public void setSubjectPrefix(String subjectPrefix) {
		this.subjectPrefix = subjectPrefix;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public void setSalesAddr(String salesAddr) {
		this.salesAddr = salesAddr;
	}

	public void setAdminAddr(String adminAddr) {
		this.adminAddr = adminAddr;
	}

	public void setSalesManagerAddr(String salesManagerAddr) {
		this.salesManagerAddr = salesManagerAddr;
	}

	public void setHtmlHeader(String htmlHeader) {
		this.htmlHeader = htmlHeader;
	}

	public void setHtmlFooter(String htmlFooter) {
		this.htmlFooter = htmlFooter;
	}

	public void setPlainHeader(String plainHeader) {
		this.plainHeader = plainHeader;
	}

	public void setPlainFooter(String plainFooter) {
		this.plainFooter = plainFooter;
	}

}
