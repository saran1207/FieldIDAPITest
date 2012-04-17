package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.SendSavedItemPage;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;


public abstract class SearchSubMenu extends SubMenu<AssetSearchCriteria> {

    private WebMarkupContainer actions;
    private Link printLink;
    private Link exportLink;

    public SearchSubMenu(String id, final Model<AssetSearchCriteria> model) {
        super(id,model);

        add(printLink = makeLinkLightBoxed(new AssetSearchMassActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", model)));
        add(exportLink = makeLinkLightBoxed(new AssetSearchMassActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", model)));

        actions=new WebMarkupContainer("actions");

        actions.add(new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", model));
        actions.add(new Link("massUpdateLink") {
            @Override public void onClick() {
                setResponsePage(new MassUpdateAssetsPage(model));
            }
        });

        actions.add(new Link("massScheduleLink") {
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

    protected void updateMenuBeforeRender(int selected) {
        super.updateMenuBeforeRender(selected);
        boolean rowsSelected = selected > 0;
        exportLink.setVisible(rowsSelected && (selected < maxExport));
        printLink.setVisible(rowsSelected && (selected < maxPrint));
        actions.setVisible(rowsSelected && (selected < maxUpdate));
    }


}
