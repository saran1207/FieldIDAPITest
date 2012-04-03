package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;

@SuppressWarnings("serial")
public class ReportClickThruHandler extends SimpleClickThruHandler {
	
	public ReportClickThruHandler(Component component, Long id) {
		super(component, id);		
    }

	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThruPage() {
        // XXX : WEB-2714
		return ReportingResultsPage.class;
	}    

}
