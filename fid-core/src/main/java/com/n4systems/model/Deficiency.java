package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "DEFICIENCY")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Deficiency extends Observation {
	private static final long serialVersionUID = 1L;

	public Deficiency() {
		super(Type.DEFICIENCY);
	}
	
	
	public Deficiency(Tenant tenant) {
		super(Type.DEFICIENCY, tenant);
	}
}
