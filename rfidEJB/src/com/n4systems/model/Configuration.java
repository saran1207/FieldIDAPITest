package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.ConfigEntry;

@Entity
@Table( name = "configurations" )
public class Configuration extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ConfigEntry identifier;
	
	@Column(nullable=false)
	private String value;

	private Long tenantId;
	
	public Configuration() {}
	
	public Configuration(ConfigEntry identifier, String value) {
		this(identifier, value, null);
	}
	
	public Configuration(ConfigEntry identifier, String value, Long tenantId) {
		this.identifier = identifier;
		this.value = value;
		this.tenantId = tenantId;
	}
	
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	@Deprecated
	public ConfigEntry getKey() {
		return getIdentifier();
	}

	@Deprecated
	public void setKey(ConfigEntry key) {
		setIdentifier(key);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String keyValue) {
		this.value = keyValue;
	}

	public ConfigEntry getIdentifier() {
		return identifier;
	}

	public void setIdentifier(ConfigEntry identifier) {
		this.identifier = identifier;
	}
	
	
	
}
