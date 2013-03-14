package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.print.ExportSearchToExcelPage;
import com.n4systems.fieldid.wicket.pages.print.PrintAllCertificatesPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.SendSavedItemPage;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import rfid.web.helper.SessionUser;


public abstract class SearchSubMenu extends SubMenu<AssetSearchCriteria> {

    private WebMarkupContainer actions;
    private Link printLink;
    private Link exportLink;
    private Link massEventLink;
    private Link massUpdateLink;
    private Link massScheduleLink;

    public SearchSubMenu(String id, final Model<AssetSearchCriteria> model) {
        super(id,model);

        add(printLink = makeLinkLightBoxed(new MassActionLink<PrintAllCertificatesPage>("printAllCertsLink", PrintAllCertificatesPage.class, model)));
        add(exportLink = makeLinkLightBoxed(new MassActionLink<ExportSearchToExcelPage>("exportToExcelLink", ExportSearchToExcelPage.class, model)));

        actions=new WebMarkupContainer("actions");
        
        actions.add(massEventLink = new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", model));
        actions.add(massUpdateLink = new Link("massUpdateLink") {
            @Override public void onClick() {
                setResponsePage(new MassUpdateAssetsPage(model));
            }
        });
        actions.add(massScheduleLink = new Link("massScheduleLink") {
            @Override public void onClick() {
                setResponsePage(new MassSchedulePage(model));
            }
        });

        add(actions);
        
        add(new Link("emailLink") {
            @Override
            public void onClick() {
                setResponsePage(new SendSavedItemPage(model, getPage()));
            }
        });
        add(new SaveMenu("saveMenu") {
            @Override protected Link createSaveLink(String id) {
                return SearchSubMenu.this.createSaveLink(id);
            }
            @Override protected Link createSaveAsLink(String id) {
                return SearchSubMenu.this.createSaveAsLink(id);
            }
        });

        initializeLimits();
    }

    @Override
    protected String getNoneSelectedMsgKey() {
        return "label.select_assets";
    }

    protected void updateMenuBeforeRender(AssetSearchCriteria criteria) {
        super.updateMenuBeforeRender(criteria);

        int selected = criteria.getSelection().getNumSelectedIds();
        boolean rowsSelected = selected > 0;
        exportLink.setVisible(rowsSelected && (selected < maxExport));

        boolean isManufacturerCertificate = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);

        printLink.setVisible(rowsSelected && (selected < maxPrint) && isManufacturerCertificate);
        
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();

        massEventLink.setVisible(sessionUser.hasAccess("createevent"));
        massUpdateLink.setVisible(sessionUser.hasAccess("tag"));
        massScheduleLink.setVisible(sessionUser.hasAccess("createevent"));
        
        actions.setVisible(rowsSelected && (selected < maxUpdate) && (massEventLink.isVisible() || massUpdateLink.isVisible() || massScheduleLink.isVisible()));
    }


}
