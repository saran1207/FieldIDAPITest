package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.User;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ExaminersModel extends FieldIDSpringModel<List<User>> {

    @SpringBean
    private UserService userService;
    private IModel<User> defaultUserEntry;

    public ExaminersModel() {
        this(new Model<User>());
    }

    public ExaminersModel(IModel<User> defaultUserEntry) {
        this.defaultUserEntry = defaultUserEntry;
    }

    @Override
    protected List<User> load() {
        List<User> examiners = userService.getExaminers();

        if (defaultUserEntry.getObject() != null && !examiners.contains(defaultUserEntry.getObject())) {
            examiners.add(defaultUserEntry.getObject());
        }

        return examiners;
    }

}
