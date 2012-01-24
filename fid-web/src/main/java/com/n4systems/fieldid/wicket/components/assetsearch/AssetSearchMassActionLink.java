package com.n4systems.fieldid.wicket.components.assetsearch;

import javax.servlet.http.HttpSession;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.viewhelpers.AssetSearchContainer;
import com.n4systems.fieldid.wicket.components.search.results.LegacySRSMassActionLink;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

public class AssetSearchMassActionLink extends LegacySRSMassActionLink<AssetSearchCriteriaModel, AssetSearchContainer> {

    public AssetSearchMassActionLink(String id, String url, IModel<AssetSearchCriteriaModel> reportCriteriaModel) {
        super(id, url, reportCriteriaModel);
        add(new AttributeAppender("class", new Model<String>("lightboxPrintLink"), " " ));        
    }

    @Override
    protected AssetSearchContainer convertAndStoreCriteria(AssetSearchCriteriaModel searchCriteriaModel, HttpSession session) {
        return new LegacyReportCriteriaStorage().storeCriteria(searchCriteriaModel, session);
    }
        
    @Override
    public void renderHead(IHeaderResponse response) {		
    	response.renderJavaScript("$(function() { $('.lightboxPrintLink').colorbox({ ajax:true }) });","lightboxPrintLinkListeners");
    	super.renderHead(response);
    }

}
