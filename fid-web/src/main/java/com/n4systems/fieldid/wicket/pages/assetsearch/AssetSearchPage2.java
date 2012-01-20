package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;

@SuppressWarnings("serial")
public class AssetSearchPage2 extends FieldIDFrontEndPage implements Mediator {

    private SearchFilterPanel filter;
	private WebMarkupContainer container;
	private SearchColumnsPanel columns;

	public AssetSearchPage2() {
    	add(container = createContentWithMenus());
    }
    
    private WebMarkupContainer createContentWithMenus() {
    	WebMarkupContainer container = new WebMarkupContainer("container");
    	container.setOutputMarkupId(true);
    	container.add(filter = new SearchFilterPanel("filter",this));
    	container.add(columns = new SearchColumnsPanel("columns",this));
    	container.add(new SearchMenu("topMenu",this));    	
    	container.add(new ResultsContentPanel("content", this));
    	container.add(new SearchMenu("bottomMenu",this));    	
    	return container;
	}

	@Override	
    public void renderHead(IHeaderResponse response) {
    	response.renderCSSReference("style/search/search.css");    	
    	super.renderHead(response);
    }

	@Override
	public void handleEvent(AjaxRequestTarget target, Component component) {
		String id = component.getId();
		if (id.equals("search")) {
			target.add(filter);
			target.add(container);
		}
	}

}
