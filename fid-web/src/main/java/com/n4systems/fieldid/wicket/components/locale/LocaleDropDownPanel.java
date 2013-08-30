package com.n4systems.fieldid.wicket.components.locale;


import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.Locale;

public class LocaleDropDownPanel extends Panel {

    public LocaleDropDownPanel(String id) {
        super(id);
        add(new LocaleDropDown("localeSelect", FieldIDSession.get().getTenant().getSettings().getLanguages()).setNullValid(false));

    }
}