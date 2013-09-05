package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import com.n4systems.fieldid.utils.SessionUserInUse;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.model.PlatformType;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.time.SystemClock;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.Collection;
import java.util.Locale;

public class FieldIDRequestCycleListener implements IRequestCycleListener {

	@SpringBean
	private SecurityContext securityContext;

    @SpringBean
    private UserGroupService userGroupService;
	
    public FieldIDRequestCycleListener() {
        Injector.get().inject(this);
    }

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        FieldIDSession fieldidSession = FieldIDSession.get();

        SessionUser sessionUser = fieldidSession.getSessionUser();
        if (sessionUser != null) {
            storeFlagIfConcurrentUser(fieldidSession.getId(), sessionUser);
            FilteredIdLoader<User> userLoader = new FilteredIdLoader<User>(new OpenSecurityFilter(), User.class);
            User user = userLoader.setId(sessionUser.getId()).load();
            Collection<User> visibleUsers = userGroupService.findUsersVisibleTo(user);
            ThreadLocalInteractionContext.getInstance().setVisibleUsers(visibleUsers);
            ThreadLocalInteractionContext.getInstance().setCurrentUser(user);

           if (fieldidSession.getUserLocale()!=null) {
               ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(fieldidSession.getUserLocale());
           } else if (user.getLanguage()!=null) {
               ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(user.getLanguage());
           } else {
               ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(Locale.getDefault());
           }

            ThreadLocalInteractionContext.getInstance().setCurrentPlatformType(PlatformType.WEB);
            ThreadLocalInteractionContext.getInstance().setCurrentPlatform( FieldIdVersion.getWebVersionDescription());

            securityContext.setUserSecurityFilter(sessionUser.getSecurityFilter());
        }

        SystemSecurityGuard securityGuard = fieldidSession.getSecurityGuard();
        if (securityGuard != null) {
            securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(securityGuard.getTenantId()));
        }

        storeFlashMessages(cycle);
    }

    private void storeFlagIfConcurrentUser(String sessionId, SessionUser sessionUser) {
        SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader(), ConfigContext.getCurrentContext(), new SystemClock(), new ActiveSessionSaver());

        if (sessionUser != null && !sessionUserInUse.doesActiveSessionBelongTo(sessionUser.getUniqueID(), sessionId)) {
            FieldIDSession.get().setConcurrentSessionDetectedInRequestCycle();
        }
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        ThreadLocalInteractionContext.getInstance().clear();
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
