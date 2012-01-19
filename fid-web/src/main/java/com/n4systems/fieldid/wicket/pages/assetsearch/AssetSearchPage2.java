package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;

@SuppressWarnings("serial")
public class AssetSearchPage2 extends FieldIDFrontEndPage implements Mediator {

    private AssetFilterPanel filter;

	public AssetSearchPage2() {
		setOutputMarkupId(true);
    	add(new SearchBar("topBar"));
    	add(new SearchMenu("topMenu",this));    	
    	add(filter = new AssetFilterPanel("filter",this));
    	add(new ResultsContentPanel("content"));
    	add(new SearchMenu("bottomMenu",this));    	
    	add(new SearchBar("bottomBar"));
    	filter.setVisible(false);
    }
    
    @Override	
    public void renderHead(IHeaderResponse response) {
    	response.renderCSSReference("style/search/search.css");    	
    	super.renderHead(response);
    }

	@Override
	public void handleEvent(AjaxRequestTarget target, Component component) {
		String id = component.getId();
		if (id.equals("showFilters")) { 
			filter.setVisible(true);
		} else if (id.equals("search")) { 
			filter.setVisible(false);
			target.add(AssetSearchPage2.this);
		}
	}

}
