package com.n4systems.fieldid.permissions;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserPermissionLocatorTest {

	@Test
	public void should_find_annotation_on_class() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(PermissionRequiredAction.class);
		assertNotNull(sut.getClassPermissions());
	}
	
	@Test
	public void should_not_find_annotation_on_class() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(NoPermissionRequiredAction.class);
		assertNull(sut.getClassPermissions());
	}
	
	@Test
	public void should_find_annotation_on_method() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(PermissionRequiredAction.class);
		assertNotNull(sut.getMethodPermissions("methodWithPermission"));
	}
	
	@Test
	public void should_not_find_annotation_on_method() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(PermissionRequiredAction.class);
		assertNull(sut.getMethodPermissions("methodWithOutPermission"));
	}
	
	
	@Test
	public void should_find_a_permission_annotation_from_the_method() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(PermissionRequiredAction.class);
		assertArrayEquals(new int[]{2}, sut.getActionPermissionRequirements("methodWithPermission"));
	}
	
	@Test
	public void should_find_a_permission_annotation_from_the_class() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(PermissionRequiredAction.class);
		assertArrayEquals(new int[]{1}, sut.getActionPermissionRequirements("methodWithOutPermission"));
	}
	
	@Test
	public void should_find_have_no_permissions_required() throws Exception {
		UserPermissionLocator sut = new UserPermissionLocator(NoPermissionRequiredAction.class);
		assertArrayEquals(new int[]{}, sut.getActionPermissionRequirements("methodWithOutPermission"));
	}
	
	
	
	@SuppressWarnings("unused")
	@UserPermissionFilter(userRequiresOnOf={1})
	private class PermissionRequiredAction {
		@UserPermissionFilter(userRequiresOnOf={2})
		public void methodWithPermission() {
		}
		
		public void methodWithOutPermission() {
		}
	}
	
	@SuppressWarnings("unused")
	private class NoPermissionRequiredAction {
		public void methodWithOutPermission() {
		}
	}
}
