package com.n4systems.fieldid.wicket.pages.reporting.action;

import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LegacyReportMassActionLink extends Link {

    private String url;
    private IModel<EventReportCriteriaModel> reportCriteriaModel;

    public LegacyReportMassActionLink(String id, String url, IModel<EventReportCriteriaModel> reportCriteriaModel) {
        super(id);
        this.url = url;
        this.reportCriteriaModel = reportCriteriaModel;
    }

    @Override
    public void onClick() {
        HttpServletRequest httpServletRequest = ((WebRequest) getRequest()).getHttpServletRequest();
        HttpSession session = httpServletRequest.getSession();

        EventSearchContainer searchContainer = new LegacyReportCriteriaStorage().storeCriteria(reportCriteriaModel.getObject(), session);
        String formattedUrl = String.format(url, searchContainer.getSearchId());

        String destination = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;

        getRequestCycle().setRequestTarget(new RedirectRequestTarget(destination));
    }

}
