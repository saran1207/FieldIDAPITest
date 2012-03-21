package com.n4systems.fieldid.wicket.util;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.viewhelpers.AssetSearchContainer;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.EventReportCriteria;

import javax.servlet.http.HttpSession;

public class LegacyReportCriteriaStorage {

    public EventSearchContainer storeCriteria(EventReportCriteria criteriaModel, HttpSession httpSession) {
        EventSearchContainer searchContainer = new ReportFormatConverter(new SerializableSecurityGuard(FieldIDSession.get().getTenant())).convertCriteria(criteriaModel);
        httpSession.setAttribute(WebSessionMap.REPORT_CRITERIA, searchContainer);
        httpSession.setAttribute(WebSessionMap.NEW_REPORT_CRITERIA, criteriaModel);
        httpSession.setAttribute(WebSessionMap.KEY_MULTI_SELECTION, criteriaModel.getSelection());
        return searchContainer;
    }

    public AssetSearchContainer storeCriteria(AssetSearchCriteria criteriaModel, HttpSession httpSession) {
        AssetSearchContainer assetSearchContainer = new ReportFormatConverter(new SerializableSecurityGuard(FieldIDSession.get().getTenant())).convertCriteria(criteriaModel);
        httpSession.setAttribute(WebSessionMap.SEARCH_CRITERIA, assetSearchContainer);
        return assetSearchContainer;
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
