package com.n4systems.webservice.assetdownload;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SyncAsset {
	private Long id;
	private Date lastModified;

	public SyncAsset() {
		this(null, null);
	}
	
	public SyncAsset(Long id, Date lastModified) {
		this.id = id;
		this.lastModified = lastModified;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
