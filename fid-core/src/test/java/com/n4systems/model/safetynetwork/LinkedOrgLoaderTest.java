package com.n4systems.model.safetynetwork;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.testutils.NoCommitAndRollBackTransaction;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

public class LinkedOrgLoaderTest {

	@Test(expected=SecurityException.class)
	public void load_throws_security_exception_on_null_connection() {		
		SecurityFilter ownerFilter = new OrgOnlySecurityFilter(OrgBuilder.aPrimaryOrg().build());

		LinkedOrgLoader loader = new LinkedOrgLoader(ownerFilter, new OrgConnectionByLinkedOrgLoader(null, null) {
			@Override
			protected List<OrgConnection> load(EntityManager em, SecurityFilter filter) {
				return null;
			}
		});
		
		loader.load(new NoCommitAndRollBackTransaction());
	}
	
}
