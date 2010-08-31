package com.n4systems.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "RECOMMENDATION")
public class Recommendation extends Observation {
	private static final long serialVersionUID = 1L;

	public Recommendation() {
		super(Type.RECOMMENDATION);
	}

	public Recommendation(Tenant tenant) {
		super(Type.RECOMMENDATION, tenant);
	}
}
