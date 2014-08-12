package com.n4systems.fieldid.wicket.components.setup.assetstatus;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetStatus;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This is the Action Column for the Asset Status page.  It gets populated with AssetStatusActionCell objects.
 *
 * Created by Jordan Heath on 11/08/14.
 */
public class AssetStatusActionColumn extends AbstractColumn<AssetStatus> {

    private AssetStatusListPanel listPanel;

    public AssetStatusActionColumn(AssetStatusListPanel listPanel) {
        super(new FIDLabelModel(""));
        this.listPanel = listPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<AssetStatus>> item,
                             String id,
                             IModel<AssetStatus> model) {

        item.add(new AssetStatusActionCell(id, model, listPanel));
    }
}
