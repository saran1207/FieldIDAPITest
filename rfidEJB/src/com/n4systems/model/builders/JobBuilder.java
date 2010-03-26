package com.n4systems.model.builders;

import static com.n4systems.model.builders.TenantBuilder.*;

import java.util.Arrays;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Project;
import com.n4systems.model.Tenant;
import com.n4systems.util.RandomString;

public class JobBuilder extends BaseBuilder<Project> {

	private final Tenant tenantOrganization;
	private final boolean eventJob;
	private final UserBean[] employees;
	private final String title;
	private final String identifier;
	
	public static JobBuilder aJob() {
		return new JobBuilder(aTenant().build());
	}
	
	
	
	private JobBuilder(Tenant tenant) {
		this(tenant, true, null, RandomString.getString(10), RandomString.getString(10));
	}
	
	private JobBuilder(Tenant tenant, boolean eventJob, UserBean[] employees, String title, String identifier) {
		super();
		this.eventJob = eventJob;
		this.tenantOrganization = tenant;
		this.title = title;
		this.identifier = identifier;
		if (employees == null) {
			this.employees = new UserBean[0];
		} else {
			this.employees = employees;
		}
	}
	
	

	public JobBuilder withTitle(String title) {
		return new JobBuilder(tenantOrganization, eventJob, employees, title, identifier);
	}
	
	public JobBuilder withResources(UserBean...employees) {
		return new JobBuilder(tenantOrganization, eventJob, employees, title, identifier);
	}

	public JobBuilder withProjectID(String identifier) {
		return new JobBuilder(tenantOrganization, eventJob, employees, title, identifier);
	}
	
	
	@Override
	public Project build() {
		Project job = new Project();
		job.setId(id);
		job.setTenant(tenantOrganization);
		job.setOpen(true);
		job.setName(title);
		job.setProjectID(identifier);
		job.setEventJob(eventJob);
		job.getResources().addAll(Arrays.asList(employees));
		return job;
	}







}
