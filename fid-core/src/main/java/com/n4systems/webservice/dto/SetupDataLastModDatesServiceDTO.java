package com.n4systems.webservice.dto;

import java.io.Serializable;
import java.util.Date;

public class SetupDataLastModDatesServiceDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date productTypes;
	private Date inspectionTypes;
	private Date autoAttributes;
	private Date owners;
	private Date jobs;
	private Date employees;
	private Date locations;
	
	public Date getEmployees() {
		return employees;
	}

	public void setEmployees(Date employees) {
		this.employees = employees;
	}

	public SetupDataLastModDatesServiceDTO() {}

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

	public Date getLocations() {
		return locations;
	}

	public void setLocations(Date locations) {
		this.locations = locations;
	}
	
}
