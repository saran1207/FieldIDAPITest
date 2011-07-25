package com.n4systems.fieldid.wicket;

import java.util.Collection;

import org.apache.wicket.Response;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.services.SecurityContext;

public class FieldIDRequestCycle extends WebRequestCycle {

	@SpringBean
	private SecurityContext securityContext;
	
    public FieldIDRequestCycle(WebApplication application, WebRequest request, Response response) {
        super(application, request, response);
        InjectorHolder.getInjector().inject(this);
    }

    @Override
    protected void onBeginRequest() {
    	FieldIDSession fieldidSession = FieldIDSession.get();
    	
        SessionUser sessionUser = fieldidSession.getSessionUser();
        if (sessionUser != null) {
            FilteredIdLoader<User> userLoader = new FilteredIdLoader<User>(new OpenSecurityFilter(), User.class);
            User user = userLoader.setId(sessionUser.getId()).load();
            ThreadLocalUserContext.getInstance().setCurrentUser(user);
            
            securityContext.setUserSecurityFilter(sessionUser.getSecurityFilter());
        }
       
        SystemSecurityGuard securityGuard = fieldidSession.getSecurityGuard();
        if (securityGuard != null) {
        	securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(securityGuard.getTenantId()));
        }

        storeFlashMessages();
    }

    @Override
    protected void onEndRequest() {
        ThreadLocalUserContext.getInstance().setCurrentUser(null);
        securityContext.setUserSecurityFilter(null);
        securityContext.setTenantSecurityFilter(null);
    }

    private void storeFlashMessages() {
        FlashScopeMarshaller marshaller = new FlashScopeMarshaller(null, ((WebRequest)getRequest()).getHttpServletRequest().getSession(true));
        Collection<String> flashMessages = marshaller.getMessageCollectionFromSession(FlashScopeMarshaller.FLASH_MESSAGES);
        for (String flashMessage : flashMessages) {
            FieldIDSession.get().info(flashMessage);
        }
        marshaller.storeAndRemovePreviousFlashMessages();
    }

}
