package com.n4systems.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "DEFICIENCY")
public class Deficiency extends Observation {
	private static final long serialVersionUID = 1L;

	public Deficiency() {
		super(Type.DEFICIENCY);
	}
	
	
	public Deficiency(Tenant tenant) {
		super(Type.DEFICIENCY, tenant);
	}
}
