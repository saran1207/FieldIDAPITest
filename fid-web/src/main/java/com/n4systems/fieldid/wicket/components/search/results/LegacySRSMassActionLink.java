package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Deprecated
public abstract class LegacySRSMassActionLink<MODEL extends SearchCriteria> extends Link {

    private String url;
    private IModel<MODEL> reportCriteriaModel;

    public LegacySRSMassActionLink(String id, String url, IModel<MODEL> reportCriteriaModel) {
        super(id);
        this.url = url;
        this.reportCriteriaModel = reportCriteriaModel;
    }

    @Override
    public void onClick() {
        HttpServletRequest httpServletRequest = ((ServletWebRequest) getRequest()).getContainerRequest();
        HttpSession session = httpServletRequest.getSession();

        SearchCriteriaContainer<MODEL> container = convertAndStoreCriteria(reportCriteriaModel.getObject(), session);

        String formattedUrl = String.format(url, container.getSearchId());
        String destination = ConfigService.getInstance().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;

        getRequestCycle().replaceAllRequestHandlers(new RedirectRequestHandler(destination));
    }

    protected abstract SearchCriteriaContainer<MODEL> convertAndStoreCriteria(MODEL searchCriteriaModel, HttpSession session);

}
