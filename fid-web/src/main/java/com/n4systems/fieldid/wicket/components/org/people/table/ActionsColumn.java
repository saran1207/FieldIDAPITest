package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.fieldid.wicket.components.org.people.PeopleListPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class ActionsColumn extends AbstractColumn<User> {

    PeopleListPanel listPanel;

    public ActionsColumn(PeopleListPanel listPanel) {
        super(new FIDLabelModel(""));
        this.listPanel = listPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
        cellItem.add(new UserActionsCell(componentId, rowModel, listPanel));
    }
}
