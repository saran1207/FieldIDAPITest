package com.n4systems.fieldid.wicket.pages.massupdate;

import com.n4systems.fieldid.wicket.components.massupdate.openevent.MassUpdateOpenEventsPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class MassUpdateOpenEventsPage extends FieldIDFrontEndPage{
    
    public MassUpdateOpenEventsPage(IModel<EventReportCriteria> criteriaIModel) {
        add(new MassUpdateOpenEventsPanel("contentPanel", criteriaIModel));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/massupdate/mass_update_assets.css");
        response.renderCSSReference("style/legacy/newCss/event/resolve.css");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
    }

}
