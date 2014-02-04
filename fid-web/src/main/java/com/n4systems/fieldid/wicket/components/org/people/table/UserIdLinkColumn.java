package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class UserIdLinkColumn extends AbstractColumn<User> {

    public UserIdLinkColumn(IModel<String> displayModel, String sortProperty) {
        super(displayModel, sortProperty);
    }

    @Override
    public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
        cellItem.add(new UserIdLinkCell(componentId, rowModel));
    }
}
