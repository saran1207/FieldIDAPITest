package com.n4systems.ejb.legacy.impl;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.LoginException;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class EntityManagerBackedUserManagerTest {

	
	@SuppressWarnings("unchecked")
	private QueryBuilder<User> queryBuilder = createMock(QueryBuilder.class);  
	private EntityManager em = createMock(EntityManager.class);
	private EntityManagerBackedUserManager fixture; 
	private TenantSettings tenantSettings = new TenantSettings();
	private TenantSettingsService tenantSettingsService = createMock(TenantSettingsService.class);
	
	@Before 
	public void setup() { 
		fixture = new EntityManagerBackedUserManager(em, tenantSettingsService) { 
			@Override
			protected <T> QueryBuilder<T> getQueryBuilder(Class<T> clazz, QueryFilter filter) {
				return (QueryBuilder<T>) queryBuilder;
			};
		};
		tenantSettings = new TenantSettings();		
	}
	
	@Test
	public void testFindUserByPw_happyPath() {
		String userID = "derek";
		String tenantName = "n4systems";
		
		User user = UserBuilder.aFullUser().build();
		
		expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);			
		
		replay(queryBuilder);		
		
		User foundUser = fixture.findUserByPw(tenantName, userID, "password");
		
		assertEquals(user, foundUser);
	}
	
	
	@Test(expected=LoginException.class)
	public void testFindUserByPw_userNotFound() {
		String userID = "derek";
		String tenantName = "n4systems";
		
		User user = null; // i.e. user not found.
		
		expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);			
		
		replay(queryBuilder);		
		
		fixture.findUserByPw(tenantName, userID, "password");		
	}	
	
	@Test(expected=LoginException.class)
	public void testFindUserByPw_userPasswordIncorrect() {
		String userID = "derek";
		String tenantName = "n4systems";
		
		User user = UserBuilder.aFullUser().build();
		Tenant tenant = TenantBuilder.aTenant().build();
		user.setTenant(tenant);
		
		expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);					
		replay(queryBuilder);
		
		expect(tenantSettingsService.getTenantSettings(tenant.getId())).andReturn(new TenantSettings());
		replay(tenantSettingsService);
	
		fixture.findUserByPw(tenantName, userID, "INCORRECT_PASSWORD");
	}
		
	@Test
	public void testFindUserByPw_userLocked() {
		String userID = "derek";
		String tenantName = "n4systems";
		
		User user = UserBuilder.aFullUser().withLocked(true).build();
		Tenant tenant = TenantBuilder.aTenant().build();
		user.setTenant(tenant);
		
		expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);			
		replay(queryBuilder);
		
		expect(tenantSettingsService.getTenantSettings(tenant.getId())).andReturn(new TenantSettings());
		replay(tenantSettingsService);

		try {
			fixture.findUserByPw(tenantName, userID, "password");
			fail("should throw an exception for locked user");
		} catch (LoginException e) { 
			assertTrue(e.isLocked());
		}
	}
		
}
