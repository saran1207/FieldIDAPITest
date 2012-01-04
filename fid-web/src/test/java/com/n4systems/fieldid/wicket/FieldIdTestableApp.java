package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;


public class FieldIdTestableApp extends WebApplication {

	private ComponentTestInjector injector;
	private User user = UserBuilder.aSystemUser().build();
	private FieldIDSession session;
	
	public FieldIdTestableApp(ComponentTestInjector injector) {
		this.injector = injector;
        injector.getInjector().bind(this);
	}

	@Override
	protected void init() {
        getComponentInstantiationListeners().add(new IComponentInstantiationListener() {
            @Override
            public void onInstantiation(Component component) {
                injector.inject(component);
            }
        });
		super.init();
	}
	
	@Override
	public Class<? extends Page> getHomePage() {
		return DashboardPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		//  NOTE : in order to make tests able to just set session data once before startPage or rendering stuff, i only create a new session 
		//  the first time thru. 
		if (session==null) { 
			session = new FieldIDSession(request, user);
		}
		/* populate this with required fields like user etc..*/
		return session;
	}
	
	public FieldIdTestableApp withUser(User user) {
		this.user = user;
		return this;
	}
	
}	
	
	
