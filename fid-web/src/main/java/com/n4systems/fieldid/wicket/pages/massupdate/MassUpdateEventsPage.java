package com.n4systems.fieldid.wicket.pages.massupdate;

import com.n4systems.fieldid.wicket.components.massupdate.event.MassUpdateEventsPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class MassUpdateEventsPage extends FieldIDFrontEndPage {

	public MassUpdateEventsPage(IModel<EventReportCriteria> criteriaModel) {
		add(new MassUpdateEventsPanel("contentPanel", criteriaModel));
	}
	
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/massupdate/mass_update_assets.css");
    }
}
