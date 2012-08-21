package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;


import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.PropertyRenderer;
import com.n4systems.fieldid.wicket.components.renderer.StatusChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventstatus.EventStatusesForTenantModel;
import com.n4systems.model.EventStatus;
import com.n4systems.model.Status;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ResolutionDetailsCriteriaPanel extends Panel {

    public ResolutionDetailsCriteriaPanel(String id, IModel<?> model) {
        super(id, model);

        add(new FidDropDownChoice<Status>("result", Status.getValidEventStates(), new StatusChoiceRenderer()).setNullValid(true));

        add(new FidDropDownChoice<EventStatus>("eventStatus", new EventStatusesForTenantModel(), new PropertyRenderer<EventStatus>("displayName", "id")).setNullValid(true));

    }
}
