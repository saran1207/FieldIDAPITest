package com.n4systems.fieldid.wicket.components.user;


import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by Jordan Heath on 2016-01-29.
 */
public abstract class UserListPanel extends Panel {
    @SpringBean
    protected UserService userService;

    public UserListPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new SimpleDefaultDataTable<User>("users", getTableColumns(), getDataProvider(), 300).setOutputMarkupId(true));
        add(new Label("total", getTotalCountLabel()));
    }

    private LoadableDetachableModel<String> getTotalCountLabel() {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return getTotalLabelValue();
            }
        };
    }

    protected abstract String getTotalLabelValue();

    protected abstract SortableDataProvider getDataProvider();

    protected abstract List<IColumn<User>> getTableColumns();
}
