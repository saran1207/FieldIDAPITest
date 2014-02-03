package com.n4systems.fieldid.servlets;

import com.n4systems.fieldid.actions.utils.WebSessionMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class ConcurrentLoginSessionListener implements HttpSessionListener {

	private static final Set<HttpSession> sessions = Collections.newSetFromMap(new WeakHashMap<HttpSession, Boolean>());

	public static WebSessionMap getSessionById(String sessionId) {
		WebSessionMap foundSession = null;
		for (HttpSession session: sessions) {
			if (session.getId().equals(sessionId)) {
				foundSession = new WebSessionMap(session);
				break;
			}
		}
		return foundSession;
	}

	// Returns a Set of valid WebSessionMap's for the userId specified.
	public static Set<WebSessionMap> getValidSessionsForUser(Long userId) {
		Set<WebSessionMap> userSessions = new HashSet<WebSessionMap>();
		WebSessionMap webSession;
		for (HttpSession session: sessions) {
			webSession = new WebSessionMap(session);
			if (webSession.isValid() && webSession.getSessionUser().getId().equals(userId)) {
				userSessions.add(webSession);
			}
		}
		return userSessions;
	}

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		sessions.add(sessionEvent.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		sessions.remove(sessionEvent.getSession());
	}

}
