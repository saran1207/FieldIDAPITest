package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.model.Model;


public class ReportingSubMenu extends SubMenu<EventReportCriteria> {

	public ReportingSubMenu(String id, final Model<EventReportCriteria> model) {
		super(id, model);
	}
	
	@Override
	protected void onBeforeRender() {
        // XXX : disable any menu items here as needed.
		super.onBeforeRender();
	}

}
