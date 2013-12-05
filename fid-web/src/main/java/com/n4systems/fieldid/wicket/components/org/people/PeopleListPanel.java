package com.n4systems.fieldid.wicket.components.org.people;


import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.org.people.table.ActionsColumn;
import com.n4systems.fieldid.wicket.components.org.people.table.LastLoginColumn;
import com.n4systems.fieldid.wicket.components.org.people.table.UserGroupColumn;
import com.n4systems.fieldid.wicket.components.org.people.table.UserIdLinkColumn;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

public class PeopleListPanel extends Panel {

    public static final int USERS_PER_PAGE = 10;
    private FieldIDDataProvider<User> dataProvider = null;

    private SimpleDefaultDataTable table;
    private WebMarkupContainer blankSlate;

    public PeopleListPanel(String id, FieldIDDataProvider<User> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;
        setOutputMarkupPlaceholderTag(true);
        add(table = new SimpleDefaultDataTable<User>("usersTable", getUserTableColumns(), dataProvider, USERS_PER_PAGE));
        add(blankSlate = new WebMarkupContainer("blankSlate"));
        updateVisibility();
    }

    public void updateVisibility() {
        table.setVisible(dataProvider.size() > 0);
        blankSlate.setVisible(dataProvider.size() < 1);
    }

    private List<IColumn<User>> getUserTableColumns () {
        List<IColumn<User>> columns = Lists.newArrayList();

        columns.add(new UserIdLinkColumn(new FIDLabelModel("label.userid"), "userID"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.username"), "lastName, firstName", "fullName"));
        columns.add(new UserGroupColumn(new FIDLabelModel("label.user_groups")));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress"));
        columns.add(new LastLoginColumn(new FIDLabelModel("label.lastlogin")));
        columns.add(new ActionsColumn(this));

        return columns;
    }


}
