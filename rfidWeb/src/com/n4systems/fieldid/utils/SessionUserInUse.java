package com.n4systems.fieldid.utils;

import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;

public class SessionUserInUse {

	private final IdLoader<Loader<ActiveSession>> activeSessionLoader;

	public SessionUserInUse(IdLoader<Loader<ActiveSession>> activeSessionLoader) {
		this.activeSessionLoader = activeSessionLoader;
	}

	public boolean doesActiveSessionBelongTo(Long userIdentifier, String sessionId) {
		ActiveSession activeSession = getActiveSession(userIdentifier);
		
		return (activeSession != null && 
				activeSessionMatchSessionId(sessionId, activeSession));
	}

	private boolean activeSessionMatchSessionId(String sessionId, ActiveSession activeSession) {
		return (activeSession.getUser().isSystem() ||  
		sessionId.equals(activeSession.getSessionId()));
	}

	private ActiveSession getActiveSession(Long userIdentifier) {
		ActiveSession activeSession = activeSessionLoader.setId(userIdentifier).load();
		return activeSession;
	}

}
