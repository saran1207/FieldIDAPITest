package com.n4systems.fieldid.wicket.pages.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.wicket.components.user.columns.ArchivedUsersListActionColumn;
import com.n4systems.fieldid.wicket.components.user.columns.UserNameLinkColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;

import java.util.List;

public class ArchivedUsersListPage extends UsersListPage {

    @Override
    protected Model<UserListFilterCriteria> getUserListFilterCriteria() {
        return Model.of(new UserListFilterCriteria(true));
    }

    @Override
    protected List<IColumn<User>> getUserTableColumns() {
        List<IColumn<User>> columns = Lists.newArrayList();

        columns.add(new UserNameLinkColumn(new FIDLabelModel("label.name_first_last"), "firstName, lastName"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.organization"), "owner", "owner.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.customer"), "owner.customerOrg", "owner.customerOrg.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.division"), "owner.divisionOrg", "owner.divisionOrg.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.lastlogin"), "created"));
        columns.add(new ArchivedUsersListActionColumn());
        return columns;
    }
}
