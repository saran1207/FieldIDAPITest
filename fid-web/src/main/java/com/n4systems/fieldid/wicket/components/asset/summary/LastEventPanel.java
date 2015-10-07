package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.asset.events.table.EventActionsCell;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.model.Asset;
import com.n4systems.model.EventResult;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LastEventPanel extends Panel {

    @SpringBean
    protected AssetService assetService;

    private DateFormat dtf = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateTimeFormat());

    
    public LastEventPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        ThingEvent lastEvent =  new LocalizeModel<ThingEvent>(new LastEventModel(asset)).getObject();

        if(lastEvent != null) {
            EventResult eventResult = lastEvent.getEventResult();

            if (eventResult.equals(EventResult.PASS)) {
                add(new ContextImage("resultIcon", "images/event-completed-pass.png"));
            } else if (eventResult.equals(EventResult.FAIL)) {
                add(new ContextImage("resultIcon", "images/event-completed-fail.png"));
            } else {
                add(new ContextImage("resultIcon", "images/event-completed-na.png"));
            }
            
            add(new Label("lastEventDate", new DayDisplayModel(Model.of(lastEvent.getDate())).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
            add(new Label("lastEventType", new PropertyModel<String>(lastEvent, "type.displayName")));
            add(new Label("performedBy", new PropertyModel<String>(lastEvent, "performedBy.displayName")));
            add(new EventActionsCell("actions", Model.of(lastEvent)));

        }
    }

    class LastEventModel extends LoadableDetachableModel<ThingEvent> {
        private Asset asset;

        LastEventModel(Asset asset) {
            this.asset = asset;
        }

        @Override
        protected ThingEvent load() {
            return assetService.findLastEvents(asset, FieldIDSession.get().getSessionUser().getSecurityFilter());
        }
    }
}
