package com.n4systems.model.eula;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table(name = "eulas")
public class EULA extends AbstractEntity implements UnsecuredEntity {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String legalText;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;
	
	@Column(nullable=false, length=10)
	private String version;

	public EULA() {
	}

	public String getLegalText() {
		return legalText;
	}

	public void setLegalText(String legalText) {
		if (allowUpdate()) {
			this.legalText = legalText;
		}
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		if (allowUpdate()) {
			this.effectiveDate = effectiveDate;
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		if (allowUpdate()) {
			this.version = version;
		}
	}
	
	private boolean allowUpdate() {
		return (isNew() || effectiveDate.after(new Date()));
	}

}
