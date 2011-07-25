package com.n4systems.fieldid.utils;

import org.apache.struts2.StrutsStatics;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.context.UserContext;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.services.SecurityContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SetCurrentUserInterceptor extends AbstractInterceptor implements StrutsStatics {

    private UserContext userContext;

    public SetCurrentUserInterceptor() {
        this(ThreadLocalUserContext.getInstance());
    }

    public SetCurrentUserInterceptor(UserContext userContext) {
        this.userContext = userContext;
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
	            userContext.setCurrentUser(user);
	            
	            securityContext.setUserSecurityFilter(sessionUser.getSecurityFilter());
	        }

	        return invocation.invoke();
        } finally {
        	securityContext.setUserSecurityFilter(null);
            userContext.setCurrentUser(null);
        }
    }

}
