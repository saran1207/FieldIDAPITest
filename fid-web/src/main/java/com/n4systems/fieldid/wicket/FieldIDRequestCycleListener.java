package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.services.SecurityContext;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.Collection;

public class FieldIDRequestCycleListener implements IRequestCycleListener {

	@SpringBean
	private SecurityContext securityContext;
	
    public FieldIDRequestCycleListener() {
        Injector.get().inject(this);
    }

    @Override
    public void onBeginRequest(RequestCycle cycle) {
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

        storeFlashMessages(cycle);
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        ThreadLocalUserContext.getInstance().setCurrentUser(null);
        securityContext.setUserSecurityFilter(null);
        securityContext.setTenantSecurityFilter(null);
    }

    private void storeFlashMessages(RequestCycle cycle) {
        FlashScopeMarshaller marshaller = new FlashScopeMarshaller(null, ((ServletWebRequest)cycle.getRequest()).getContainerRequest().getSession(true));
        Collection<String> flashMessages = marshaller.getMessageCollectionFromSession(FlashScopeMarshaller.FLASH_MESSAGES);
        for (String flashMessage : flashMessages) {
            FieldIDSession.get().info(flashMessage);
        }
        marshaller.storeAndRemovePreviousFlashMessages();
    }

    @Override
    public void onDetach(RequestCycle cycle) {
    }

    @Override
    public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
    }

    @Override
    public void onRequestHandlerScheduled(RequestCycle cycle, IRequestHandler handler) {
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
    public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url) {
    }

}
