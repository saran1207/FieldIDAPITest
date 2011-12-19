package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AssetActionsColumn extends AbstractColumn<RowView> {

    public AssetActionsColumn() {
        super(new Model<String>(""));
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> item, String componentId, IModel<RowView> rowModel) {
        item.add(new AssetActionsCell(componentId, rowModel, item.getMarkupId()));
    }

}
