package com.n4systems.model.security;

import static com.n4systems.model.builders.CustomerOrgBuilder.*;
import static com.n4systems.model.builders.DivisionOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.junit.Assert.*;
import static com.n4systems.model.builders.SecondaryOrgBuilder.*;


import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;



public class UserSecurityFilterTest {

	@Test public void should_apply_only_tenant_user_belongs_to_primary_org() throws Exception {
		UserBean user = anEmployee().build();
		QueryBuilder<UserBean> queryBuilder = new TestingQueryBuilder<UserBean>(UserBean.class);
		
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
		UserBean user = aCustomerUser().withOwner(aPrimaryCustomerOrg().build()).build();
		QueryBuilder<UserBean> queryBuilder = new TestingQueryBuilder<UserBean>(UserBean.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_customerOrg_id"));
	}
	
	
	@Test public void should_apply_tenant_and_owner_division_user_belongs_to_a_primary_division() throws Exception {
		UserBean user = aCustomerUser().withOwner(aPrimaryDivisionOrg().build()).build();
		QueryBuilder<UserBean> queryBuilder = new TestingQueryBuilder<UserBean>(UserBean.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_divisionOrg_id"));
	}
	
	
	@Test public void should_apply_tenant_and_customer_user_belongs_to_a_secondary_customer() throws Exception {
		UserBean user = aCustomerUser().withOwner(aSecondaryCustomerOrg().build()).build();
		QueryBuilder<UserBean> queryBuilder = new TestingQueryBuilder<UserBean>(UserBean.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_customerOrg_id"));
	}
	
	
	@Test public void should_apply_tenant_and_owner_division_user_belongs_to_a_secondary_division() throws Exception {
		UserBean user = aCustomerUser().withOwner(aSecondaryDivisionOrg().build()).build();
		QueryBuilder<UserBean> queryBuilder = new TestingQueryBuilder<UserBean>(UserBean.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_divisionOrg_id"));
	}
	
	@Test public void should_apply_tenant_and_owner_organization_and_include_primary_orgs_user_belongs_to_a_secondary_org() throws Exception {
		UserBean user = anEmployee().withOwner(aSecondaryOrg().build()).build();
		QueryBuilder<UserBean> queryBuilder = new TestingQueryBuilder<UserBean>(UserBean.class);

		AbstractSecurityFilter sut = new UserSecurityFilter(user);
		
		sut.applyFilter(queryBuilder);
		
		assertEquals(user.getTenant().getId(), queryBuilder.getWhereParameterValue("filter_tenant_id"));
		assertEquals(user.getOwner().getId(), queryBuilder.getWhereParameterValue("filter_owner_secondaryOrg_id"));
		assertEquals(Comparator.EQ_OR_NULL, queryBuilder.getWhereParameter("filter_owner_secondaryOrg_id").getComparator());
	}
	
	
	
	
}
