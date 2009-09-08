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
		if (certificateName != null && certificateName.trim().length() > 0) {
			return certificateName;
		}
		
		// PrimaryOrgs default to their name, SecondaryOrgs differ to the primary		
		return (isPrimary()) ? getName() : ((SecondaryOrg)this).getParent().getCertificateName();
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
