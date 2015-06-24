package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;

/**
 * Created by rrana on 2015-06-24.
 */
public class SmartSearchNextScheduledDateCell extends Panel {

    @SpringBean
    private AssetService assetService;

    public SmartSearchNextScheduledDateCell(String id, IModel<? extends Asset> assetModel) {
        super(id);

        Date date = null;

        ThingEvent event = assetService.findNextScheduledEventByAsset(assetModel.getObject().getID());

        if(event != null) {
            date = event.getDueDate();
        }

        add(new Label("nextScheduledDate", new DayDisplayModel(Model.of(date)).withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
    }
}
