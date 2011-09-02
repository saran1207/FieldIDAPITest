package com.n4systems.fieldid.wicket.util;

import javax.servlet.http.HttpSession;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.search.EventReportCriteriaModel;

public class LegacyReportCriteriaStorage {

    public EventSearchContainer storeCriteria(EventReportCriteriaModel criteriaModel, HttpSession httpSession) {
        EventSearchContainer searchContainer = new ReportFormatConverter(new SerializableSecurityGuard(FieldIDSession.get().getTenant())).convertCriteria(criteriaModel);
        httpSession.setAttribute(WebSessionMap.REPORT_CRITERIA, searchContainer);
        httpSession.setAttribute(WebSessionMap.NEW_REPORT_CRITERIA, criteriaModel);
        httpSession.setAttribute(WebSessionMap.KEY_MULTI_SELECTION, criteriaModel.getSelection());
        return searchContainer;
    }

    public EventReportCriteriaModel getStoredCriteria(HttpSession session) {
        EventReportCriteriaModel criteriaModel = (EventReportCriteriaModel) session.getAttribute(WebSessionMap.NEW_REPORT_CRITERIA);

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
