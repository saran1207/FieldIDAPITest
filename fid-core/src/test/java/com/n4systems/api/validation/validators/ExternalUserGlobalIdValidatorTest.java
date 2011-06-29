package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.api.model.AssetView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.model.UserViewBuilder;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserByUserIdLoader;
import com.n4systems.persistence.loaders.GlobalIdExistsLoader;

public class ExternalUserGlobalIdValidatorTest {
	
	@Test(expected = ClassCastException.class)
	public void test_requires_user_view() {
		ExternalUserGlobalIdValidator validator = new ExternalUserGlobalIdValidator();
		validator.validate("C", new AssetView()/*NOT UserView as expected*/, null, null, null, null);
	}
	
	@Test
	public void test_null_userId_fails() {
		ExternalUserGlobalIdValidator validator = new ExternalUserGlobalIdValidator();
		UserView userView = new UserViewBuilder().withDefaultValues().withUserId(null).build();
		ValidationResult result = validator.validate(null, userView, null, null, null, null);
		assertTrue(result.isFailed());
		assertEquals(String.format(FieldValidator.ExternalUserIdMissingValidatorFail, userView.getFirstName(), userView.getLastName()), result.getMessage());
	}
	
	@Test
	public void test_valid_add_user() {
		final UserByUserIdLoader loader = createMock(UserByUserIdLoader.class);
		ExternalUserGlobalIdValidator validator = new ExternalUserGlobalIdValidator() {
			@Override
			UserByUserIdLoader createUserByUserIdLoader(SecurityFilter filter, UserView userView) {
				return loader;
			}
		};
		UserView userView = new UserViewBuilder().withDefaultValues().build();
		expect(loader.load()).andReturn(null);
		expect(loader.setUserID(userView.getUserID())).andReturn(loader);
		replay(loader);
		ValidationResult result = validator.validate(null, userView, null, null, null, null);
		assertTrue(result.isPassed());
		verify(loader);
	}
	
	@Test
	public void test_valid_edit_existing_user() {
		final GlobalIdExistsLoader loader = createMock(GlobalIdExistsLoader.class);
		ExternalUserGlobalIdValidator validator = new ExternalUserGlobalIdValidator() {
			@Override GlobalIdExistsLoader createGlobalIdExistsLoader(SecurityFilter filter) {
				return loader;
			}
		};
		String globalId = "existingUserId";
		UserView userView = new UserViewBuilder().withDefaultValues().build();
		expect(loader.load()).andReturn(Boolean.TRUE);
		expect(loader.setGlobalId(globalId)).andReturn(loader);
		replay(loader);
		ValidationResult result = validator.validate(globalId, userView, null, null, null, null);
		assertTrue(result.isPassed());
		verify(loader);
	}
	
	@Test
	public void test_invalid_edit_existing_user() {
		final GlobalIdExistsLoader loader = createMock(GlobalIdExistsLoader.class);
		ExternalUserGlobalIdValidator validator = new ExternalUserGlobalIdValidator() {
			@Override GlobalIdExistsLoader createGlobalIdExistsLoader(SecurityFilter filter) {
				return loader;
			}
		};
		String globalId = "existingUserId";
		UserView userView = new UserViewBuilder().withDefaultValues().build();
		expect(loader.load()).andReturn(Boolean.FALSE);
		expect(loader.setGlobalId(globalId)).andReturn(loader);
		replay(loader);
		ValidationResult result = validator.validate(globalId, userView, null, null, null, null);
		assertTrue(result.isFailed());
		assertEquals(String.format(FieldValidator.ExternalUserGlobalIdValidatorFail,userView.getFirstName(), userView.getLastName(), globalId), result.getMessage());
		verify(loader);
	}
	
}
