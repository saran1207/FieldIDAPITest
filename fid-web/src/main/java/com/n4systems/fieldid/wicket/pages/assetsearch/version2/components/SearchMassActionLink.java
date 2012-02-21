package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import javax.servlet.http.HttpSession;

import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.viewhelpers.AssetSearchContainer;
import com.n4systems.fieldid.wicket.components.search.results.LegacySRSMassActionLink;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchMassActionLink extends LegacySRSMassActionLink<AssetSearchCriteriaModel, AssetSearchContainer> {

    public SearchMassActionLink(String id, String url, IModel<AssetSearchCriteriaModel> model) {
        super(id, url, model);
    }

    @Override
    protected AssetSearchContainer convertAndStoreCriteria(AssetSearchCriteriaModel searchCriteriaModel, HttpSession session) {
        return new LegacyReportCriteriaStorage().storeCriteria(searchCriteriaModel, session);
    }
        
}
