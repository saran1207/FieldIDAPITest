package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.actions.MassUpdate;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionPanel;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

public class AssetSearchMassActionPanel extends MassActionPanel {

    public AssetSearchMassActionPanel(String id, final IModel<AssetSearchCriteriaModel> criteriaModel) {
        super(id);

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();

        boolean certsEnabled = FieldIDSession.get().getSecurityGuard().isManufacturerCertificateEnabled();
        boolean hasCreateEvent = sessionUser.hasAccess("createEvent");
        boolean hasTag = sessionUser.hasAccess("tag");

        AssetSearchMassActionLink printManuCertsLink;
        Link massSchedueLink;
        Link massUpdateLink;
        AssetSearchMassActionLink massEventLink;

        add(printManuCertsLink = new AssetSearchMassActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", criteriaModel));
        add(new AssetSearchMassActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", criteriaModel));
        add(massEventLink = new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", criteriaModel));
        add(massUpdateLink = new Link("massUpdateLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassUpdatePage(criteriaModel));
            }
        });

        add(massSchedueLink = new Link("massScheduleLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassSchedulePage(criteriaModel));
            }
        });

        massSchedueLink.setVisible(hasCreateEvent);
        massEventLink.setVisible(hasCreateEvent);
        printManuCertsLink.setVisible(certsEnabled);
        massUpdateLink.setVisible(hasTag);
    }

}
