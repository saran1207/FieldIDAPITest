package com.n4systems.ejb.legacy.impl;

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
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;


public class EntityManagerBackedUserManagerTest {

	
	@SuppressWarnings("unchecked")
	private QueryBuilder<User> queryBuilder = createMock(QueryBuilder.class);
    private QueryBuilder<Tenant> tenantQueryBuilder = createMock(QueryBuilder.class);
    private EntityManager em = createMock(EntityManager.class);
	private EntityManagerBackedUserManager fixture; 
	private TenantSettings tenantSettings = new TenantSettings();
	private TenantSettingsService tenantSettingsService = createMock(TenantSettingsService.class);
	
	@Before 
	public void setup() { 
		fixture = new EntityManagerBackedUserManager(em, tenantSettingsService) { 
			@Override
			protected <T> QueryBuilder<T> getQueryBuilder(Class<T> clazz, QueryFilter filter) {
                if (clazz == User.class) {
                    return (QueryBuilder<T>) queryBuilder;
                } else if (clazz == Tenant.class) {
                    return (QueryBuilder<T>) tenantQueryBuilder;
                }
                return null;
			};
		};
		tenantSettings = new TenantSettings();		
	}
	
	@Test
	public void testFindUserByPw_happyPath() throws Exception {
        User user = UserBuilder.aFullUser().build();

        String userID = user.getUserID();
		String tenantName = user.getTenant().getName();

        expect(tenantQueryBuilder.addWhere(Comparator.EQ, "tenantName", "name", user.getTenant().getName())).andReturn(tenantQueryBuilder);
        expect(tenantQueryBuilder.getSingleResult(em)).andReturn(user.getTenant());
		
		expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);			
		
		replay(queryBuilder, tenantQueryBuilder);
		
		User foundUser = fixture.findUserByPw(tenantName, userID, "password");
		
		assertEquals(user, foundUser);
	}
	
	
	@Test(expected=LoginException.class)
	public void testFindUserByPw_userNotFound() throws Exception {
		String userID = "derek";
		String tenantName = "n4systems";

		User user = null; // i.e. user not found.

        expect(tenantQueryBuilder.addWhere(Comparator.EQ, "tenantName", "name", tenantName)).andReturn(tenantQueryBuilder);
        Tenant tenant = new Tenant();
        tenant.setName("n4systems");
        tenant.setId(42L);
        expect(tenantQueryBuilder.getSingleResult(em)).andReturn(tenant);

        expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);

        expect(tenantSettingsService.getTenantSettings(tenant.getId())).andReturn(new TenantSettings());

        replay(queryBuilder, tenantQueryBuilder, tenantSettingsService);
		
		fixture.findUserByPw(tenantName, userID, "password");
	}	
	
	@Test(expected=LoginException.class)
	public void testFindUserByPw_userPasswordIncorrect() throws Exception {
		User user = UserBuilder.aFullUser().build();

        String userID = user.getUserID();
        String tenantName = user.getTenant().getName();

        Tenant tenant = TenantBuilder.aTenant().build();
		user.setTenant(tenant);

        expect(tenantQueryBuilder.addWhere(Comparator.EQ, "tenantName", "name", user.getTenant().getName())).andReturn(tenantQueryBuilder);
        expect(tenantQueryBuilder.getSingleResult(em)).andReturn(user.getTenant());

        expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);					
		replay(queryBuilder, tenantQueryBuilder);
		
		expect(tenantSettingsService.getTenantSettings(tenant.getId())).andReturn(new TenantSettings());
		replay(tenantSettingsService);
	
		fixture.findUserByPw(tenantName, userID, "INCORRECT_PASSWORD");
	}
		
	@Test
	public void testFindUserByPw_userLocked() {
		User user = UserBuilder.aFullUser().withLocked(true).build();

        String userID = user.getUserID();
        String tenantName = user.getTenant().getName();

        Tenant tenant = TenantBuilder.aTenant().build();
		user.setTenant(tenant);

        expect(tenantQueryBuilder.addWhere(Comparator.EQ, "tenantName", "name", user.getTenant().getName())).andReturn(tenantQueryBuilder);
        expect(tenantQueryBuilder.getSingleResult(em)).andReturn(user.getTenant());

        expect(queryBuilder.getSingleResult(em)).andReturn(user);
		expect(queryBuilder.addSimpleWhere("registered", true)).andReturn(queryBuilder);
		expect(queryBuilder.addSimpleWhere("state", EntityState.ACTIVE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);
		expect(queryBuilder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE)).andReturn(queryBuilder);			
		replay(queryBuilder, tenantQueryBuilder);
		
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
