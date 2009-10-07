package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.NoCommitAndRollBackTransaction;

public class LinkedOrgLoaderTest {

	@Test(expected=SecurityException.class)
	public void load_throws_security_exception_on_null_connection() {		
		SecurityFilter ownerFilter = new OrgOnlySecurityFilter(OrgBuilder.aPrimaryOrg().build());

		LinkedOrgLoader loader = new LinkedOrgLoader(ownerFilter, new OrgConnectionByLinkedOrgLoader(null, null) {
			@Override
			protected OrgConnection load(EntityManager em, SecurityFilter filter) {
				return null;
			}
		});
		
		loader.load(new NoCommitAndRollBackTransaction());
	}
	
}
