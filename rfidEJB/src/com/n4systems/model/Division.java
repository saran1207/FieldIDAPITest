package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//XXX - move into com.n4systems.model.division package
@Entity
@Table(name = "divisions")
public class Division extends EntityWithTenant implements NamedEntity, Listable<Long>, FilteredEntity {
	private static final long serialVersionUID = 1L;
	
	private String name;

	@ManyToOne(optional = false)
	private Customer customer;

	public Division() {}

	
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
		this.name = (name != null) ? name.trim() : null;
	}
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, "customer.id", "id", null, null);
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return getName();
	}

	@Override
    public String toString() {
	    return getName() + " (" + getId() + ")";
    }
	
}
