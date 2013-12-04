package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.EventForm;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class PlaceEventPage extends FieldIDFrontEndPage {

    private List<AbstractEvent.SectionResults> sectionResults;

    public PlaceEventPage() {
        EventForm form = new EventForm();
        add(new EventFormEditPanel("eventFormPanel", new PropertyModel<List<AbstractEvent.SectionResults>>(PlaceEventPage.this, "sectionResults")).setVisible(form!=null && form.getAvailableSections().size()>0));
    }

}
