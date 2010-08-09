package com.n4systems.model.security;

import static com.n4systems.model.builders.CustomerOrgBuilder.*;
import static com.n4systems.model.builders.DivisionOrgBuilder.*;
import static com.n4systems.model.builders.SecondaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;



public class UserSecurityFilterTest {
	
	@Test public void should_apply_only_tenant_user_belongs_to_primary_org() throws Exception {
		User user = anEmployee().build();
		QueryBuilder<User> queryBuilder = new TestingQueryBuilder<User>(User.class);
		
		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		for (String filter_name : queryBuilder.getWhereParameters().keySet()) {
			if (filter_name.startsWith("filter_owner")) {
				fail("an owner filter was created with name " + filter_name);
			}
		}
	}
	
	@Test public void should_apply_tenant_and_customer_user_belongs_to_a_primary_customer() throws Exception {
		User user = aCustomerUser().withOwner(aPrimaryCustomerOrg().build()).build();
		QueryBuilder<User> queryBuilder = new TestingQueryBuilder<User>(User.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_customerOrg_id"));
	}
	
	
	@Test public void should_apply_tenant_and_owner_division_user_belongs_to_a_primary_division() throws Exception {
		User user = aCustomerUser().withOwner(aPrimaryDivisionOrg().build()).build();
		QueryBuilder<User> queryBuilder = new TestingQueryBuilder<User>(User.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_divisionOrg_id"));
	}
	
	
	@Test public void should_apply_tenant_and_customer_user_belongs_to_a_secondary_customer() throws Exception {
		User user = aCustomerUser().withOwner(aSecondaryCustomerOrg().build()).build();
		QueryBuilder<User> queryBuilder = new TestingQueryBuilder<User>(User.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_customerOrg_id"));
	}
	
	
	@Test public void should_apply_tenant_and_owner_division_user_belongs_to_a_secondary_division() throws Exception {
		User user = aCustomerUser().withOwner(aSecondaryDivisionOrg().build()).build();
		QueryBuilder<User> queryBuilder = new TestingQueryBuilder<User>(User.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_divisionOrg_id"));
	}
	
	@Test public void should_apply_tenant_and_owner_organization_and_include_primary_orgs_user_belongs_to_a_secondary_org() throws Exception {
		User user = anEmployee().withOwner(aSecondaryOrg().build()).build();
		QueryBuilder<User> queryBuilder = new TestingQueryBuilder<User>(User.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_secondaryOrg_id"));
		
		assertEquals(Comparator.EQ_OR_NULL, ((WhereParameter<?>)queryBuilder.getWhereParameter("filter_owner_secondaryOrg_id")).getComparator());
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_state_for_primary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(anEmployee().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", "state");
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.state = :filter_state AND p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_for_primary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(anEmployee().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_for_primary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(anEmployee().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_for_primary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(anEmployee().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", null, null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_state_for_secondary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aSecondaryUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", "state");
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.state = :filter_state AND p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_for_secondary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aSecondaryUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_for_secondary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aSecondaryUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND (p.owner.id.secondaryOrg.id = :filter_owner_id_secondaryOrg_id OR p.owner.id.secondaryOrg.id IS NULL)", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_for_secondary_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aSecondaryUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", null, null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_state_for_customer_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aCustomerUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", "state");
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.state = :filter_state AND p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_for_customer_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aCustomerUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_for_customer_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aCustomerUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND p.owner.id.customerOrg.id = :filter_owner_id_customerOrg_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_for_customer_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aCustomerUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", null, null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_state_for_division_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aDivisionUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", "state");
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.state = :filter_state AND p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_user_for_division_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aDivisionUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", "user.id", null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND p.user.id = :filter_user_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_owner_for_division_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aDivisionUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", "owner.id", null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id AND p.owner.id.divisionOrg.id = :filter_owner_id_divisionOrg_id", clause);
	}
	
	@Test
	public void test_where_clause_with_tenant_for_division_org_user() throws Exception {
		SecurityFilter filter = new UserSecurityFilter(aDivisionUser().build());
		
		SecurityTestEntity.securityDefiner = new SecurityDefiner("tenant.id", null, null, null);
		
		String clause = filter.produceWhereClause(SecurityTestEntity.class, "p");
		assertEquals("p.tenant.id = :filter_tenant_id", clause);
	}
}
