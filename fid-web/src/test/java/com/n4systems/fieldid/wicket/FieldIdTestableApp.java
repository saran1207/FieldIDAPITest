package com.n4systems.fieldid.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;


public class FieldIdTestableApp extends WebApplication {

	private ComponentTestInjector injector;
	private User user = UserBuilder.aSystemUser().build();
	private FieldIDSession session;
	
	public FieldIdTestableApp(ComponentTestInjector injector) {
		this.injector = injector;
	}

	@Override
	protected void init() {
		addComponentInstantiationListener(new IComponentInstantiationListener() {					
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
	protected ISessionStore newSessionStore() {
		//This method is overridden to avoid the WicketSerializableException
		//Some test classes use jMocks for collaboration.
		//Wicket needs everything to be serializable and mocks are not
		return new HttpSessionStore(FieldIdTestableApp.this);
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
	
	
