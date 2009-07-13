package com.n4systems.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "nontenant")
public class NonTenantOrganization extends Organization {

	private static final long serialVersionUID = 1L;

}
