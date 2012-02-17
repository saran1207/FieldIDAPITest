package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchSubMenu extends Panel {
	
	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";
	private Link massSchedueLink;
	private Link massUpdateLink;
	private AssetSearchMassActionLink massEventLink;
	private AssetSearchMassActionLink printManuCertsLink;
	
	
	public SearchSubMenu(String id, final Model<AssetSearchCriteriaModel> model) {
		super(id);
		add(new SubMenuLink("columns"));
		add(new SubMenuLink("filters"));
        add(printManuCertsLink = new AssetSearchMassActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", model));
        add(new AssetSearchMassActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", model));
        add(massEventLink = new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", model));
        add(massUpdateLink = new Link("massUpdateLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassUpdatePage(model));
            }
        });

        add(massSchedueLink = new Link("massScheduleLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassSchedulePage(model));
            }
        });		
		add(new AttributeAppender("class", "sub-menu"));		
		add(createSaveLink("save",true));
		add(createSaveLink("saveAs",false));		
	}	
	
    protected Link createSaveLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override public void onClick() {
                setResponsePage(getSaveResponsePage(overwrite));
            }
        };
        if (!overwrite) {
            // If this is not overwrite (ie the Save As link), it should be invisible if this isn't an existing saved report
            link.setVisible(isExistingSavedItem());
        }
        return link;
    }
	
    protected boolean isExistingSavedItem() {
		return true;
	}

	protected Page getSaveResponsePage(boolean overwrite) {
    	throw new UnsupportedOperationException("override this to redirect on Save actions");
    }
	

	protected void onClick(AjaxRequestTarget target, String id) {
	}
	
	class SubMenuLink extends AjaxLink  {
		
		public SubMenuLink(final String id) {
			super(id);
		}
		
		@Override
		public void onClick(AjaxRequestTarget target) {
			SearchSubMenu.this.onClick(target, getId());
			target.appendJavaScript(SHOW_JS);
		}
		
	}
	
	
}
