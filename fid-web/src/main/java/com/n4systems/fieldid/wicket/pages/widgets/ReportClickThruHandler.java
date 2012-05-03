package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunReportPage;
import org.apache.wicket.Component;

public class ReportClickThruHandler extends SimpleClickThruHandler {
	
	public ReportClickThruHandler(Component component, Long widgetDefinitionId) {
		super(component, widgetDefinitionId);
    }

	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThruPage() {
		return RunReportPage.class;
	}    

}
