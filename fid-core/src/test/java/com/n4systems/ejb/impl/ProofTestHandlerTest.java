package com.n4systems.ejb.impl;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.exceptions.TooManySerialsException;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.testutils.DummyEntityManager;
import com.n4systems.testutils.TestConfigContext;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ConfigEntry;

public class ProofTestHandlerTest {
	private static final int MAX_SERIALS = 10;

	@Before
	public void setup_config_context() {
		TestConfigContext.newContext().setEntry(ConfigEntry.MAX_SERIALS_PER_PROOFTEST, MAX_SERIALS);
	}
	
	@After
	public void reset_config_context() {
		TestConfigContext.resetToDefaultContext();
	}
	
	@Test(expected=TooManySerialsException.class)
	public void throws_exception_when_max_serials_exceeded()
	{
		FileDataContainer fileData = new FileDataContainer();
		fileData.setSerialNumbers(Collections.nCopies(MAX_SERIALS + 1, ""));
		
		ProofTestHandler handler = new ProofTestHandlerImpl(new DummyEntityManager());
		handler.eventServiceUpload(fileData, UserBuilder.aUser().build());
	}
}
