package com.n4systems.handlers.creator.safetynetwork;

import static com.n4systems.model.builders.PrimaryOrgBuilder.aPrimaryOrg;
import static com.n4systems.model.builders.UserBuilder.anAdminUser;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.messages.Message;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.NullNotifier;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.testutils.DummyTransaction;

public class CreateConnectionHandlerTest {
	
	private User adminUser;
	private PrimaryOrg remoteOrg;
	private PrimaryOrg localOrg;
	private Transaction transaction;
	private OrgConnectionSaver saver;
	private OrgConnectionExistsLoader loader;
	
	@Before
	public void setUp() {
		saver = createMock(OrgConnectionSaver.class);
		loader = createMock(OrgConnectionExistsLoader.class);
		adminUser = anAdminUser().build();
		remoteOrg = aPrimaryOrg().withName("receiving org").build();
		localOrg = aPrimaryOrg().withName("sending org").build();
		transaction = new DummyTransaction();
	}
	
	@Test
	public void create_connection_test() throws Exception {
		CreateConnectionHandler handler = getHandler();
		
		saver.save(eq(transaction), isA(OrgConnection.class));
		replay(saver);
		
		handler.withMessage(createMessage()).create(transaction);
		
		verify(saver);
		verify(loader);
	}
	
	private CreateConnectionHandler getHandler() {
		return new CreateConnectionHandler(saver, new NullNotifier(), getNonSecureLoaderFactory());
	}

	private NonSecureLoaderFactory getNonSecureLoaderFactory() {
		NonSecureLoaderFactory factory =  new NonSecureLoaderFactory() {
			@Override
			public OrgConnectionExistsLoader createOrgConnectionExistsLoader() {
				return loader;
			}
		};
		expect(loader.setCustomerId(localOrg.getId())).andReturn(loader);
		expect(loader.setVendorId(remoteOrg.getId())).andReturn(loader);
		expect(loader.load(transaction)).andReturn(false);
		replay(loader);
		return factory;
	}

	private Message createMessage() {
		Message message = new Message();
		message.setSender(localOrg);
		message.setRecipient(remoteOrg);
		message.setSubject("test");
		message.setBody("test");
		message.setVendorConnection(true);
		message.setModifiedBy(adminUser);
		return message;
	}
	

}
