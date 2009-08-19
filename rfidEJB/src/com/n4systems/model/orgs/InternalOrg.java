package com.n4systems.model.orgs;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@SuppressWarnings("serial")
@MappedSuperclass
abstract public class InternalOrg extends BaseOrg {

	@Column(name="certificatename")
	private String certificateName;

	@Column(name="defaulttimezone")
	private String defaultTimeZone;
	
	public InternalOrg() {}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getDefaultTimeZone() {
		return defaultTimeZone;
	}

	public void setDefaultTimeZone(String defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}
	
}
