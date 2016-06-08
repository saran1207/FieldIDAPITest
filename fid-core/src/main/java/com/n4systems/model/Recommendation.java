package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "RECOMMENDATION")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Recommendation extends Observation {
	private static final long serialVersionUID = 1L;

	public Recommendation() {
		super(Type.RECOMMENDATION);
	}

	public Recommendation(Tenant tenant) {
		super(Type.RECOMMENDATION, tenant);
	}
}
