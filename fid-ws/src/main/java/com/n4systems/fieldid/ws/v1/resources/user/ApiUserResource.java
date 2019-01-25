package com.n4systems.fieldid.ws.v1.resources.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.newrelic.api.agent.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("user")
public class ApiUserResource extends AbstractUserResource {

    @Autowired
    private UserService userService;

    @Override
    protected void addUserTypeTermToQuery(QueryBuilder<User> query) {
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.PERSON));
    }

    @GET
    @Path("visibleUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public Response findVisibleUserIds() {

        setNewRelicWithAppInfoParameters();
        List<User> users = userService.getUsers(true, false);
        List<Long> filteredUsers = new ArrayList<>();

        for (User user : users) {
            boolean readOnlyCustomerUserAndExternal = securityContext.getUserSecurityFilter().getUser().isReadOnly() && user.getOwner().isExternal();
            if (!readOnlyCustomerUserAndExternal || securityContext.getUserSecurityFilter().getOwner().equals(user.getOwner())) {
                filteredUsers.add(user.getId());
            }
        }

        return Response.ok().entity(filteredUsers).build();
    }
}
