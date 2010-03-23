package com.n4systems.fieldid.actions.subscriptions.view.model;

import static java.lang.Boolean.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * We allow a mixture of leters and number and dashes.  the first and last characters can not be dashes.
 * @author aaitken
 *
 */
@RunWith(value=Parameterized.class)
public class TenantNameValidationTest {

	private static final Boolean INVALID = FALSE;
	private static final Boolean VALID = TRUE;
	
	
	private String tenantName;
	private Boolean valid;
	
	
	@Parameters 
	public static Collection<Object[]> paramaters() {
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		
		data.add(new Object[] {"123", VALID});
		data.add(new Object[] {"abc", VALID});
		data.add(new Object[] {"a1b", VALID});
		data.add(new Object[] {"a12", VALID});
		data.add(new Object[] {"thisIsMyGreatTenantName", VALID});
		data.add(new Object[] {"this-one-has-dashes", VALID});
		
		data.add(new Object[] {"-dashInTheBeginning", INVALID});
		data.add(new Object[] {"dashInTheEnd-", INVALID});
		
		data.add(new Object[] {"a_b", INVALID});
		data.add(new Object[] {"a&b", INVALID});
		data.add(new Object[] {"a%b", INVALID});
		data.add(new Object[] {"a/b", INVALID});
		data.add(new Object[] {"a$b", INVALID});
		data.add(new Object[] {"a.b", INVALID});
		data.add(new Object[] {"a=b", INVALID});
		
		data.add(new Object[] {"msachesnut.msanet.fieldid.com", INVALID});
		
		
		return data;
	}
	
	
	

	public TenantNameValidationTest(String tenantName, Boolean valid) {
		super();
		this.tenantName = tenantName;
		this.valid = valid;
	}




	@Test
	public void test_for_valid_tenant_name() throws Exception {
		SignUpRequestDecorator sut = new SignUpRequestDecorator(null, null);
		
		sut.setTenantName(tenantName);
		
		assertThat(sut.isValidTenantName(), is(valid));
		
	}
}
