package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;


@SuppressWarnings("serial")
public class SearchMenu extends Panel {

	private Component showFilters;
	private Component showColumns;
	private AssetSearchMassActionLink printManuCertsLink;
	private AssetSearchMassActionLink massEventLink;
	private AssetSearchMassActionLink massUpdateLink;
	private Link massSchedueLink;
	private AssetSearchMassActionLink exportExcelLink;

    private StringBuffer variableAssignmentScriptBuffer;

    
    
	public SearchMenu(String id, final IModel<AssetSearchCriteriaModel> model, final FormListener formListener) {
		super(id);
		add(showFilters = new WebMarkupContainer("showFilters").add(new AttributeAppender("class", new StringResourceModel("label.filters",this,null), " ")));
		add(showColumns = new WebMarkupContainer("showColumns").add(new AttributeAppender("class", new StringResourceModel("label.columns",this,null), " ")));

		showFilters.add(createToggleBehavior("filter",""));		
		showColumns.add(createToggleBehavior("columns",""));
		
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();

        boolean certsEnabled = FieldIDSession.get().getSecurityGuard().isManufacturerCertificateEnabled();
        boolean hasCreateEvent = sessionUser.hasAccess("createEvent");
        boolean hasTag = sessionUser.hasAccess("tag");		
		
		add(new AjaxLink("save") {
			@Override public void onClick(AjaxRequestTarget target) {
				//tbd;
			}			
		});		
		add(printManuCertsLink = new AssetSearchMassActionLink("printCertificates", "/aHtml/searchPrintAllCerts.action?searchId=%s", model));
		add(exportExcelLink = new AssetSearchMassActionLink("exportToExcel", "/aHtml/searchResults.action?searchId=%s", model));
		
		add(massEventLink = new AssetSearchMassActionLink("event", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", model));
		add(massUpdateLink = new AssetSearchMassActionLink("update", "/massUpdateAssets.action?searchId=%s", model));
		add(massSchedueLink = new Link("schedule") {
            @Override public void onClick() {
                setResponsePage(new MassSchedulePage(model));
            }
        });
		
		AttributeAppender lightBoxAppender = new AttributeAppender("class", new Model<String>("lightboxPrintLink"), " " );
		printManuCertsLink.add(lightBoxAppender);
		exportExcelLink.add(lightBoxAppender);
		
        printManuCertsLink.setVisible(certsEnabled);
        
        massSchedueLink.setVisible(hasCreateEvent);
        massEventLink.setVisible(hasCreateEvent);
        massUpdateLink.setVisible(hasTag);
        
        addJavaScript();
	}
	
	private void addJavaScript() {
	    variableAssignmentScriptBuffer = new StringBuffer();

        addSizeParameterLabel("maxSizeForMassUpdate", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, getTenantId()));
        addSizeParameterLabel("maxSizeForExcelExport", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, getTenantId()));
        addSizeParameterLabel("maxSizeForPDFPrintOuts", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, getTenantId()));
        addSizeParameterLabel("maxSizeForSummaryReport", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, getTenantId()));
        addSizeParameterLabel("maxSizeForMultiEvent", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, getTenantId()));
        addSizeParameterLabel("maxSizeForAssigningEventsToJobs", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, getTenantId()));
        addSizeParameterLabel("maxSizeForMassSchedule", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_SCHEDULE, getTenantId()));

        variableAssignmentScriptBuffer.append("addLimitGuards();");
        variableAssignmentScriptBuffer.append("$('.lightboxPrintLink').colorbox({ ajax:true });");
		
	}

    @Override
    public void renderHead(IHeaderResponse response) {		
        response.renderOnDomReadyJavaScript(variableAssignmentScriptBuffer.toString());    	
    	super.renderHead(response);
    }	

    protected void addSizeParameterLabel(String labelKey, Integer size) {
    	add(new Label(labelKey, size.toString()));
    	variableAssignmentScriptBuffer.append(labelKey).append(" = ").append(size).append(";\n");
    }
    
    protected Long getTenantId() {
    	return FieldIDSession.get().getSessionUser().getTenant().getId();
    }
    
	private Behavior createToggleBehavior(final String cssClass, final String showHide) {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope("$('."+cssClass+"').toggle("+showHide+");");
			}
		});
	}		
	
	
}
