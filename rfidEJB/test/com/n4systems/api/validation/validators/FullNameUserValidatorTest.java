package com.n4systems.api.validation.validators;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.UserByFullNameLoader;

public class FullNameUserValidatorTest {
	
	@Test
	public void validate_passes_on_null_value() { 
		FullNameUserValidator validator = new FullNameUserValidator();
		
		assertTrue(validator.validate(null, null, null, null, null).isPassed());
	}
	
	@Test
	public void validate_passes_when_one_user_found() {
		String fullName = "Mark F";
		
		final UserByFullNameLoader loader = createMock(UserByFullNameLoader.class);
		expect(loader.setFullName(fullName)).andReturn(loader);
		expect(loader.load()).andReturn(Arrays.asList(new UserBean()));
		replay(loader);
		
		FullNameUserValidator validator = new FullNameUserValidator() {
			protected UserByFullNameLoader createUserByFullNameLoader(SecurityFilter filter) {
				return loader;
			}
		};
		
		assertTrue(validator.validate(fullName, null, null, null, null).isPassed());
		verify(loader);
	}
	
	@Test
	public void validate_fails_when_no_users_found() {
		String fullName = "Mark F";
		
		final UserByFullNameLoader loader = createMock(UserByFullNameLoader.class);
		expect(loader.setFullName(fullName)).andReturn(loader);
		expect(loader.load()).andReturn(new ArrayList<UserBean>());
		replay(loader);
		
		FullNameUserValidator validator = new FullNameUserValidator() {
			protected UserByFullNameLoader createUserByFullNameLoader(SecurityFilter filter) {
				return loader;
			}
		};
		
		assertFalse(validator.validate(fullName, null, null, null, null).isPassed());
		verify(loader);
	}
	
	@Test
	public void validate_fails_when_more_than_one_user_found() {
		String fullName = "Mark F";
		
		final UserByFullNameLoader loader = createMock(UserByFullNameLoader.class);
		expect(loader.setFullName(fullName)).andReturn(loader);
		expect(loader.load()).andReturn(Arrays.asList(new UserBean(), new UserBean()));
		replay(loader);
		
		FullNameUserValidator validator = new FullNameUserValidator() {
			protected UserByFullNameLoader createUserByFullNameLoader(SecurityFilter filter) {
				return loader;
			}
		};
		
		assertFalse(validator.validate(fullName, null, null, null, null).isPassed());
		verify(loader);
	}
	
	@Test
	public void test_format_user_name_list() {
		FullNameUserValidator validator = new FullNameUserValidator();
		
		UserBuilder uBuilder = UserBuilder.anEmployee();
		UserBean mark = uBuilder.withFirstName("Mark").withLastName("Frederiksen").build();
		UserBean jesse = uBuilder.withFirstName("Jesse").withLastName("Miller").build();
		UserBean alex = uBuilder.withFirstName("Alex").withLastName("Aitken").build();
		
		String nameList = validator.formatUserNameList(Arrays.asList(mark, jesse, alex));
		
		assertEquals("'Mark Frederiksen', 'Jesse Miller', 'Alex Aitken'", nameList);
	}
}
