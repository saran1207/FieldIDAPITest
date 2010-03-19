package com.n4systems.fieldid.utils;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.StoppedClock;
import com.n4systems.util.time.SystemClock;


public class SessionUserInUseTest {

	
	
	private static final String A_SESSION_ID_1 = "D58B2BC9AC59D705E7B767829FB0071A";
	private static final String A_SESSION_ID_2 = "705E7B767829FB0D58B2BC9AC59D071A";

	@Test
	public void should_find_that_session_is_not_being_used_by_your_session_when_there_is_no_active_session() throws Exception {
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(null);
		long usersId = 1L;
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
			
		
		assertFalse(sut.doesActiveSessionBelongTo(usersId, A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_find_that_session_is_not_being_used_by_your_session_when_the_active_session_belongs_to_a_different_session_id() throws Exception {
		UserBean user = aUser().build();
		
		ActiveSession activeSession = new ActiveSession(user, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
			
		assertFalse(sut.doesActiveSessionBelongTo(user.getId(), A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_find_that_session_is_being_used_by_your_session_when_the_active_session_has_the_same_session_id() throws Exception {
		UserBean user = aUser().build();
		
		ActiveSession activeSession = new ActiveSession(user, A_SESSION_ID_1);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
			
		assertTrue(sut.doesActiveSessionBelongTo(user.getId(), A_SESSION_ID_1));
	}
	
	@Test 
	public void should_find_that_the_session_does_not_belong_to_you_if_it_has_expired() {
		NonDataSourceBackedConfigContext configContext = new NonDataSourceBackedConfigContext();
		UserBean user = aUser().build();
		
		ActiveSession activeSession = createExpiredSession(user, A_SESSION_ID_1);
		
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		
		SessionUserInUse sut = new SessionUserInUse(loader, configContext, defaultClock(), getSuccessfulSaver());
			
		assertFalse(sut.doesActiveSessionBelongTo(user.getId(), A_SESSION_ID_1));
	}

	
	@Test 
	public void should_find_for_a_system_user_that_the_session_does_not_belong_to_you_if_it_has_expired() throws Exception {
		NonDataSourceBackedConfigContext configContext = new NonDataSourceBackedConfigContext();
		UserBean systemUser = aSystemUser().build();
		
		ActiveSession activeSession = createExpiredSession(systemUser, A_SESSION_ID_1);
		
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		
		SessionUserInUse sut = new SessionUserInUse(loader, configContext, defaultClock(), getSuccessfulSaver());
			
		assertFalse(sut.doesActiveSessionBelongTo(systemUser.getId(), A_SESSION_ID_1));
	}

	
	
	
	@Test
	public void should_request_loader_to_retrieve_the_active_session_for_the_user_id_passed_in() throws Exception {
		long userIdentifier = 1L;
		ActiveSessionLoader loader = createMock(ActiveSessionLoader.class);
		expect(loader.setId(userIdentifier)).andReturn(loader);
		expect(loader.load()).andReturn(null);
		replay(loader);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
			
		sut.doesActiveSessionBelongTo(userIdentifier, A_SESSION_ID_1);
		
		verify(loader);
	}


	private ActiveSessionSaver getSuccessfulSaver() {
		ActiveSessionSaver saver = EasyMock.createMock(ActiveSessionSaver.class);
		expect(saver.update((ActiveSession)anyObject())).andAnswer(new IAnswer<ActiveSession>() {

			public ActiveSession answer() throws Throwable {
				return (ActiveSession) EasyMock.getCurrentArguments()[0];
			}
		});
		replay(saver);
		return saver;
	}
	
	
	@Test
	public void should_touch_the_active_session_when_it_belongs_to_the_passed_in_session_id() {
		UserBean user = aUser().build();
		
		ActiveSession activeSession = new ActiveSession(user, A_SESSION_ID_1);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		ActiveSessionSaver saver = EasyMock.createMock(ActiveSessionSaver.class);
		expect(saver.update(activeSession)).andReturn(activeSession);
		replay(saver);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), saver);
			
		sut.doesActiveSessionBelongTo(user.getId(), A_SESSION_ID_1);
		
		verify(saver);
	}
	
	
	@Test
	public void should_find_that_a_system_users_owns_the_active_session_when_the_active_session_is_their_session() throws Exception {
		UserBean systemUser = aSystemUser().build();
		
		ActiveSession activeSession = new ActiveSession(systemUser, A_SESSION_ID_1);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
			
		assertTrue(sut.doesActiveSessionBelongTo(systemUser.getId(), A_SESSION_ID_1));
	}
	
	
	@Test
	public void should_find_that_a_system_users_owns_the_active_session_when_the_active_session_is_not_their_session() throws Exception {
		UserBean systemUser = aSystemUser().build();
		
		ActiveSession activeSession = new ActiveSession(systemUser, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
			
		assertTrue(sut.doesActiveSessionBelongTo(systemUser.getId(), A_SESSION_ID_1));
	}
	
	
	
	@Test
	public void should_find_that_no_active_session_returned_by_the_loader_means_that_there_is_no_active_session() throws Exception {
		UserBean user = aUser().build();
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(null);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
		assertFalse("an active session being reported when one should not exist.", sut.isThereAnActiveSessionFor(user.getId())); 
	}
	
	@Test
	public void should_find_that_there_is_an_active_session_when_one_exists_and_has_not_expired() throws Exception {
		UserBean user = aUser().build();
		ActiveSession activeSession = new ActiveSession(user, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
		assertTrue(sut.isThereAnActiveSessionFor(user.getId())); 
	}
	
	@Test
	public void should_find_that_there_is_not_an_active_session_for_a_system_user_when_one_exists_and_has_not_expired() throws Exception {
		UserBean sysetmUser = aSystemUser().build();
		ActiveSession activeSession = new ActiveSession(sysetmUser, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
		assertFalse(sut.isThereAnActiveSessionFor(sysetmUser.getId())); 
	}
	
	
	@Test
	public void should_find_that_there_is_not_an_active_session_for_a_system_user_when_one_does_not_exists() throws Exception {
		UserBean sysetmUser = aSystemUser().build();
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(null);
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), defaultClock(), getSuccessfulSaver());
		assertFalse(sut.isThereAnActiveSessionFor(sysetmUser.getId())); 
	}

	
	@Test
	public void should_find_that_there_is_not_an_active_session_for_a_user_when_it_has_expired() throws Exception {
		UserBean user = aUser().build();
		Clock clock = new StoppedClock();
		
		ActiveSession activeSession = createExpiredSession(user, A_SESSION_ID_2);
		IdLoader<Loader<ActiveSession>> loader = createActiveSessionLoader(activeSession);
		
		
		SessionUserInUse sut = new SessionUserInUse(loader, new NonDataSourceBackedConfigContext(), clock, getSuccessfulSaver());
		
		assertFalse(sut.isThereAnActiveSessionFor(user.getId())); 
	}
	
	
	
	private ActiveSession createExpiredSession(UserBean user, String sessionId) {
		ActiveSession activeSession = new ActiveSession(user, sessionId) {
			@Override
			public boolean isExpired(int timeoutInMinutes, Clock clock) {
				return true;
			}
		};
		return activeSession;
	}
	
	private SystemClock defaultClock() {
		return new SystemClock();
	}

	private IdLoader<Loader<ActiveSession>> createActiveSessionLoader(ActiveSession activeSession) {
		ActiveSessionLoader loader = createMock(ActiveSessionLoader.class);
		expect(loader.setId(anyLong())).andReturn(loader).atLeastOnce();
		expect(loader.load()).andReturn(activeSession).atLeastOnce();
		replay(loader);
		
		return loader; 
	}
	
	private class NonDataSourceBackedConfigContext extends ConfigContext {

		
		public NonDataSourceBackedConfigContext() {
			super();
			markDirty();
		}
		
		protected void reloadConfigurations() {
			configruations.clear();
		}
	}
}
