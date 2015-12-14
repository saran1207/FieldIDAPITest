package com.n4systems.ejb.impl;

import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.exceptions.TooManyIdentifiersException;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.services.config.ConfigServiceTestManager;
import com.n4systems.testutils.DummyEntityManager;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ConfigEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class ProofTestHandlerTest {

	private static final int MAX_IDENTIFIERS = 10;

	@Before
	public void setup_config_context() {
		ConfigServiceTestManager.setTestDouble().addConfigurationValue(ConfigEntry.MAX_SERIALS_PER_PROOFTEST, MAX_IDENTIFIERS);
	}
	
	@After
	public void reset_config_context() {
		ConfigServiceTestManager.resetInstance();
	}
	
	@Test(expected=TooManyIdentifiersException.class)
	public void throws_exception_when_max_identifiers_exceeded()
	{
		FileDataContainer fileData = new FileDataContainer();
		fileData.setIdentifiers(Collections.nCopies(MAX_IDENTIFIERS + 1, ""));
		
		ProofTestHandler handler = new ProofTestHandlerImpl(new DummyEntityManager());
		handler.eventServiceUpload(fileData, UserBuilder.aUser().build());
	}

}
