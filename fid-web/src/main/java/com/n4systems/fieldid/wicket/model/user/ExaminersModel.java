package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ExaminersModel extends FieldIDSpringModel<List<User>> {

    @SpringBean
    private UserService userService;

    @Override
    protected List<User> load() {
        return userService.getExaminers();
    }

}
