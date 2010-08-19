package com.n4systems.model.activesession;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;


import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.TestingQueryBuilder;

public class ActiveSessionSaverTest {

	private static final String A_SESSION_ID_1 = "a32923jasdflkajsdfa";
	private static final String A_SESSION_ID_2 = "k32j2akjfakjsdfkja";
	private User user;
	private ActiveSession newActiveSession;


	@Before
	public void createActiveSession() {
		user = aUser().build();
		newActiveSession = new ActiveSession(user, A_SESSION_ID_1);
	}



	@Test
	public void should_save_active_session_when_there_is_no_currect_active_session_for_the_user() {
		
		EntityManager entityManager = createMock(EntityManager.class);
		entityManager.persist(newActiveSession);
		replay(entityManager);
		
		createQueryBuildOverridenActiveSessionSaver(null).save(entityManager, newActiveSession);
		
		verify(entityManager);
	}



	private ActiveSessionSaverTestExtension createQueryBuildOverridenActiveSessionSaver(final ActiveSession existingActiveSession) {
		return new ActiveSessionSaverTestExtension(existingActiveSession);
	}
	
	@Test
	public void should_lookup_existing_active_session_by_the_user_id_of_the_current_session() throws Exception {
		
		EntityManager entityManager = createMock(EntityManager.class);
		entityManager.persist(newActiveSession);
		replay(entityManager);
		
		ActiveSessionSaverTestExtension sut = createQueryBuildOverridenActiveSessionSaver(null);
		sut.save(entityManager, newActiveSession);
		
		assertEquals(user.getId(), sut.builder.getWhereParameterValue("userId"));
		
	}
	
	
	@Test
	public void should_remove_an_existing_active_session_before_saving_the_new_one() throws Exception {
		ActiveSession existingActiveSession = new ActiveSession(user, A_SESSION_ID_2); 
		
		EntityManager entityManager = createStrictMock(EntityManager.class);
		entityManager.remove(existingActiveSession);
		entityManager.flush();
		entityManager.persist(newActiveSession);
		
		replay(entityManager);
		
		createQueryBuildOverridenActiveSessionSaver(existingActiveSession).save(entityManager, newActiveSession);
		
		verify(entityManager);
	}
	
	private final class ActiveSessionSaverTestExtension extends ActiveSessionSaver {
		private final ActiveSession existingActiveSession;
		QueryBuilder<ActiveSession> builder;

		private ActiveSessionSaverTestExtension(ActiveSession existingActiveSession) {
			this.existingActiveSession = existingActiveSession;
		}

		@Override
		protected QueryBuilder<ActiveSession> getQueryBuilder() {
			builder = new TestingQueryBuilder<ActiveSession>(ActiveSession.class).setSingleResult(existingActiveSession);
			return builder;
		}
	}
}
