package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public class UsersForTenantModel extends FieldIDSpringModel<List<User>> {

    @SpringBean
    private UserService userService;

    @Override
    protected List<User> load() {
        List<User> users = userService.getUsers(false, false);

        //This is how you do a Comparator with a Lambda.  It's actually not necessary to create comparators anymore...
        //All of the functionality of a comparator is handled by this cute little Lambda function.
        Collections.sort(users, (user, user1) -> user.getLastName().compareTo(user1.getLastName()));

        return users;
    }

}
