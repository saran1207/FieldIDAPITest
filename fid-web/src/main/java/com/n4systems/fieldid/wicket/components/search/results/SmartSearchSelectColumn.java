package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.model.Asset;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2015-06-23.
 */
public class SmartSearchSelectColumn extends PropertyColumn<Asset> {


    public SmartSearchSelectColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Asset>> item, String id, IModel<Asset> assetIModel) {
        item.add(new SmartSearchSelectCell(id, assetIModel));
    }
}
