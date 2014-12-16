package com.n4systems.fieldid.wicket.components.proceduresearch.results;

import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-11-21.
 */
public class BlankColumn extends AbstractColumn<RowView> {

    public BlankColumn() {
        super(new Model<String>(""));
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> cellItem, String componentId, IModel<RowView> rowModel) {
        cellItem.add(new BlankCell(componentId, rowModel));
    }
}
