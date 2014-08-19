package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class LastLoginColumn extends AbstractColumn<User> {

    public LastLoginColumn(IModel<String> displayModel) {
        super(displayModel, "lastLogin");
    }

    @Override
    public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
        cellItem.add(new LastLoginCell(componentId, rowModel));
    }
}
