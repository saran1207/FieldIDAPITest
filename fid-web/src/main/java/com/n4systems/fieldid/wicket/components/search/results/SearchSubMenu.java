package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

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
	private AssetSearchMassActionLink printLink;
	private AssetSearchMassActionLink exportLink;
	private Label msg;
	private String clicked;
	private SubMenuLink columns;
	private SubMenuLink filters;
	
	
	public SearchSubMenu(String id, final Model<AssetSearchCriteriaModel> model, Link saveLink) {
		super(id);
		this.model = model;
		add(columns = new SubMenuLink("columns"));
		add(filters = new SubMenuLink("filters"));
		add(saveLink);
		
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
	
	protected void onClick(AjaxRequestTarget target, String id) {
		// stub : override to add behaviour
	}
	
	private void delegateOnClick(AjaxRequestTarget target, String id) {
		clicked = id;
		target.add(columns);
		target.add(filters);
		onClick(target,id);
	}
	
	private String getToggleState(SubMenuLink link) {
		return StringUtils.equals(clicked,link.getId()) ? "true" : "";
	}
	
	
	// --------------------------------------------------------------------------------------------
	
	
	class LightboxActionLink extends AssetSearchMassActionLink {

		public LightboxActionLink(String id, String url, IModel<AssetSearchCriteriaModel> model) {
			super(id, url, model);
			setOutputMarkupId(true);
		}
		
		@Override
		public void renderHead(IHeaderResponse response) {
			response.renderOnLoadJavaScript("$('#"+getMarkupId()+"').colorbox({ ajax:true });");
		}
		
	}
	
	
	class SubMenuLink extends AjaxLink  {
		
		// BUG NOTE : when you do this show/hide stuff you will be losing any changes user might have entered into the form. 
		//  i.e. if you enter a identifier, then click Columns (switch panels) then back to Filters, the changed identifier is not preserved.
		//  need to either do this via javascript or updateForm.
		
		public SubMenuLink(final String id) {
			super(id);
			add(new AttributeAppender("class", new Model<String>() {
				@Override public String getObject() {
					return getToggleState(SubMenuLink.this);
				}
			}, " ") );
		}
		
		@Override
		public void onClick(AjaxRequestTarget target) {			
			delegateOnClick(target, getId());
			target.appendJavaScript(SHOW_JS);
		}

	}

}
