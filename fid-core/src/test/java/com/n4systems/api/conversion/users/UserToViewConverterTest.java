package com.n4systems.api.conversion.users;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.UserView;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;

public class UserToViewConverterTest {
	
	@Test
	public void test_to_view() throws ConversionException {
		UserToViewConverter converter = new UserToViewConverter();
		
		User model = UserBuilder.aFullUser().build();
		
		UserView view = converter.toView(model);
		
		assertNotNull(view);
		assertSame(model.getFirstName(), view.getFirstName());
		assertSame(model.getLastName(), view.getLastName());
		assertSame(model.getEmailAddress(), view.getEmailAddress());
		assertSame(model.getGlobalId(), view.getGlobalId());
		assertSame(model.getUserType(), UserType.valueFromLabel(view.getAccountType()));
		assertSame(model.getPermissions(), view.getPermissions());
		assertSame(model.getPosition(), view.getPosition());
		assertSame(model.getUserID(), view.getUserID());
	}
}
