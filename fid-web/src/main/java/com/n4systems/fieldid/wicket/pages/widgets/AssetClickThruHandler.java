package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;

@SuppressWarnings("serial")
public class AssetClickThruHandler extends SimpleClickThruHandler {
	
	public AssetClickThruHandler(Component component, Long id) {
		super(component, id);		
    }

	@Override
	protected Class<? extends FieldIDFrontEndPage> getClickThruPage() {
		return AssetSearchResultsPage.class;
	}
	
	
}
