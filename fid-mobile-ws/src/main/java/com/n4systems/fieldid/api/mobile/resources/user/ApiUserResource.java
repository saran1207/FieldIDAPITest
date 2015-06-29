package com.n4systems.fieldid.api.mobile.resources.user;

import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("user")
public class ApiUserResource extends AbstractUserResource {

    @Override
    protected void addUserTypeTermToQuery(QueryBuilder<User> query) {
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.PERSON));
    }

}
