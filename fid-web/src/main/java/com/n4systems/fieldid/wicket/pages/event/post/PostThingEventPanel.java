package com.n4systems.fieldid.wicket.pages.event.post;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class PostThingEventPanel extends Panel {

    public PostThingEventPanel(String id, IModel<ThingEvent> event) {
        super(id);
        DropDownChoice assetStatus = new DropDownChoice<AssetStatus>("assetStatus", new PropertyModel<AssetStatus>(event, "assetStatus"), new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>());
        assetStatus.add(new UpdateComponentOnChange());
        assetStatus.setNullValid(true);
        add(assetStatus);
    }

}
