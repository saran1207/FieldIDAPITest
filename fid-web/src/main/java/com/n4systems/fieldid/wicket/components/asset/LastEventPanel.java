package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
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
            String lastEventDate = dtf.format(lastEvent.getDate());

            add(new Image("lastEventResult", new ContextRelativeResource("/images/small-check.png")));
            add(new Label("lastEventDate", lastEventDate));
            add(new Label("lastEventType", eventType));

            String viewEventUrl = "aHtml/iframe/event.action?uniqueID=" + lastEvent.getID() + "&assetId=" + asset.getId();
            add(new NonWicketIframeLink("viewLastEventLink", viewEventUrl, false, 600, 600 ));

            String printEventUrl = "file/downloadEventCert.action?uniqueID="+ lastEvent.getID() + "&reportType=INSPECTION_CERT";
            add(new NonWicketLink("printLastEventLink", printEventUrl));
        }
    }
}
