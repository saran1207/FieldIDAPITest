package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name="jobsites" )
public class JobSite extends EntityWithTenant implements NamedEntity, Listable<Long>, FilteredEntity {

	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "r_customer")
	private Customer customer;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "r_division")
	private Division division; 
	
	public JobSite() {}
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, "customer.id", "division.id", null, null);
	}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	

	private void trimName() {
		name = (name != null) ? name.trim() : null;
	}
	
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getDisplayName() {
		return getName();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer( Customer customer ) {
		this.customer = customer;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision( Division division ) {
		this.division = division;
	}

	@Override
    public String toString() {
	    return getName() + " (" + getId() + ")";
    }

}
