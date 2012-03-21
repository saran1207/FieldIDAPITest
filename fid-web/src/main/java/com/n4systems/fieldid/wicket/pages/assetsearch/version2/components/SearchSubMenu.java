package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.Component;
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

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;


public class SearchSubMenu extends Panel {
	
	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";
	
	private static final String FILTERS_ID = "filters";
	private static final String COLUMNS_ID = "columns";

	private WebMarkupContainer actions;
	private Model<AssetSearchCriteria> model;
	private Integer maxUpdate;
	private Integer maxSchedule;
	private Integer maxPrint;
	private Integer maxExport;
	private Integer maxEvent;
	private AssetSearchMassActionLink printLink;
	private AssetSearchMassActionLink exportLink;
	private Label msg;
	private String clicked;
	private SubMenuLink columns;
	private SubMenuLink filters;
	
	
	public SearchSubMenu(String id, final Model<AssetSearchCriteria> model) {
		super(id);
		this.model = model;
		add(columns = new SubMenuLink(COLUMNS_ID));
		add(filters = new SubMenuLink(FILTERS_ID));
		add(createSaveLink("save"));
		
        add(printLink = new LightboxActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", model));
        add(exportLink = new LightboxActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", model));
        add(msg = new Label("msg", new StringResourceModel("label.select_assets", this, null)));
        
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
		
		add(new AttributeAppender("class", "sub-menu"));		
	}		
	
	protected Component createSaveLink(String id) {
		throw new IllegalStateException("you must override this method to create Save link for the SubMenu");
	}

	private void initializeLimits() {
		// XXX : the actions limits here should roll into one single "maxMassActionLimit"???    ask matt.
		Long tenantId = FieldIDSession.get().getSessionUser().getTenant().getId(); 
         maxUpdate = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, tenantId);
        maxExport = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, tenantId);
        maxPrint = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, tenantId);
        maxEvent = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, tenantId);
        maxSchedule = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_SCHEDULE, tenantId);		
	}

	@Override
	protected void onBeforeRender() {
		int selected = model.getObject().getSelection().getNumSelectedIds();
		boolean rowsSelected = selected>0;
		msg.setVisible(!rowsSelected);
		exportLink.setVisible(rowsSelected && (selected < maxExport));
		printLink.setVisible(rowsSelected && (selected < maxPrint));
		actions.setVisible(rowsSelected && (selected < maxUpdate));
		super.onBeforeRender();
	}
	
	
	// --------------------------------------------------------------------------------------------
	
	
	class LightboxActionLink extends AssetSearchMassActionLink {

		public LightboxActionLink(String id, String url, IModel<AssetSearchCriteria> model) {
			super(id, url, model);
			setOutputMarkupId(true);
		}
		
		@Override
		public void renderHead(IHeaderResponse response) {
			response.renderOnLoadJavaScript("$('#"+getMarkupId()+"').colorbox({ ajax:true });");
		}
		
	}
	
	
	class SubMenuLink extends WebMarkupContainer  {		

		public SubMenuLink(final String id) {
			super(id);
			add(createToggleBehavior(FILTERS_ID.equals(id)));
		}
		
		 private Behavior createToggleBehavior(final boolean showFilters) {
				return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
					@Override public JsScope callback() {
						return JsScopeUiEvent.quickScope("fieldIdWidePage.showConfig("+showFilters+");");
					}
				});
			}
	}

}
