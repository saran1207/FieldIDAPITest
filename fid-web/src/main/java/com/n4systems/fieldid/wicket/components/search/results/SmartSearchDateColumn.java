package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.Asset;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Date;

/**
 *
 */
abstract public class SmartSearchDateColumn extends PropertyColumn<Asset> {


    public SmartSearchDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);

    }

    @Override
    public void populateItem(Item<ICellPopulator<Asset>> item, String id, IModel<Asset> assetIModel) {
        item.add(new Label(id,
                new DayDisplayModel(Model.of(getDate(assetIModel))).
                        withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
    }

    abstract protected Date getDate(IModel<Asset> assetIModel);
}
