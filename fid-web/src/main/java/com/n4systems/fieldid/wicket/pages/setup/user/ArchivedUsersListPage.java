package com.n4systems.fieldid.wicket.pages.setup.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.wicket.components.user.columns.ArchivedUsersListActionColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ArchivedUsersListPage extends UsersListPage {

    @Override
    protected Model<UserListFilterCriteria> getUserListFilterCriteria() {
        return Model.of(new UserListFilterCriteria(true));
    }

    @Override
    protected List<IColumn<User>> getUserTableColumns() {
        List<IColumn<User>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.name_first_last"), "firstName, lastName", "fullName") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.organization"), "owner", "owner.name") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.customer"), "owner.customerOrg", "owner.customerOrg.name") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.division"), "owner.divisionOrg", "owner.divisionOrg.name") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.lastlogin"), "created") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new ArchivedUsersListActionColumn() {
            @Override
            protected void onError(AjaxRequestTarget target, String message) {
                error(message);
                target.add(feedbackPanel);
            }
        });
        return columns;
    }
}
