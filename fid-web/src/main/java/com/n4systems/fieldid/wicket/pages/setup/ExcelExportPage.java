package com.n4systems.fieldid.wicket.pages.setup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.export.ExportService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

@SuppressWarnings("serial")
public class ExcelExportPage extends SetupPage {
	
	@SpringBean	ExportService exportService;
	@SpringBean UserService userService;
	
	
    public ExcelExportPage(PageParameters params) {
        super(params);        
        
        // for special combo box = "Chosen.js".
//        add(CSSPackageResource.getHeaderContribution("style/chosen/chosen.css"));
//        add(JavascriptPackageResource.getHeaderContribution("javascript/jquery-1.4.2.min.js"));
//        add(JavascriptPackageResource.getHeaderContribution("javascript/chosen/chosen.jquery.js"));
        
        // for lightbox stuff.
        add(CSSPackageResource.getHeaderContribution("style/pageStyles/downloads.css"));        

        add(new ExcelExportForm("form"));
        
    }

	class ExcelExportForm extends Form {

        private Date fromDate;
        private Date toDate;
        private EventType eventType;
		private DropDownChoice<EventType> eventTypeSelect;
		private EventTypeGroup eventTypeGroup;  // leave this null...it will pick up *all* event types.
		private Object downloadCoordinator;

        public ExcelExportForm(String id) {
            super(id);

            add(new FIDFeedbackPanel("feedbackPanel"));

            final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(this, "eventTypeGroup");	            
            final IModel<EventType> eventTypeModel = new PropertyModel<EventType>(this, "eventType");
            final EventTypesForTenantModel availableEventTypesModel = new EventTypesForTenantModel(eventTypeGroupModel);
            add(eventTypeSelect = new DropDownChoice<EventType>("eventType", eventTypeModel, availableEventTypesModel, new EventTypeChoiceRenderer()));
            add(new ExportLink("exportToExcelLink", "/aHtml/exportEvents.action?eventType=%d" ));
            eventTypeSelect.setNullValid(true).setRequired(true);
            
            eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override protected void onUpdate(AjaxRequestTarget target) { }             	
            });
            	           
        } 

        class ExportLink extends Link<String> { 
        	
        	private String url;
        	
        	public ExportLink(String id, String url) {
        		super(id);
        		this.url = url;
        		add(new AttributeAppender("class", new Model<String>("lightview exportToExcel"), " "));
        	}
        	
        	@Override
        	public void onClick() {
        		HttpServletRequest httpServletRequest = ((WebRequest) getRequest()).getHttpServletRequest();			
        		String formattedUrl = String.format(url, eventType.getId());
        		String destination = ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_PROTOCOL) + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath() + formattedUrl;
        		getRequestCycle().setRequestTarget(new RedirectRequestTarget(destination));			
        	}    	
        }
        
	}

    
}
