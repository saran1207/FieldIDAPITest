package com.n4systems.fieldid.wicket.components.locale;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import java.util.List;
import java.util.Locale;


public class LocaleDropDown extends DropDownChoice {

    private class LocaleRenderer extends ChoiceRenderer {
        @Override
        public String getDisplayValue(Object locale) {
            // return ((Locale)locale).getDisplayName(getLocale());
            return ((Locale)locale).getDisplayName();

        }
    }

    public LocaleDropDown(String id, List<Locale> supportedLocales) {
        super(id, supportedLocales);
        setChoiceRenderer(new LocaleRenderer());
        setModel(new IModel() {
            @Override
            public Object getObject() {
                return FieldIDSession.get().getUserLocale();
            }

            @Override
            public void setObject(Object object) {
                if (null!= object) {
                    FieldIDSession.get().setUserLocale((Locale) object);
                }
            }

            @Override
            public void detach() {

            }
        });
    }

        @Override
        protected boolean wantOnSelectionChangedNotifications() {
            return true;
        }


}
