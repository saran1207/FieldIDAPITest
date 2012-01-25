package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class AssetSearchPage2 extends FieldIDFrontEndPage implements FormListener {

	private WebMarkupContainer container;
	private SearchFilterPanel filter;
	private SearchColumnsPanel columns;
	private ResultsContentPanel content;
	private IModel<AssetSearchCriteriaModel> model;

	public AssetSearchPage2() {
    	container = new WebMarkupContainer("container");
    	container.setOutputMarkupId(true);
    	model = createModel();
    	container.add(new SearchMenu("topMenu",model,this));    	
    	container.add(new SearchConfigPanel("config", model,this));
    	container.add(content=new ResultsContentPanel("content", model, this));
    	add(container);
    }	
	
	protected IModel<AssetSearchCriteriaModel> createModel() {
		return new CompoundPropertyModel<AssetSearchCriteriaModel>(new AssetSearchCriteriaModel()); 
	}

	@Override	
    public void renderHead(IHeaderResponse response) {
    	response.renderCSSReference("style/search/search.css");    	
    	response.renderCSSReference("style/search/menu.css");    	
    	super.renderHead(response);
    }

	@Override
	public void handleEvent(AjaxRequestTarget target, Component component, Form<?> form) {
		String id = component.getId();
		if (id.equals("search")) {
			model.getObject().setReportAlreadyRun(true);
            model.getObject().getSelection().clear();			
			target.add(content);
		}
	}
	
	@Override
	protected boolean useLegacyCss() {
		return true;
	}
	
	@Override
	protected boolean useSiteWideCss() {
		return true;
	}

}
