package com.n4systems.fieldid.permissions;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.security.Permissions;
import org.junit.Test;
import rfid.web.helper.SessionUser;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UserAccessControllerTest {

	
	
	@Test
	public void should_allow_access_to_action_when_there_is_no_permissions_required() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction());
		assertTrue(sut.userHasAccessToAction("doSomeActionSameAsClassPermissions"));
	}
	
	@Test
	public void should_deny_access_to_action_when_user_does_not_have_the_permission() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction());
		assertFalse(sut.userHasAccessToAction("doSinglePermissionRequiredAction"));
	}
	
	
	@Test
	public void should_allow_access_to_action_when_user_has_the_permission() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction(Permissions.CREATE_EVENT));
		assertTrue(sut.userHasAccessToAction("doSinglePermissionRequiredAction"));
	}
	
	@Test
	public void should_allow_access_to_action_when_user_has_on_of_the_requied_permission() throws Exception {
		UserAccessController sut = new UserAccessController(new SampleAction(Permissions.CREATE_EVENT));
		assertTrue(sut.userHasAccessToAction("doMultiPermissionRequiredAction"));
	}
	
	@Test
	public void should_allow_anyone_access() throws Exception {
		UserAccessController noPermissionUser = new UserAccessController(new SampleAction(Permissions.NO_PERMISSIONS));
		assertTrue(noPermissionUser.userHasAccessToAction("doSomeActionThatIsAlwaysOpenToEveryUser"));
		
		UserAccessController adminUser = new UserAccessController(new SampleAction(Permissions.ADMIN));
		assertTrue(adminUser.userHasAccessToAction("doSomeActionThatIsAlwaysOpenToEveryUser"));
		
	}
	
	@Test 
	public void should_deny_access_to_any_user() {
		UserAccessController noPermissionUser = new UserAccessController(new SampleAction(Permissions.ADMIN));
		assertFalse(noPermissionUser.userHasAccessToAction("doSomeActionThatIsBlocksEveryOne"));
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
		
		public String doSomeActionSameAsClassPermissions() {
			return SUCCESS;
		}
		
		@UserPermissionFilter(userRequiresOneOf={Permissions.CREATE_EVENT})
		public String doSinglePermissionRequiredAction() {
			return SUCCESS;
		}
		
		@UserPermissionFilter(userRequiresOneOf={Permissions.TAG, Permissions.CREATE_EVENT, Permissions.EDIT_EVENT})
		public String doMultiPermissionRequiredAction() {
			return SUCCESS;
		}
		
		@UserPermissionFilter(open=true)
		public String doSomeActionThatIsAlwaysOpenToEveryUser() {
			return SUCCESS;
		}
		
		@UserPermissionFilter()
		public String doSomeActionThatIsBlocksEveryOne() {
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
