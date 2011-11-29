package com.n4systems.fieldid.ws.v1.resources;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.models.ApiUser;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class AuthenticationResourceTest {
	
	private AuthenticationResource fixture;
	private UserService userService;
	
	@Before
	public void before() {
		fixture = new AuthenticationResource();
		userService = createMock(UserService.class);

		fixture.setUserService(userService);
	}
	
	@Test(expected = ForbiddenException.class)
	public void authenticate_throws_forbidden_on_null_tenant() {
		replay(userService);
		fixture.authenticate(null, "asd", "asd");
	}
	
	@Test(expected = ForbiddenException.class)
	public void authenticate_throws_forbidden_on_null_user() {
		replay(userService);
		fixture.authenticate("asd", null, "asd");
	}
	
	@Test(expected = ForbiddenException.class)
	public void authenticate_throws_forbidden_on_null_pass() {
		replay(userService);
		fixture.authenticate("asd", "asd", null);
	}
	
	@Test(expected = ForbiddenException.class)
	public void authenticate_throws_forbidden_when_user_not_found() {
		String tenant = "tenant";
		String user = "user";
		String pass = "pass";
		
		expect(userService.authenticateUserByPassword(tenant, user, pass)).andReturn(null);
		replay(userService);
		
		fixture.authenticate(tenant, user, pass);
	}
	
	@Test
	public void authenticate_returns_api_user_when_found() {
		String tenant = "tenant";
		String userId = "user";
		String pass = "pass";
		
		User user = UserBuilder.anEmployee().build();
		
		expect(userService.authenticateUserByPassword(tenant, userId, pass)).andReturn(user);
		replay(userService);
		
		ApiUser apiUser = fixture.authenticate(tenant, userId, pass);
		
		assertEquals(user.getId(), apiUser.getId());
		assertEquals(user.isActive(), apiUser.isActive());
		assertEquals(user.getOwner().getId(), apiUser.getOwnerId());
		assertEquals(user.getUserID(), apiUser.getUserId());
		assertEquals(user.getDisplayName(), apiUser.getName());
		assertEquals(user.getHashPassword(), apiUser.getHashPassword());
		assertEquals(user.getHashSecurityCardNumber(), apiUser.getHashSecurityCardNumber());
		assertEquals(user.getAuthKey(), apiUser.getAuthKey());
		assertEquals(user.getUserType().name(), apiUser.getUserType());
		assertEquals(Permissions.hasOneOf(user, Permissions.CreateEvent), apiUser.getPermissions().isCreateEvent());
		assertEquals(Permissions.hasOneOf(user, Permissions.EditEvent), apiUser.getPermissions().isEditEvent());
		assertEquals(Permissions.hasOneOf(user, Permissions.Tag), apiUser.getPermissions().isIdentify());	
	}
}
