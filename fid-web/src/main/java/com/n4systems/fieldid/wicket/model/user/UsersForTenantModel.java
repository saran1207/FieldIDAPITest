package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserListLoader;

import java.util.List;

public class UsersForTenantModel extends FieldIDSpringModel<List<User>> {

    @Override
    protected List<User> load() {
        return new UserListLoader(getSecurityFilter()).load();
    }

}
