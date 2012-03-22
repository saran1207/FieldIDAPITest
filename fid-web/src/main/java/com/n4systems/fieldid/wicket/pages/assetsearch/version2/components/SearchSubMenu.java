package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;


public class SearchSubMenu extends SubMenu<AssetSearchCriteria> {
	

	private WebMarkupContainer actions;
	private Integer maxUpdate;
	private Integer maxSchedule;
	private Integer maxPrint;
	private Integer maxExport;
	private Integer maxEvent;
	private AssetSearchMassActionLink printLink;
	private AssetSearchMassActionLink exportLink;
	private Label msg;

	
	public SearchSubMenu(String id, final Model<AssetSearchCriteria> model) {
		super(id,model);

        add(printLink = new LightboxActionLink("printAllCertsLink", "/aHtml/searchPrintAllCerts.action?searchId=%s", model));
        add(exportLink = new LightboxActionLink("exportToExcelLink", "/aHtml/searchResults.action?searchId=%s", model));
        add(msg = new Label("msg", new StringResourceModel("label.select_assets", this, null)));
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

}
