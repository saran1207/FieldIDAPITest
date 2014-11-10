package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.model.LotoPrintout;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-11-10.
 */
public class LotoDeleteColumn extends PropertyColumn<LotoPrintout> {

    public WebMarkupContainer container;

    public LotoDeleteColumn(IModel<String> displayModel, String sortProperty, WebMarkupContainer container) {
        super(displayModel, sortProperty);
        this.container = container;
    }

    @Override
    public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String id, IModel<LotoPrintout> printoutIModel) {
        item.add(new LotoDeleteCell(id, printoutIModel, container));
    }
}
