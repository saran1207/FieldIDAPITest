package com.n4systems.fieldid.wicket.util;

import com.n4systems.fieldid.actions.utils.WebSession;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.search.EventReportCriteriaModel;

import javax.servlet.http.HttpSession;

public class LegacyReportCriteriaStorage {

    public EventSearchContainer storeCriteria(EventReportCriteriaModel criteriaModel, HttpSession httpSession) {
        EventSearchContainer searchContainer = new ReportFormatConverter().convertCriteria(criteriaModel);
        httpSession.setAttribute(WebSession.REPORT_CRITERIA, searchContainer);
        httpSession.setAttribute(WebSession.NEW_REPORT_CRITERIA, criteriaModel);
        httpSession.setAttribute(WebSession.KEY_MULTI_SELECTION, criteriaModel.getSelection());
        return searchContainer;
    }

    public EventReportCriteriaModel getStoredCriteria(HttpSession session) {
        EventReportCriteriaModel criteriaModel = (EventReportCriteriaModel) session.getAttribute(WebSession.NEW_REPORT_CRITERIA);

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
