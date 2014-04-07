package com.n4systems.fieldid.wicket.pages.massupdate;

import com.n4systems.fieldid.wicket.components.massupdate.event.MassUpdateEventsPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class MassUpdateEventsPage extends FieldIDFrontEndPage {

	public MassUpdateEventsPage(IModel<EventReportCriteria> criteriaModel) {
		add(new MassUpdateEventsPanel("contentPanel", criteriaModel) {
            @Override
            protected void returnToPage(IModel<EventReportCriteria> criteria) {
                MassUpdateEventsPage.this.returnToPage(criteria);
            }
        });
	}
	
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/massupdate/mass_update_assets.css");
    }

    protected void returnToPage(IModel<EventReportCriteria> criteriaModel) {
        setResponsePage(new ReportPage(criteriaModel.getObject()));
    }

}
