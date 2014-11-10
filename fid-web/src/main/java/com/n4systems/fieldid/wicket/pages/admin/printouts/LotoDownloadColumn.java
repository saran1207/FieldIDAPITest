package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.model.LotoPrintout;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-11-10.
 */
public class LotoDownloadColumn extends PropertyColumn<LotoPrintout> {


    public LotoDownloadColumn(IModel<String> displayModel, String sortProperty) {
        super(displayModel, sortProperty);
    }

    @Override
    public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String id, IModel<LotoPrintout> printoutIModel) {
        item.add(new LotoDownloadCell(id, printoutIModel));
    }
}
