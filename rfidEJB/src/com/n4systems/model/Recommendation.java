package com.n4systems.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@DiscriminatorValue(value = "RECOMMENDATION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Recommendation extends Observation {
	private static final long serialVersionUID = 1L;

	public Recommendation() {
		super(Type.RECOMMENDATION);
	}

	public Recommendation(Tenant tenant) {
		super(Type.RECOMMENDATION, tenant);
	}
}
