package com.n4systems.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

@Entity(name="predefinedlocations")
public class PredefinedLocation extends EntityWithTenant implements NamedEntity {

	@Column(nullable=false, length=255)
	private String name;

	@ManyToOne
	private PredefinedLocation parent;
	
	
	public PredefinedLocation() {
		super();
	}

	
	public PredefinedLocation(Tenant tenant, PredefinedLocation parent) {
		super(tenant);
		this.parent = parent;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	

	public String getName() {
		return name;
	}

	
	public PredefinedLocation getParent() {
		return parent;
	}
}
