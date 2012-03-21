package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;


public class ReportingFilterPanel extends Panel {

	public ReportingFilterPanel(String id, final IModel<EventReportCriteria> model) {
		super(id,model);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("style/component/searchFilter.css");
		super.renderHead(response);
	}
	
						
}
