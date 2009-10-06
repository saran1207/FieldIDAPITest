package com.n4systems.commandprocessors;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.classextension.EasyMock.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.commandprocessors.CreateSafetyNetworkConnectionCommandProcessor;
import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.n4systems.testutils.NoCommitAndRollBackTransaction;
import com.n4systems.testutils.TestDoubleNonSecuredLoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.NonDataSourceBackedConfigContext;




public class CreateSafetyNetworkConnectionCommandProcessorTest {

	@SuppressWarnings("unchecked")
	@Test public void should_create_new_connection() {
		PrimaryOrg primary1 = aPrimaryOrg().build();
		PrimaryOrg primary2 = aPrimaryOrg().build();
		
		UserBean user = anEmployee().withOwner(primary1).build();
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(primary1, primary2);
		
		ConfigContext.setCurrentContext(new NonDataSourceBackedConfigContext());
		
		NonSecureIdLoader<BaseOrg> mockLoader = createMock(NonSecureIdLoader.class);
		expect(mockLoader.setId(primary1.getId())).andReturn(mockLoader);
		expect(mockLoader.load((Transaction)anyObject())).andReturn(primary1);
		expect(mockLoader.setId(primary2.getId())).andReturn(mockLoader);
		expect(mockLoader.load((Transaction)anyObject())).andReturn(primary2);
		replay(mockLoader);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.add(BaseOrg.class, mockLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(ConfigContext.getCurrentContext());
		
		sut.setActor(user).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(command, new NoCommitAndRollBackTransaction());
		
		verify(mockLoader);
	}

	private CreateSafetyNetworkConnectionMessageCommand createCommand(PrimaryOrg primary1, PrimaryOrg primary2) {
		CreateSafetyNetworkConnectionMessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
		command.setCustomerOrgId(primary1.getId());
		command.setVendorOrgId(primary2.getId());
		return command;
	}

}
