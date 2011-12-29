package com.n4systems.api.validation.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.api.model.UserView;
import com.n4systems.api.model.UserViewBuilder;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.security.UserType;


public class PermissionValidatorTest {
	private static final String USERID = "user";
	private PermissionValidator validator = new PermissionValidator();	
	private UserView userView;

	@Before
	public void setup() { 
		userView = UserViewBuilder.aUserView().withUserId(USERID).withAccountType(UserType.FULL.getLabel()).build();
	}
	
	@Test
	public void validate_null_assumes_no() { 
		ValidationResult result = validator.validate(null, userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null);
		assertTrue(result.isPassed());  
	}

	
	@Test
	public void test_invalid_account_type() { 
		String accountType = "bogusType";
		String userId = "joe";
		userView.setAccountType(accountType);		
		userView.setUserID(userId);
		ValidationResult result = validator.validate("Y", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null);
		String msg = String.format(FieldValidator.AccountTypeFail, accountType, userId );
		assertEquals(msg, result.getMessage());		
	}
	
	@Test
	public void test_pass_conditions() { 
		assertTrue(validator.validate("Y", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null).isPassed());
		assertTrue(validator.validate("N", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null).isPassed());
	}
	
	@Test
	public void test_validate_ignores_case() { 
		assertTrue(validator.validate("y", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null).isPassed());
		assertTrue(validator.validate("n", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null).isPassed());
	}
	
	@Test
	public void test_invalid_YorN() { 
		ValidationResult result = validator.validate("X", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null);
		String msg = String.format(FieldValidator.YNValidatorFail, "X", UserView.IDENTIFY_ASSETS_FIELD );
		assertEquals(msg, result.getMessage());
		assertFalse(result.isPassed());
		
		result = validator.validate("yn", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null);  
		msg = String.format(FieldValidator.YNValidatorFail, "yn", UserView.IDENTIFY_ASSETS_FIELD );
		assertEquals(msg, result.getMessage());
		assertFalse(result.isPassed());
		
		result = validator.validate("", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null);
		msg = String.format(FieldValidator.YNValidatorFail, "", UserView.IDENTIFY_ASSETS_FIELD );
		assertEquals(msg, result.getMessage());
		assertFalse(result.isPassed());		
	}

	@Test
	public void test_no_permissions() {
		// user has readonly permissions-shouldn't be able to IDENTIFY...
		String accountType = UserType.READONLY.getLabel();
		userView = UserViewBuilder.aUserView().withUserId(USERID).withAccountType(accountType).build();
		ValidationResult result = validator.validate("Y", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null);
		String msg = String.format(FieldValidator.PermissionTypeFail, USERID, UserView.IDENTIFY_ASSETS_FIELD, "Y");
		assertEquals(msg, result.getMessage());
		
		// this ok, because we aren't setting the permission (via "N" parameter)
		assertTrue(validator.validate("N", userView, UserView.IDENTIFY_ASSETS_FIELD, null, null, null).isPassed()); 
	}		
	
}
