package com.n4systems.util.persistence.search.terms;

import static org.junit.Assert.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;


import org.junit.Test;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.util.persistence.WhereParameter;


public class OwnerTermTest {

	@Test
	public void should_not_produce_a_where_clause_with_primary_org() {
		PrimaryOrg primaryOrg = aPrimaryOrg().build();
		OwnerTerm sut = new OwnerTerm("owner", primaryOrg);
		
		WhereParameter<?> whereParameter = sut.getWhereParameter();
		
		assertNull(whereParameter);
	}
}
