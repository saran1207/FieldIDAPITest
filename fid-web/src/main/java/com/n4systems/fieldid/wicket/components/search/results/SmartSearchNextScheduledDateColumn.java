package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;

/**
 * Created by rrana on 2015-06-24.
 */
public class SmartSearchNextScheduledDateColumn extends SmartSearchDateColumn {

    @SpringBean
    private AssetService assetService;

    public SmartSearchNextScheduledDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
        // Provide support for SpringBean annotations. SpringBean annotations only work automatically with Wicket
        // components (i.e. Panels) and not Columns such as this class.
        Injector.get().inject(this);
    }

    @Override
    protected Date getDate(IModel<Asset> assetIModel) {
        ThingEvent event = assetService.findNextScheduledEventByAsset(assetIModel.getObject().getID());
        if(event != null) {
            return event.getDueDate();
        }
        else
            return null;
    }

}
