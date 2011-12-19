package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class LegacySRSMassActionLink<MODEL, LEGACY extends SearchContainer> extends Link {

    private String url;
    private IModel<MODEL> reportCriteriaModel;

    public LegacySRSMassActionLink(String id, String url, IModel<MODEL> reportCriteriaModel) {
        super(id);
        this.url = url;
        this.reportCriteriaModel = reportCriteriaModel;
    }

    @Override
    public void onClick() {
        HttpServletRequest httpServletRequest = ((WebRequest) getRequest()).getHttpServletRequest();
        HttpSession session = httpServletRequest.getSession();

        LEGACY searchContainer = convertAndStoreCriteria(reportCriteriaModel.getObject(), session);

        String formattedUrl = String.format(url, searchContainer.getSearchId());
        String destination = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;

        getRequestCycle().setRequestTarget(new RedirectRequestTarget(destination));
    }

    protected abstract LEGACY convertAndStoreCriteria(MODEL model, HttpSession session);

}
