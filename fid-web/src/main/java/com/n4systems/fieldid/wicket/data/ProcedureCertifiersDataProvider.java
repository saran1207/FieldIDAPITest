package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

/**
 * This is a Data Provider to feed the Procedure Certifiers List Panel/Table/Component thingy.
 *
 * Created by Jordan Heath on 2016-01-29.
 */
public class ProcedureCertifiersDataProvider extends FieldIDDataProvider<User> {
    @SpringBean
    private UserService userService;

    public ProcedureCertifiersDataProvider(String order, SortOrder sortOrder) {
        setSort(order, sortOrder);
    }

    @Override
    public Iterator<User> iterator(int first, int count) {
        return userService.getSortedCertifiers(getSort().getProperty(),
                                               getSort().isAscending())
                          .iterator();
    }

    @Override
    public int size() {
        return (int)userService.countCertifiers();
    }

    @Override
    public IModel<User> model(User object) {
        return new AbstractReadOnlyModel<User>() {
            @Override
            public User getObject() {
                return object;
            }
        };
    }
}
