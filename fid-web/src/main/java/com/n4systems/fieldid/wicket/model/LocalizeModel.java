package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.model.IModel;

import java.util.Locale;

public class LocalizeModel<T> implements IModel<T> {

    public final IModel<T> model;

    public LocalizeModel(IModel<T> model) {
        this.model = model;
    }

    @Override
    public T getObject() {
        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        try {
            Locale language = FieldIDSession.get().getSessionUser().getLanguage();
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(language);
            return model.getObject();
        } finally {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
        }
    }

    @Override
    public void setObject(T object) {
        model.setObject(object);
    }

    @Override
    public void detach() {
        model.detach();
    }
}
