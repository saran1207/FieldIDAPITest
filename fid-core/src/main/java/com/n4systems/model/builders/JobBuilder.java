package com.n4systems.model.builders;

import com.n4systems.model.Project;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

import java.util.Arrays;

public class JobBuilder extends EntityWithTenantBuilder<Project> {

	private final boolean eventJob;
	private final User[] employees;
	private final String title;
	private final String identifier;
    private final String status;
    private final BaseOrg customer;
    
	public static JobBuilder aJob() {
		return new JobBuilder(null, null, true, null, null, null, null);
	}

	private JobBuilder(Tenant tenant, BaseOrg customer, boolean eventJob, User[] employees, String title, String identifier, String status) {
        super(tenant);
        this.customer=customer;
		this.eventJob = eventJob;
		this.title = title;
		this.identifier = identifier;
		if (employees == null) {
			this.employees = new User[0];
		} else {
			this.employees = employees;
		}
        this.status = status;
	}

	public JobBuilder withTitle(String title) {
		return makeBuilder(new JobBuilder(tenant,customer, eventJob, employees, title, identifier, status));
	}
	
	public JobBuilder withOwner(BaseOrg customer) {
		return makeBuilder(new JobBuilder(tenant,customer, eventJob, employees, title, identifier, status));
	}
	
	public JobBuilder withResources(User...employees) {
		return makeBuilder(new JobBuilder(tenant, customer, eventJob, employees, title, identifier, status));
	}

	public JobBuilder withProjectID(String identifier) {
		return makeBuilder(new JobBuilder(tenant, customer,eventJob, employees, title, identifier, status));
	}

    public JobBuilder status(String status) {
        return makeBuilder(new JobBuilder(tenant, customer,eventJob, employees, title, identifier, status));
    }

	@Override
	public Project createObject() {
		Project job = assignAbstractFields(new Project());
		job.setId(getId());
		job.setOwner(customer);
		job.setOpen(true);
		job.setName(title);
		job.setProjectID(identifier);
		job.setEventJob(eventJob);
		job.getResources().addAll(Arrays.asList(employees));
        job.setStatus(status);
		return job;
	}

}
