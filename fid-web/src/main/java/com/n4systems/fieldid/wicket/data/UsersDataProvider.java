package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class UsersDataProvider extends FieldIDDataProvider<User> {

    private UserListFilterCriteria criteria;

    @SpringBean
    private UserService userService;

    public UsersDataProvider(UserListFilterCriteria criteria) {
        this.criteria = criteria;
        setSort(criteria.getOrder(), criteria.isAscending() ? SortOrder.ASCENDING: SortOrder.DESCENDING);
    }

    @Override
    public Iterator<User> iterator(int first, int count) {
        List<User> usersList = userService.getUsers(criteria);
        if( first + count < usersList.size())
                usersList.subList(first, first + count);
        else
                usersList.subList(first, usersList.size());
        return usersList.iterator();
    }

    @Override
    public int size() {
        return userService.countUsers(criteria).intValue();
    }

    @Override
    public IModel<User> model(final User object) {
        return new AbstractReadOnlyModel<User>() {
            @Override
            public User getObject() {
                return object;
            }
        };
    }

    public void setCriteria(UserListFilterCriteria criteria) {
        this.criteria = criteria;
    }

}
