package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.RunSearchPage;
import org.apache.wicket.Component;

public class AssetClickThruHandler extends SimpleClickThruHandler {
	
	public AssetClickThruHandler(Component component, Long widgetDefinitionId) {
		super(component, widgetDefinitionId);
    }

	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThruPage() {
		return RunSearchPage.class;
	}	
	
}
