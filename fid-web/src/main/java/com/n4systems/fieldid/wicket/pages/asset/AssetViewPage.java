package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.asset.AssetAttributeDetailsPanel;
import com.n4systems.fieldid.wicket.components.asset.AssetDetailsPanel;
import com.n4systems.fieldid.wicket.components.asset.LinkedAssetPanel;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AssetViewPage extends AssetPage {

    private DateFormat df = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat());
    private DateFormat dtf = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateTimeFormat());

    private LinkedAssetPanel linkedAssetPanel;
    
    public AssetViewPage(PageParameters params) {
        super(params);
           
        final Asset asset = assetModel.getObject();
        
        if(asset.getImageName() == null) {
            add(new ExternalImage("assetImage", ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetTypeImage.action?uniqueID=" + asset.getType().getId())));
        } else {
            add(new ExternalImage("assetImage", ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetImage.action?uniqueID=" + assetId)));
        }
        
        add(new GoogleMap("map").addLocation(asset.getGpsLocation().getLatitude().doubleValue(), asset.getGpsLocation().getLongitude().doubleValue()));

        add(new Label("nextEventMsg", getNextEventMessage()));

        add(new Label("lastEventMsg", getLastEventMessage()));

        add(new AssetAttributeDetailsPanel("assetAttributeDetailsPanel", assetModel));
        
        add(linkedAssetPanel = new LinkedAssetPanel("linkedAssetPanel", assetModel));
        
        add(new AssetDetailsPanel("assetDetailsPanel", assetModel));
        
        linkedAssetPanel.setOutputMarkupId(true);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.asset").getObject() + " / " + assetModel.getObject().getIdentifier());
    }

    private String getNextEventMessage() {
        String nextEventMessage = null;
        EventSchedule nextEvent = eventScheduleService.getNextEventSchedule(assetId, null);

        List<EventSchedule> schedules = eventScheduleService.getAvailableSchedulesFor(assetModel.getObject());

        if(nextEvent != null) {
            String eventType = nextEvent.getEventType().getName();

            String nextDate = df.format(nextEvent.getNextDate());

            String label = "";
            if(nextEvent.isPastDue()) {
                label = "label.nexteventdate_pastdue";
            }else if(nextEvent.getDaysToDue() == 0) {
                label="label.nexteventdate_due_today";
            }else {
                label="label.nexteventdate_msg";
            }

            nextEventMessage =  new FIDLabelModel(label, eventType, nextDate).getObject();
        }

        return nextEventMessage;
    }

    private String getLastEventMessage() {
        String lastEventMessage = null;
        Event lastEvent = assetService.findLastEvents(assetModel.getObject(), FieldIDSession.get().getSessionUser().getSecurityFilter());
    
        if(lastEvent != null) {
            String eventType = lastEvent.getType().getName();
            String lastEventDate = dtf.format(lastEvent.getDate());
            lastEventMessage = new FIDLabelModel("label.lasteventdate_msg", eventType, lastEventDate).getObject();
        }

        return lastEventMessage;
    }

}
