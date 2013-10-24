package com.n4systems.fieldid.wicket.pages.search.actions;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteriaContainer;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AssetTextSearchActionsPanel extends Panel {

    private IModel<Set<String>> selectedItemsModel;

    public AssetTextSearchActionsPanel(String id, final IModel<Set<String>> selectedItemsModel) {
        super(id, selectedItemsModel);
        this.selectedItemsModel = selectedItemsModel;

        add(new SubmitLink("massEventLink") {
            @Override
            public void onSubmit() {
                AssetSearchCriteria assetSearchCriteria = createAssetSearchCriteria();

                HttpServletRequest httpServletRequest = ((ServletWebRequest) getRequest()).getContainerRequest();
                HttpSession session = httpServletRequest.getSession();

                SearchCriteriaContainer<AssetSearchCriteria> container = new LegacyReportCriteriaStorage().storeCriteria(assetSearchCriteria, session);

                String formattedUrl = String.format("/multiEvent/selectEventType.action?searchContainerKey=" + WebSessionMap.SEARCH_CRITERIA + "&searchId=%s", container.getSearchId());
                String destination = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;

                getRequestCycle().replaceAllRequestHandlers(new RedirectRequestHandler(destination));
            }
        });

        add(new Link("massUpdateLink") {
            @Override
            public void onClick() {
                AssetSearchCriteria assetSearchCriteria = createAssetSearchCriteria();
                setResponsePage(new MassUpdateAssetsPage(new Model<AssetSearchCriteria>(assetSearchCriteria), getPage()));
            }
        });

        add(new Link("massScheduleLink") {
            @Override
            public void onClick() {
                AssetSearchCriteria assetSearchCriteria = createAssetSearchCriteria();
                setResponsePage(new MassSchedulePage(new Model<AssetSearchCriteria>(assetSearchCriteria)));
            }
        });
    }

    private AssetSearchCriteria createAssetSearchCriteria() {
        List<Long> ids = new ArrayList<Long>();

        for (String selectedId : selectedItemsModel.getObject()) {
            ids.add(Long.parseLong(selectedId));
        }

        AssetSearchCriteria assetSearchCriteria = new AssetSearchCriteria();

        MultiIdSelection multiIdSelection = new MultiIdSelection();

        assetSearchCriteria.setSelection(multiIdSelection);

        return assetSearchCriteria;
    }

}
