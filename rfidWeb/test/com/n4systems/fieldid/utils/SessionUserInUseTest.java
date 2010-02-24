package com.n4systems.fieldid.utils;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;


public class SessionUserInUseTest {

	
	
	private static final String A_SESSION_ID_1 = "D58B2BC9AC59D705E7B767829FB0071A";
	private static final String A_SESSION_ID_2 = "705E7B767829FB0D58B2BC9AC59D071A";

	@Test
	public void should_find_that_session_is_not_being_used_by_your_session_when_there_is_no_active_session() throws Exception {
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(null);
		long usersId = 1L;
		
		SessionUserInUse sut = new SessionUserInUse(loader);
			
		
		assertFalse(sut.doesActiveSessionBelongTo(usersId, A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_find_that_session_is_not_being_used_by_your_session_when_the_active_session_belongs_to_a_different_session_id() throws Exception {
		UserBean user = aUser().build();
		
		ActiveSession activeSession = new ActiveSession(user, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader);
			
		assertFalse(sut.doesActiveSessionBelongTo(user.getId(), A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_find_that_session_is_being_used_by_your_session_when_the_active_session_has_the_same_session_id() throws Exception {
		UserBean user = aUser().build();
		
		ActiveSession activeSession = new ActiveSession(user, A_SESSION_ID_1);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader);
			
		assertTrue(sut.doesActiveSessionBelongTo(user.getId(), A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_request_loader_to_retrieve_the_active_session_for_the_user_id_passed_in() throws Exception {
		long userIdentifier = 1L;
		ActiveSessionLoader loader = createMock(ActiveSessionLoader.class);
		expect(loader.setId(userIdentifier)).andReturn(loader);
		expect(loader.load()).andReturn(null);
		replay(loader);
		
		SessionUserInUse sut = new SessionUserInUse(loader);
			
		sut.doesActiveSessionBelongTo(userIdentifier, A_SESSION_ID_1);
		
		verify(loader);
	}
	
	
	
	@Test
	public void should_find_that_a_system_users_owns_the_active_session_when_the_active_session_is_their_session() throws Exception {
		UserBean systemUser = aSystemUser().build();
		
		ActiveSession activeSession = new ActiveSession(systemUser, A_SESSION_ID_1);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader);
			
		assertTrue(sut.doesActiveSessionBelongTo(systemUser.getId(), A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_find_that_a_system_users_owns_the_active_session_when_the_active_session_is_not_their_session() throws Exception {
		UserBean systemUser = aSystemUser().build();
		
		ActiveSession activeSession = new ActiveSession(systemUser, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader);
			
		assertTrue(sut.doesActiveSessionBelongTo(systemUser.getId(), A_SESSION_ID_1));
	}

	private IdLoader<Loader<ActiveSession>> createActiveSessionLoader(ActiveSession activeSession) {
		ActiveSessionLoader loader = createMock(ActiveSessionLoader.class);
		expect(loader.setId(anyLong())).andReturn(loader);
		expect(loader.load()).andReturn(activeSession);
		replay(loader);
		
		return loader; 
	}
}
