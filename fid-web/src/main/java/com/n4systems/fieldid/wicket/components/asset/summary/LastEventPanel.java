package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.asset.events.table.EventActionsCell;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
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

        Event lastEvent =  new LocalizeModel<Event>(new LastEventModel(asset)).getObject();

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
            add(new Label("lastEventType", lastEvent.getType().getName()));
            add(new Label("performedBy", lastEvent.getPerformedBy().getDisplayName()));
            add(new EventActionsCell("actions", Model.of(lastEvent)));

        }
    }

    class LastEventModel extends LoadableDetachableModel<Event> {
        private Asset asset;

        LastEventModel(Asset asset) {
            this.asset = asset;
        }

        @Override
        protected Event load() {
            return assetService.findLastEvents(asset, FieldIDSession.get().getSessionUser().getSecurityFilter());
        }
    }
}
