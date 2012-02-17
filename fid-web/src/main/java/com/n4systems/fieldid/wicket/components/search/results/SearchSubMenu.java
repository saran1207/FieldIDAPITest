package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

@SuppressWarnings("serial")
public class SearchSubMenu extends Panel {
	
	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";
	private WebMarkupContainer actions;
	private Model<AssetSearchCriteriaModel> model;
	private Integer maxUpdate;
	private Integer maxSchedule;
	private Integer maxPrint;
	private Integer maxExport;
	private Integer maxEvent;
	
	
	public SearchSubMenu(String id, final Model<AssetSearchCriteriaModel> model) {
		super(id);
		this.model = model;
		add(new SubMenuLink("columns"));
		add(new SubMenuLink("filters"));
		
        add(new AssetSearchMassActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", model));
        add(new AssetSearchMassActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", model));
        
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
		add(createSaveLink("save",true));
		add(createSaveLink("saveAs",false));
		
		initializeLimits();
		
		add(new AttributeAppender("class", "sub-menu"));		
	}		
	
	private void initializeLimits() {
		// XXX : the actions stuff should roll into one "maxMassAction"??? ask matt.
		Long tenantId = FieldIDSession.get().getSessionUser().getTenant().getId(); 
        maxUpdate = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, tenantId);
        maxExport = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, tenantId);
        maxPrint = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, tenantId);
        //ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, tenantId);
        maxEvent = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, tenantId);
        //ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, tenantId);
        maxSchedule = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_SCHEDULE, tenantId);		
	}

	@Override
	protected void onBeforeRender() {
		int selected = model.getObject().getSelection().getNumSelectedIds();
		// XXX : implement correct max
		actions.setVisible(selected>0&&selected<250);
		super.onBeforeRender();
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
