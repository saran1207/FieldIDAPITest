package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.model.PlatformType;
import com.n4systems.util.ServiceLocator;
import org.apache.struts2.StrutsStatics;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.context.InteractionContext;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.services.SecurityContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Collection;

public class SetCurrentUserInterceptor extends AbstractInterceptor implements StrutsStatics {

    private InteractionContext interactionContext;

    public SetCurrentUserInterceptor() {
        this(ThreadLocalInteractionContext.getInstance());
    }

    public SetCurrentUserInterceptor(InteractionContext interactionContext) {
        this.interactionContext = interactionContext;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
        SecurityContext securityContext = invocationWrapper.getAction().getSecurityContext();
        
        try {
	        SessionUser sessionUser = invocationWrapper.getSessionUser();
	        
	        if (sessionUser != null) {
	            Long userId = sessionUser.getId();
	            FilteredIdLoader<User> userLoader = new FilteredIdLoader<User>(new OpenSecurityFilter(), User.class);
	            User user = userLoader.setId(userId).load();
                Collection<User> visibleUsers = ServiceLocator.getUserGroupService().findUsersVisibleTo(user);
                interactionContext.setVisibleUsers(visibleUsers);
	            interactionContext.setCurrentUser(user);
                interactionContext.setCurrentPlatformType(PlatformType.WEB);
                interactionContext.setCurrentPlatform(FieldIdVersion.getWebVersionDescription());
	            
	            securityContext.setUserSecurityFilter(sessionUser.getSecurityFilter());
	        }

	        return invocation.invoke();
        } finally {
        	securityContext.setUserSecurityFilter(null);
            interactionContext.clear();
        }
    }

}
