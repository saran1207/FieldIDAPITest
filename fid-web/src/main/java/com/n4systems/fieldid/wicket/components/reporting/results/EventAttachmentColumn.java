package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-11-20.
 */
public class EventAttachmentColumn extends AbstractColumn<RowView> {

    public EventAttachmentColumn() {
        super(new Model<String>(""));
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> item, String id, IModel<RowView> rowModel) {
        item.add(new EventAttachmentCell(id, rowModel));
    }

}