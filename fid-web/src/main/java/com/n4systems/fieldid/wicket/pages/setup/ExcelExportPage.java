package com.n4systems.fieldid.wicket.pages.setup;

import java.util.Date;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.service.export.ExportService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.downloadlink.DownloadLink;

@SuppressWarnings("serial")
public class ExcelExportPage extends SetupPage {
	
	@SpringBean	ExportService exportService;
	@SpringBean UserService userService;
	
    public ExcelExportPage(PageParameters params) {
        super(params);

        add(CSSPackageResource.getHeaderContribution("style/chosen/chosen.css"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/jquery-1.4.2.min.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/chosen/chosen.jquery.js"));
        
        add(new ExcelExportForm("excelExportForm"));
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
            eventTypeSelect.setNullValid(true).setRequired(true);
            	           
            add(new AjaxButton("exportButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                	SessionUser sessionUser = FieldIDSession.get().getSessionUser();
                	String reportName = eventType.getName()+"_Events.xls";
					DownloadLink link = exportService.exportEventTypeToExcel(sessionUser.getId(), eventType, reportName);
            	}
            
            });

        }
        

    }

}
