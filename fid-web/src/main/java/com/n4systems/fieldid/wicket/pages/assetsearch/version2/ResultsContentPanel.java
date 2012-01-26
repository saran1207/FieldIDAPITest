package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.model.search.AssetSearchCriteriaModel;


@SuppressWarnings("serial")
public class ResultsContentPanel extends Panel {

	private FormListener formListener;

	public ResultsContentPanel(String id, IModel<AssetSearchCriteriaModel> model, FormListener formListener) {
		super(id, model);
		this.formListener = formListener;
		setOutputMarkupId(true);
		add(new AssetSearchResultsPanel("results", model) {
			@Override
			protected void updateSelectionStatus(AjaxRequestTarget target) {
				super.updateSelectionStatus(target);
			}
		});
	}
	
	// in order to avoid changing shared code i am just styling away the bottom navigation bar. 
	// when full search/reporting page switchover is complete the java code should be adjusted and this workaround taken out.
//	@Override
//	public void renderHead(IHeaderResponse response) {
//		response.renderJavaScript("$('.paginationWrapper:eq(1)').css('display','none');",null);
//		super.renderHead(response);
//	}
}
