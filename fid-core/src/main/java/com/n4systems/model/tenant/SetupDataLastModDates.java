package com.n4systems.model.tenant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasTenantId;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.services.SetupDataGroup;

@Entity
@Table(name = "setupdatalastmoddates")
public class SetupDataLastModDates implements HasTenantId, Saveable, Serializable {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenantId", null, null, null);
	}
	
	/**
	 * This field is used as the primary key for this table.  It will always be set to tenant.id.
	 * Note the PrimaryKeyJoinColumn annotation on the tenant field.
	 */
	@Id
	@Column(name="tenant_id")
	private Long tenantId;

	@PrimaryKeyJoinColumn(name="tenant_id")
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private Tenant tenant;

	@Column(name="assettypes", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date assetTypes = new Date();

	@Column(name="eventtypes", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTypes = new Date();
	
	@Column(name="autoattributes", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date autoAttributes = new Date();

	@Column(name="owners", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date owners = new Date();

	@Column(name="jobs", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobs = new Date();
	
	@Column(name="employees", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date employees = new Date();
	
	@Column(name="locations", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date locations = new Date();	
	
	public static String getFieldNameForSetupDataGroup(SetupDataGroup group) {
		switch (group) {
			case PRODUCT_TYPE:		return "assetTypes";
			case INSPECTION_TYPE:	return "eventTypes";
			case AUTO_ATTRIBUTES:	return "autoAttributes";
			case OWNERS:			return "owners";
			case JOBS:				return "jobs";
			case EMPLOYEES:			return "employees";
			case LOCATIONS:			return "locations";
			default: 				throw new IllegalArgumentException("Unknown group " + group.name());
		}
	}
	
	public SetupDataLastModDates() {}
	
	public SetupDataLastModDates(Tenant tenant) {
		setTenant(tenant);
	}
	
	public boolean isNew() {
		// This entity has no auto-gen id so there's really no way to tell if it's new or not
		// we'll assume it's not since merge() can handle a new entity
		return false;
	}

    @Override
	public Object getEntityId() {
		return getTenantId();
	}
	
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long r_tenant) {
		this.tenantId = r_tenant;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
		this.tenantId = tenant.getId();
	}

	public Date getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(Date assetTypes) {
		this.assetTypes = assetTypes;
	}

	public Date getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(Date eventTypes) {
		this.eventTypes = eventTypes;
	}

	public Date getAutoAttributes() {
		return autoAttributes;
	}

	public void setAutoAttributes(Date autoAttributes) {
		this.autoAttributes = autoAttributes;
	}

	public Date getOwners() {
		return owners;
	}

	public void setOwners(Date owners) {
		this.owners = owners;
	}

	public Date getJobs() {
		return jobs;
	}

	public void setJobs(Date jobs) {
		this.jobs = jobs;
	}
	
	public Date getEmployees() {
		return employees;
	}

	public void setEmployees(Date employees) {
		this.employees = employees;
	}

	public Date getLocations() {
		return locations;
	}

	public void setLocations(Date locations) {
		this.locations = locations;
	}

}
