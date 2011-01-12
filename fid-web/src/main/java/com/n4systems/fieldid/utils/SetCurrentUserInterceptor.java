package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.context.UserContext;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.StrutsStatics;
import rfid.web.helper.SessionUser;

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
        SessionUser sessionUser = invocationWrapper.getSessionUser();

        if (sessionUser != null) {
            Long userId = sessionUser.getId();
            FilteredIdLoader<User> userLoader = new FilteredIdLoader<User>(new OpenSecurityFilter(), User.class);
            User user = userLoader.setId(userId).load();
            userContext.setCurrentUser(user);
        }

        String result = invocation.invoke();

        userContext.setCurrentUser(null);

        return result;
    }

}
