package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;


public class FieldIdTestableApp extends WebApplication {

	private ComponentTestInjector injector;
	private User user = UserBuilder.aSystemUser().build();
	
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
		FieldIDSession session = new FieldIDSession(request, user);
		/* populate this with required fields like user etc..*/
		return session;
	}
	
	public FieldIdTestableApp withUser(User user) {
		this.user = user;
		return this;
	}
	
}	
	
	
