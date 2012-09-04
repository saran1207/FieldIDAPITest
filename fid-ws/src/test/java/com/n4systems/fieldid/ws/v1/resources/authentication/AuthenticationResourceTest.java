package com.n4systems.fieldid.ws.v1.resources.authentication;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUser;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUserResource;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertSame;

public class AuthenticationResourceTest {
	
	private AuthenticationResource fixture;
	private UserService userService;
	private ApiUserResource apiUserResource;
	private SecurityContext securityContext;
	
	@Before
	public void before() {
		fixture = new AuthenticationResource();
		userService = createMock(UserService.class);
		apiUserResource = createMock(ApiUserResource.class);
		securityContext = createMock(SecurityContext.class);
		
		fixture.userService = userService;
		fixture.apiUserResource = apiUserResource;
		fixture.securityContext = securityContext;
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
		String authKey = "abcdefg";
		
		User user = UserBuilder.anEmployee().withAuthKey(authKey).build();
		ApiUser apiUser = new ApiUser();
		
		expect(userService.authenticateUserByPassword(tenant, userId, pass)).andReturn(user);
		replay(userService);
		
		expect(apiUserResource.convertEntityToApiModel(user)).andReturn(apiUser);
		replay(apiUserResource);
		
		assertSame(apiUser, fixture.authenticate(tenant, userId, pass));
	}
}
