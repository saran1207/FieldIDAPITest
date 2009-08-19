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
	@Column(name="name", nullable=false)
	private ConfigEntry key;
	
	@Column(nullable=false)
	private String value;

	private Long tenantId;
	
	public Configuration() {}
	
	public Configuration(ConfigEntry key, String value) {
		this(key, value, null);
	}
	
	public Configuration(ConfigEntry key, String value, Long tenantId) {
		this.key = key;
		this.value = value;
		this.tenantId = tenantId;
	}
	
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public ConfigEntry getKey() {
		return key;
	}

	public void setKey(ConfigEntry key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String keyValue) {
		this.value = keyValue;
	}
	
	
	
}
