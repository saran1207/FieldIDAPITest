package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.model.Asset;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class SmartSearchIdentifiedDateColumn extends SmartSearchDateColumn {

    public SmartSearchIdentifiedDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    protected Date getDate(IModel<Asset> assetIModel) {
        return assetIModel.getObject().getIdentified();
    }
}
