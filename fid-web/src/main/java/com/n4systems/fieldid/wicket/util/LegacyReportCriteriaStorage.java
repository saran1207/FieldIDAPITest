package com.n4systems.fieldid.wicket.util;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;

import javax.servlet.http.HttpSession;

public class LegacyReportCriteriaStorage {

    public SearchCriteriaContainer<AssetSearchCriteria> storeCriteria(AssetSearchCriteria criteriaModel, HttpSession httpSession) {
        SearchCriteriaContainer<AssetSearchCriteria> container = new SearchCriteriaContainer<AssetSearchCriteria>(criteriaModel);
        httpSession.setAttribute(WebSessionMap.SEARCH_CRITERIA, container);
        return container;
    }

    public EventReportCriteria getStoredCriteria(HttpSession session) {
        EventReportCriteria criteriaModel = (EventReportCriteria) session.getAttribute(WebSessionMap.NEW_REPORT_CRITERIA);

        Integer reportPageNumber = (Integer) session.getAttribute("reportPageNumber");

        if (reportPageNumber != null) {
            criteriaModel.setPageNumber(reportPageNumber);
        }

        return criteriaModel;
    }

    public void storePageNumber(HttpSession httpSession, int pageNumber) {
        httpSession.setAttribute("reportPageNumber", pageNumber);
    }

}
