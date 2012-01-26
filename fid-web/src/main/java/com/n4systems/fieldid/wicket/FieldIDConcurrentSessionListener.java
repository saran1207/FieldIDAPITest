package com.n4systems.fieldid.wicket;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.utils.SessionUserInUse;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.SessionBootedPage;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.time.SystemClock;

public class FieldIDConcurrentSessionListener implements IRequestCycleListener {

	public FieldIDConcurrentSessionListener() {
		Injector.get().inject(this);
	}
	
	@Override
	public void onBeginRequest(RequestCycle cycle) {
		
		FieldIDSession fieldidSession = FieldIDSession.get();

		SessionUser sessionUser = fieldidSession.getSessionUser();
		String sessionId = fieldidSession.getId();
		
		SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader(), ConfigContext.getCurrentContext(), new SystemClock(), new ActiveSessionSaver());
		
		if (sessionUser != null && !sessionUserInUse.doesActiveSessionBelongTo(sessionUser.getUniqueID(), sessionId)) {
			cycle.setResponsePage(SessionBootedPage.class);
		}
	}

	@Override
	public void onDetach(RequestCycle cycle) {
	}

	@Override
	public void onEndRequest(RequestCycle cycle) {
	}

	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		return null;
	}

	@Override
	public void onExceptionRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler, Exception exception) {
	}

	@Override
	public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler) {
	}

	@Override
	public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
	}

	@Override
	public void onRequestHandlerScheduled(RequestCycle cycle, IRequestHandler handler) {
	}

	@Override
	public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url) {
	}

}
