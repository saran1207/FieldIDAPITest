package com.n4systems.model.builders;

import static com.n4systems.model.builders.TenantBuilder.*;

import java.util.Arrays;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Project;
import com.n4systems.model.TenantOrganization;

public class JobBuilder extends BaseBuilder<Project> {

	private final TenantOrganization tenantOrganization;
	private final boolean eventJob;
	private final UserBean[] employees;
	
	public static JobBuilder aJob() {
		return new JobBuilder(aTenant().build());
	}
	
	
	
	public JobBuilder(TenantOrganization tenant) {
		this(tenant, true, null);
	}
	
	public JobBuilder(TenantOrganization tenant, boolean eventJob, UserBean[] employees) {
		super();
		this.eventJob = eventJob;
		this.tenantOrganization = tenant;
		if (employees == null) {
			this.employees = new UserBean[0];
		} else {
			this.employees = employees;
		}
	}
	
	public JobBuilder withResources(UserBean...employees) {
		return new JobBuilder(tenantOrganization, eventJob, employees);
	}
	
	
	@Override
	public Project build() {
		Project job = new Project();
		job.setId(id);
		job.setTenant(tenantOrganization);
		job.setOpen(true);
		job.setEventJob(eventJob);
		job.getResources().addAll(Arrays.asList(employees));
		return job;
	}

}
