package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.Status;
import com.n4systems.util.FieldidDateFormatter;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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

        Event lastEvent = assetService.findLastEvents(asset, FieldIDSession.get().getSessionUser().getSecurityFilter());

        if(lastEvent != null) {
            String eventType = lastEvent.getType().getName();
            String lastEventDate = new FieldidDateFormatter(lastEvent.getDate(), FieldIDSession.get().getSessionUser(), true, true).format();
            Status status = lastEvent.getStatus();

            WebMarkupContainer result;
            add(result = new WebMarkupContainer("result"));
            Label statusLabel;
            result.add(statusLabel = new Label("lastEventResult", status.getDisplayName()));
            if (status.equals(Status.PASS)) {
                result.add(new AttributeModifier("class", "passColor"));
            } else if (status.equals(Status.FAIL)) {
                result.add(new AttributeModifier("class", "failColor"));
            } else {
                result.add(new AttributeModifier("class", "naColor"));
            }
            
            add(new Label("lastEventDate", lastEventDate));
            add(new Label("lastEventType", eventType));

            String viewEventUrl = "aHtml/iframe/event.action?uniqueID=" + lastEvent.getID() + "&assetId=" + asset.getId();
            add(new NonWicketIframeLink("viewLastEventLink", viewEventUrl, false, 600, 600 ));

            String printEventUrl = "file/downloadEventCert.action?uniqueID="+ lastEvent.getID() + "&reportType=INSPECTION_CERT";
            add(new NonWicketLink("printLastEventLink", printEventUrl));
        }
    }
}
