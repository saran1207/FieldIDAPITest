package com.n4systems.fieldid.permissions;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.security.Permissions;


public class UserAccessControllerTest {

	
	
	@Test
	public void should_allow_access_to_action_when_there_is_no_permissions_required() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction());
		assertTrue(sut.userHasAccessToAction("doSomeAction"));
	}
	
	@Test
	public void should_deny_access_to_action_when_user_does_not_have_the_permission() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction());
		assertFalse(sut.userHasAccessToAction("doSinglePermissionRequiredAction"));
	}
	
	
	@Test
	public void should_allow_access_to_action_when_user_has_the_permission() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction(Permissions.CreateInspection));
		assertTrue(sut.userHasAccessToAction("doSinglePermissionRequiredAction"));
	}
	
	@Test
	public void should_allow_access_to_action_when_user_has_on_of_the_requied_permission() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction(Permissions.CreateInspection));
		assertTrue(sut.userHasAccessToAction("doMultiPermissionRequiredAction"));
	}
	
	
	@SuppressWarnings("unused")
	private class SampleAction extends AbstractAction {

		private int permissions = 0;
		
		private SampleAction() {
			super(null);
		}
		
		private SampleAction(int permissions) {
			super(null);
			this.permissions = permissions;
		}
		
		public String doSomeAction() {
			return SUCCESS;
		}
		
		@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
		public String doSinglePermissionRequiredAction() {
			return SUCCESS;
		}
		
		@UserPermissionFilter(userRequiresOneOf={Permissions.Tag, Permissions.CreateInspection, Permissions.EditInspection})
		public String doMultiPermissionRequiredAction() {
			return SUCCESS;
		}

		@Override
		public SessionUser getSessionUser() {
			SessionUser mock = createMock(SessionUser.class);
			expect(mock.getPermissions()).andReturn(permissions);
			replay(mock);
			return mock;
			
		}
	}
	
	

}
