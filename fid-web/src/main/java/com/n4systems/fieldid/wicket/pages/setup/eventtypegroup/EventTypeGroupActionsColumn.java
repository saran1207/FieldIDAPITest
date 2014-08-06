package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupActionsColumn extends AbstractColumn<EventTypeGroup> {



    private EventTypeGroupPanel eventTypeGroupPanel;

    public EventTypeGroupActionsColumn(EventTypeGroupPanel procedureListPanel) {
        super(new FIDLabelModel(""));
        this.eventTypeGroupPanel = procedureListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<EventTypeGroup>> cellItem, String componentId, final IModel<EventTypeGroup> rowModel) {

        cellItem.add(new EventTypeGroupActionsCell(componentId, rowModel, eventTypeGroupPanel));

    }

}
