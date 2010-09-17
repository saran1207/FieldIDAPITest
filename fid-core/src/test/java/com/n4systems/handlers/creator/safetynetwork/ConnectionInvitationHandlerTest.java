package com.n4systems.handlers.creator.safetynetwork;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;

import java.net.URI;


import org.junit.Test;

import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.NullNotifier;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.NonDataSourceBackedConfigContext;
import com.n4systems.util.uri.ActionURLBuilder;

public class ConnectionInvitationHandlerTest {
	
	private ConnectionInvitationHandler handler;
	private MessageSaver saver;
	private User adminUser;
	private PrimaryOrg remoteOrg;
	private PrimaryOrg localOrg;
	
	private static final String BASE_URI = "https://n4.fielid.com/";
	
	public void setUp() {
		saver = createMock(MessageSaver.class);
		handler = new ConnectionInvitationHandler(saver, new NullNotifier(), getAdminLoader(), getActionURLBuilder());
		adminUser = anAdminUser().build();
		remoteOrg = aPrimaryOrg().withName("receiving org").build();
		localOrg = aPrimaryOrg().withName("sending org").build();
	}
	
	
	@Test
	public void testname() throws Exception {
		
	}
	
	private AdminUserListLoader getAdminLoader() {
		AdminUserListLoader adminLoader = createMock(AdminUserListLoader.class);
		
		expect(adminLoader.load((Transaction)anyObject())).andReturn(new FluentArrayList<User>(adminUser));
		replay(adminLoader);
		return adminLoader;
	}
	
	private ActionURLBuilder getActionURLBuilder() {
		return new ActionURLBuilder(URI.create(BASE_URI), new NonDataSourceBackedConfigContext());
	}
	
}
