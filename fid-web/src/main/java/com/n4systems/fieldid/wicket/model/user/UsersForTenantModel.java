package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserListLoader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsersForTenantModel extends FieldIDSpringModel<List<User>> {

    @Override
    protected List<User> load() {
        List<User> users = new UserListLoader(getSecurityFilter()).load();

        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user, User user1) {
                return user.getFullName().compareTo(user1.getFullName());
            }
        });

        return users;
    }

}
