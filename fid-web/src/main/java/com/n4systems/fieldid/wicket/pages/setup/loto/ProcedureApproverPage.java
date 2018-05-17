package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.wicket.components.org.people.table.UserGroupColumn;
import com.n4systems.fieldid.wicket.components.user.UserListPanel;
import com.n4systems.fieldid.wicket.components.user.columns.UserNameLinkCell;
import com.n4systems.fieldid.wicket.components.user.columns.UserNameLinkColumn;
import com.n4systems.fieldid.wicket.components.user.columns.UsersListActionColumn;
import com.n4systems.fieldid.wicket.data.ProcedureCertifiersDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public class ProcedureApproverPage extends FieldIDTemplatePage {

    public ProcedureApproverPage() {
        //Do nothing, look pretty...
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new UserListPanel("certifierListContainer") {
            @Override
            protected String getTotalLabelValue() {
                return new FIDLabelModel("label.total_x", userService.countCertifiers()).getObject();
            }

            @Override
            protected SortableDataProvider getDataProvider() {
                return new ProcedureCertifiersDataProvider("firstName", SortOrder.ASCENDING);
            }

            @Override
            protected List<IColumn<User>> getTableColumns() {
                List<IColumn<User>> columns = new ArrayList<>();

                columns.add(new UserNameLinkColumn(new FIDLabelModel("label.name_first_last"), "firstName, lastName") {
                    @Override
                    public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                        super.populateItem(cellItem, componentId, rowModel);
                        cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                columns.add(new PropertyColumn<User>(new FIDLabelModel("label.username"), "userID", "userID") {
                    @Override
                    public void populateItem(Item<ICellPopulator<User>> item, String componentId, IModel<User> rowModel) {
                        super.populateItem(item, componentId, rowModel);
                        item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                columns.add(new UserGroupColumn(new FIDLabelModel("label.user_group")) {
                    @Override
                    public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                        super.populateItem(cellItem, componentId, rowModel);
                        cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                columns.add(new PropertyColumn<User>(new FIDLabelModel("label.organization"), "owner", "owner.rootOrgName") {
                    @Override
                    public void populateItem(Item item, String componentId, IModel rowModel) {
                        super.populateItem(item, componentId, rowModel);
                        item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                columns.add(new PropertyColumn<User>(new FIDLabelModel("label.customer"), "owner.customerOrg", "owner.customerOrg.name"){
                    @Override
                    public void populateItem(Item<ICellPopulator<User>> item, String componentId, IModel<User> rowModel) {
                        super.populateItem(item, componentId, rowModel);
                        item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                columns.add(new PropertyColumn<User>(new FIDLabelModel("label.division"), "owner.divisionOrg", "owner.divisionOrg.name") {
                    @Override
                    public void populateItem(Item<ICellPopulator<User>> item, String componentId, IModel<User> rowModel) {
                        super.populateItem(item, componentId, rowModel);
                        item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress") {
                    @Override
                    public void populateItem(Item<ICellPopulator<User>> item, String componentId, IModel<User> rowModel) {
                        super.populateItem(item, componentId, rowModel);
                        item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                    }
                });
                if(Permissions.hasOneOf(getCurrentUser().getPermissions(), Permissions.MANAGE_SYSTEM_USERS)) {
                    columns.add(new UsersListActionColumn());
                }

                return columns;
            }
        }.setOutputMarkupId(true));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedure_approval"));
    }
}
