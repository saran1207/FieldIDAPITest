package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class AssetSearchPage2 extends FieldIDFrontEndPage implements FormListener {

	private WebMarkupContainer container;
	private SearchFilterPanel filter;
	private SearchColumnsPanel columns;
	private ResultsContentPanel content;

	public AssetSearchPage2() {
    	container = new WebMarkupContainer("container");
    	container.setOutputMarkupId(true);
    	Model<AssetSearchCriteriaModel> model = createModel();
    	container.add(new SearchMenu("topMenu",this));    	
    	container.add(new SearchConfigPanel("config", model,this));
    	container.add(content=new ResultsContentPanel("content", model, this));
    	add(container);
    }	
	
	protected Model<AssetSearchCriteriaModel> createModel() {
		return new Model<AssetSearchCriteriaModel>(new AssetSearchCriteriaModel()); 
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
			target.add(content);
		}
	}

}
