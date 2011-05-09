package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import rfid.web.helper.SessionUser;

public class FieldIDRequestCycle extends WebRequestCycle {

    public FieldIDRequestCycle(WebApplication application, WebRequest request, Response response) {
        super(application, request, response);
    }

    @Override
    protected void onBeginRequest() {
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        if (sessionUser != null) {
            FilteredIdLoader<User> userLoader = new FilteredIdLoader<User>(new OpenSecurityFilter(), User.class);
            User user = userLoader.setId(sessionUser.getId()).load();
            ThreadLocalUserContext.getInstance().setCurrentUser(user);
        }
    }

    @Override
    protected void onEndRequest() {
        ThreadLocalUserContext.getInstance().setCurrentUser(null);
    }

}
