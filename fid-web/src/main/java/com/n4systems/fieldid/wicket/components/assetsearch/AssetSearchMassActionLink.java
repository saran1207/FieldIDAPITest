package com.n4systems.fieldid.wicket.components.assetsearch;

import javax.servlet.http.HttpSession;

import com.n4systems.model.search.SearchCriteriaContainer;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.search.results.LegacySRSMassActionLink;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.AssetSearchCriteria;

@Deprecated
public class AssetSearchMassActionLink extends LegacySRSMassActionLink<AssetSearchCriteria> {

    public AssetSearchMassActionLink(String id, String url, IModel<AssetSearchCriteria> model) {
        super(id, url, model);
    }

    @Override
    protected SearchCriteriaContainer<AssetSearchCriteria> convertAndStoreCriteria(AssetSearchCriteria searchCriteriaModel, HttpSession session) {
        return new LegacyReportCriteriaStorage().storeCriteria(searchCriteriaModel, session);
    }
        
}
