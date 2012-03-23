package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;


public class SearchSubMenu extends SubMenu<AssetSearchCriteria> {
	

	private WebMarkupContainer actions;
    private Link printLink;
	private Link exportLink;

	
	public SearchSubMenu(String id, final Model<AssetSearchCriteria> model) {
		super(id,model);

        add(printLink = makeLinkLightBoxed(new AssetSearchMassActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", model)));
        add(exportLink = makeLinkLightBoxed(new AssetSearchMassActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", model)));
        add(createSaveLink("save"));

        actions=new WebMarkupContainer("actions");
        
        actions.add(new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", model));
        actions.add(new Link("massUpdateLink") {
            @Override public void onClick() {
                setResponsePage(new MassUpdatePage(model));
            }
        });

        actions.add(new Link("massScheduleLink") {
            @Override public void onClick() {
                setResponsePage(new MassSchedulePage(model));
            }
        });
        
        add(actions);
		
		initializeLimits();
	}

    @Override
    protected String getNoneSelectedMsgKey() {
        return "label.select_assets";
    }

    protected Component createSaveLink(String id) {
		throw new IllegalStateException("you must override this method to create Save link for the SubMenu");
	}

    protected void updateMenuBeforeRender(int selected) {
        super.updateMenuBeforeRender(selected);
        boolean rowsSelected = selected > 0;
        exportLink.setVisible(rowsSelected && (selected < maxExport));
        printLink.setVisible(rowsSelected && (selected < maxPrint));
        actions.setVisible(rowsSelected && (selected < maxUpdate));
    }

}
