package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.model.search.AssetSearchCriteriaModel;


@SuppressWarnings("serial")
public class SearchMenu extends Panel {

	private Component showFilters;
	private Component showColumns;
	private AssetSearchMassActionLink printManuCertsLink;
	
	public SearchMenu(String id, Model<AssetSearchCriteriaModel> model, final FormListener formListener) {
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
				 // to be done later.
				 formListener.handleEvent(target, this);				
			}			
		});		
		add(printManuCertsLink = new AssetSearchMassActionLink("printCertificates", "/aHtml/searchPrintAllCerts.action?searchId=%s", model));
		add(new AssetSearchMassActionLink("exportToExcel", "/aHtml/searchResults.action?searchId=%s", model));
		add(new AjaxLink("massActions"){
			@Override public void onClick(AjaxRequestTarget target) {
				 formListener.handleEvent(target, this);				
			}			
		});		

        printManuCertsLink.setVisible(certsEnabled);
		
//	        add(massEventLink = new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", criteriaModel));
//	        add(massUpdateLink = new AssetSearchMassActionLink("massUpdateLink", "/massUpdateAssets.action?searchId=%s", criteriaModel));
//	        add(massSchedueLink = new Link("newMassUpdateLink") {
//	            @Override
//	            public void onClick() {
//	                setResponsePage(new MassUpdatePage(criteriaModel));
//	            }
//	        });
//
//	        add(massSchedueLink = new Link("massScheduleLink") {
//	            @Override
//	            public void onClick() {
//	                setResponsePage(new MassSchedulePage(criteriaModel));
//	            }
//	        });
//
//	        massSchedueLink.setVisible(hasCreateEvent);
//	        massEventLink.setVisible(hasCreateEvent);
//	        massUpdateLink.setVisible(hasTag);
	}

	private Behavior createToggleBehavior(final String cssClass, final String showHide) {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope("$('."+cssClass+"').toggle("+showHide+");");
			}
		});
	}	
	
}
