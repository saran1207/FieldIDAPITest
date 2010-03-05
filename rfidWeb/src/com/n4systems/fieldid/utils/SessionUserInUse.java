package com.n4systems.fieldid.utils;

import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.time.Clock;

public class SessionUserInUse {

	private final IdLoader<Loader<ActiveSession>> loader;
	private final ConfigContext configContext;
	private final Clock clock;
	private final ActiveSessionSaver saver;

	public SessionUserInUse(IdLoader<Loader<ActiveSession>> activeSessionLoader, ConfigContext configContext, Clock clock, ActiveSessionSaver activeSessionSaver) {
		this.loader = activeSessionLoader;
		this.configContext = configContext;
		this.clock = clock;
		this.saver = activeSessionSaver;
		
	}

	public boolean doesActiveSessionBelongTo(Long userIdentifier, String sessionId) {
		ActiveSession activeSession = getActiveSession(userIdentifier);
		
		 if (activeSessionExists(activeSession) && activeSessionMatchSessionId(sessionId, activeSession)) {
			saver.update(activeSession);
			return true; 
			 
		 }
		 return false;
	}
	
	private boolean activeSessionExists(ActiveSession activeSession) {
		return activeSession != null;
	}

	private boolean activeSessionMatchSessionId(String sessionId, ActiveSession activeSession) {
		return (activeSession.getUser().isSystem() || sessionId.equals(activeSession.getSessionId()));
	}

	private ActiveSession getActiveSession(Long userIdentifier) {
		ActiveSession activeSession = loader.setId(userIdentifier).load();
		return activeSession;
	}

	public boolean isThereAnActiveSessionFor(Long userIdentifier) {
		ActiveSession activeSession = getActiveSession(userIdentifier);
		
		return activeSessionExists(activeSession) && activeSession.isForNonSystemUser() && activeSessionHasNotExpired(activeSession);
	}

	private boolean activeSessionHasNotExpired(ActiveSession activeSession) {
		return !activeSession.isExpired(getSessionTimeout(), clock);
	}


	

	private Integer getSessionTimeout() {
		return configContext.getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
	}
	
	

}
