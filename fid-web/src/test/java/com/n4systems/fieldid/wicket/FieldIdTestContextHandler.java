package com.n4systems.fieldid.wicket;

import org.apache.wicket.Session;

import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;

public class FieldIdTestContextHandler implements TestContextHandler<FieldIdWicketTestContext> {

	@Override
	public void initializeContext(Session session, FieldIdWicketTestContext tc) {
		FieldIDSession fieldIdSession = (FieldIDSession) session;
		User user = null;
		if (tc.getUser()!=null) { 
			user = UserBuilder.aFullUser().withPermissions(tc.getUser().getPermission()).build();
			user.getOwner().getPrimaryOrg().setExtendedFeatures(tc.getUser().getExtendedFeatures());			
		} else { 
			user = UserBuilder.anAdminUser().build();
		}
		fieldIdSession.setUser(user);
	}

}
