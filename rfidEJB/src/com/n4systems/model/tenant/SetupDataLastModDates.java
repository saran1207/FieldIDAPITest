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
import com.n4systems.model.api.Saveable;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.services.SetupDataGroup;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "setupdatalastmoddates")
public class SetupDataLastModDates implements FilteredEntity, Saveable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * This field is used as the primary key for this table.  It will always be set to tenant.id.
	 * Note the PrimaryKeyJoinColumn annotation on the tenant field.
	 */
	@Id
	private Long tenant_id;

	@PrimaryKeyJoinColumn(name="tenant_id")
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private Tenant tenant;

	@Column(name="producttypes", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date productTypes = new Date();

	@Column(name="inspectiontypes", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionTypes = new Date();
	
	@Column(name="autoattributes", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date autoAttributes = new Date();

	@Column(name="owners", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date owners = new Date();

	@Column(name="jobs", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobs = new Date();
	
	public SetupDataLastModDates() {}
	
	public SetupDataLastModDates(Tenant tenant) {
		setTenant(tenant);
	}
	
	public boolean isNew() {
		// This entity has no auto-gen id so there's really no way to tell if it's new or not
		// we'll assume it's not since merge() can handle a new entity
		return false;
	}
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets("r_tenant", null, null, null, null);
	}
	
	public void setModDate(SetupDataGroup group, Date newDate) {
		switch (group) {
			case PRODUCT_TYPE:
				productTypes = newDate;
				break;
			case INSPECTION_TYPE:
				inspectionTypes = newDate;
				break;
			case AUTO_ATTRIBUTES:
				autoAttributes = newDate;
				break;
			case OWNERS:
				owners = newDate;
				break;
			case JOBS:
				jobs = newDate;
				break;
		}
	}
	
	public Date getModDate(SetupDataGroup group) {
		switch (group) {
			case PRODUCT_TYPE:
				return productTypes;
			case INSPECTION_TYPE:
				return inspectionTypes;
			case AUTO_ATTRIBUTES:
				return autoAttributes;
			case OWNERS:
				return owners;
			case JOBS:
				return jobs;
		}
		return null;
	}
	
	protected Long getTenant_id() {
		return tenant_id;
	}

	protected void setTenant_id(Long r_tenant) {
		this.tenant_id = r_tenant;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		// need to set both here ... I thought Hibernate would manage setting the r_tenant
		// from the tenant given the PrimaryKeyJoinColumn annotation but it doesn't ...
		this.tenant = tenant;
		this.tenant_id = tenant.getId();
	}

	public Date getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(Date productTypes) {
		this.productTypes = productTypes;
	}

	public Date getInspectionTypes() {
		return inspectionTypes;
	}

	public void setInspectionTypes(Date inspectionTypes) {
		this.inspectionTypes = inspectionTypes;
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

}
