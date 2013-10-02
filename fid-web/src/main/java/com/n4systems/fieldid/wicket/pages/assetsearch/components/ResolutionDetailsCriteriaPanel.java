package com.n4systems.fieldid.wicket.pages.assetsearch.components;


import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.PropertyRenderer;
import com.n4systems.fieldid.wicket.components.renderer.StatusChoiceRenderer;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.eventstatus.EventStatusesForTenantModel;
import com.n4systems.model.EventResult;
import com.n4systems.model.EventStatus;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class ResolutionDetailsCriteriaPanel extends Panel {

    public ResolutionDetailsCriteriaPanel(String id, IModel<?> model) {
        super(id, model);

        add(new FidDropDownChoice<EventResult>("eventResult", EventResult.getValidEventResults(), new StatusChoiceRenderer()).setNullValid(true));

        add(new FidDropDownChoice<EventStatus>("eventStatus", new LocalizeModel<List<EventStatus>>(new EventStatusesForTenantModel()), new PropertyRenderer<EventStatus>("displayName", "id")).setNullValid(true));

    }
}
