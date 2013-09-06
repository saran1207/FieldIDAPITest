package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.EventBook;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EventBookTranslationsPage extends TranslationsPage{

    @SpringBean
    EventBookService eventBookService;

    public EventBookTranslationsPage() {
        super();
    }

    @Override
    protected DropDownChoice createChoice(String id) {
        return new FidDropDownChoice<EventBook>(id, Model.of(new EventBook()), eventBookService.getAllEventBooks(), getChoiceRenderer());
    }

    public IChoiceRenderer<EventBook> getChoiceRenderer() {
        return new IChoiceRenderer<EventBook>() {
            @Override
            public Object getDisplayValue(EventBook book) {
                return book.getDisplayName();
            }

            @Override
            public String getIdValue(EventBook book, int index) {
                return book.getId()+"";
            }
        };
    }
}
