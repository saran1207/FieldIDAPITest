package com.n4systems.fieldid.ws.v2.resources.setupdata.person;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("person")
public class ApiPersonResource extends SetupDataResourceReadOnly<ApiPerson, User> {

    public ApiPersonResource() {
        super(User.class, true);
    }

    @Override
    protected void addTermsToLatestQuery(QueryBuilder<?> query) {
        query.addWhere(WhereClauseFactory.create("registered", true));
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.SYSTEM));
    }

    @Override
    public ApiPerson convertEntityToApiModel(User user) {
        ApiPerson apiUser = new ApiPerson();
        apiUser.setSid(user.getId());
        apiUser.setModified(user.getModified());
        apiUser.setActive(user.isActive());
        apiUser.setOwnerId(user.getOwner().getId());
        apiUser.setName(user.getDisplayName());
        return apiUser;
    }

}
