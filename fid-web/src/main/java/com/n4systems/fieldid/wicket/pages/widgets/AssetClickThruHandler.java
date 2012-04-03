package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.RunSavedSearchPage;
import org.apache.wicket.Component;

public class AssetClickThruHandler extends SimpleClickThruHandler {
	
	public AssetClickThruHandler(Component component, Long id) {
		super(component, id);		
    }

	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThruPage() {
		return RunSavedSearchPage.class;
	}	
	
}
