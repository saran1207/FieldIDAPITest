package com.n4systems.fieldid.wicket.components.setup.eventbook;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventBook;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This is the Column class for a custom actions column in the Event Books list pages.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public class EventBooksActionColumn extends AbstractColumn<EventBook> {

    private EventBooksListPanel listPanel;

    public EventBooksActionColumn(EventBooksListPanel listPanel) {
        super(new FIDLabelModel(""));
        this.listPanel = listPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<EventBook>> item, String id, final IModel<EventBook> model) {
        item.add(new EventBooksActionCell(id, model, listPanel));
    }
}
