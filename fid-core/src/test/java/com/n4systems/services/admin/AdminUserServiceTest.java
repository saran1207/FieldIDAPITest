package com.n4systems.services.admin;

import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.admin.AdminUserType;
import com.n4systems.fieldid.service.SecurityService;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.persistence.QueryBuilder;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class AdminUserServiceTest extends FieldIdServiceTest {

	@TestTarget private AdminUserService adminUserService;
	@TestMock 	private PersistenceService persistenceService;
	@TestMock 	private SecurityService securityService;

	@Test
	public void test_createAdminUser() {
		char[] salt = new char[] {'a', 'b', 'c'};
		char[] pass = new char[] {'d', 'e', 'f'};

		expect(securityService.generateSalt(64)).andReturn(salt);
		expect(securityService.hashSaltedPassword("helloworld", salt)).andReturn(pass);

		expect(persistenceService.save(anyObject(AdminUser.class))).andReturn(1L);
		replay(persistenceService, securityService);

		AdminUser user = adminUserService.createAdminUser("test@test.com", "helloworld", AdminUserType.SUPER);

		assertTrue(user.isEnabled());
		assertEquals("test@test.com", user.getEmail());
		assertEquals(AdminUserType.SUPER, user.getType());
		assertArrayEquals(salt, user.getSalt());
		assertArrayEquals(pass, user.getPassword());

		verify(persistenceService, securityService);
	}

	@Test
	public void test_authenticateUser_returns_null_when_user_not_found() {
		expect(persistenceService.find(anyObject(QueryBuilder.class))).andReturn(null);

		replay(persistenceService, securityService);

		assertNull(adminUserService.authenticateUser("mark@mark.com", ""));

		verify(persistenceService, securityService);
	}

	@Test
	public void test_authenticateUser_returns_null_on_bad_password() {
		char[] salt = new char[] {'a', 'b', 'c'};
		char[] pass = new char[] {'d', 'e', 'f'};

		AdminUser user = new AdminUser();
		user.setSalt(salt);
		user.setPassword(pass);

		expect(persistenceService.find(anyObject(QueryBuilder.class))).andReturn(user);
		expect(securityService.hashSaltedPassword("asdf", salt)).andReturn(new char[] {'z', 'x', 'c'});
		replay(persistenceService, securityService);

		assertNull(adminUserService.authenticateUser("mark@mark.com", "asdf"));

		verify(persistenceService, securityService);
	}

	@Test
	public void test_authenticateUser_returns_user_on_password_match() {
		char[] salt = new char[] {'a', 'b', 'c'};
		char[] pass = new char[] {'d', 'e', 'f'};

		AdminUser user = new AdminUser();
		user.setSalt(salt);
		user.setPassword(pass);

		expect(persistenceService.find(anyObject(QueryBuilder.class))).andReturn(user);
		expect(securityService.hashSaltedPassword("asdf", salt)).andReturn(pass);
		replay(persistenceService, securityService);

		assertSame(adminUserService.authenticateUser("mark@mark.com", "asdf"), user);

		verify(persistenceService, securityService);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_authenticateUser_throws_exception_on_null_email() {
		replay(persistenceService, securityService);
		adminUserService.authenticateUser(null, "asdf");
		verify(persistenceService, securityService);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_authenticateUser_throws_exception_on_null_pass() {
		replay(persistenceService, securityService);
		adminUserService.authenticateUser("asd", null);
		verify(persistenceService, securityService);
	}
}
