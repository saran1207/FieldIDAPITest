package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class ArchivedUsersListActionColumn extends PropertyColumn<User> {

    public ArchivedUsersListActionColumn() {
        super(Model.of(""), "");
    }

    @Override
    public void populateItem(Item<ICellPopulator<User>> item, String componentId, IModel<User> rowModel) {
        item.add(new ArchivedUsersListActionCell(componentId, rowModel) {
            @Override
            protected void onError(AjaxRequestTarget target, String message) {
                ArchivedUsersListActionColumn.this.onError(target, message);
            }
        });
    }

    protected void onError(AjaxRequestTarget target, String message) {}
}
