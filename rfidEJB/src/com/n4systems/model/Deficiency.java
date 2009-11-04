package com.n4systems.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@DiscriminatorValue(value = "DEFICIENCY")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Deficiency extends Observation {
	private static final long serialVersionUID = 1L;

	public Deficiency() {
		super(Type.DEFICIENCY);
	}
	
	
	public Deficiency(Tenant tenant) {
		super(Type.DEFICIENCY, tenant);
	}
}
