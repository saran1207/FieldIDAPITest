package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.components.fileupload.FileUploadPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.FileAttachment;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EditEventPage extends FieldIDFrontEndPage {
    
    @SpringBean
    private EventService eventService;

    private AbstractEvent event;
    
    public EditEventPage() {
        event = eventService.createNewMasterEvent(16091209L, 204L);
        
        add(new EventFormEditPanel("eventFormPanel", new PropertyModel<List<AbstractEvent.SectionResults>>(event, "sectionResults")));
        add(new FileUploadPanel("attachmentsPanel", new PropertyModel<List<FileAttachment>>(event, "attachments")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/event_base.css");
        response.renderCSSReference("style/newCss/event/event_schedule.css");
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

}
