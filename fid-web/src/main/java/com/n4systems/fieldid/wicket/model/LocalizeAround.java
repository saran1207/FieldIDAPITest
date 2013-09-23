package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.wicket.FieldIDSession;

import java.io.Serializable;
import java.util.Locale;
import java.util.concurrent.Callable;

public class LocalizeAround<T> implements Serializable, Callable<T> {

    private Callable<T> around;

    public LocalizeAround(Callable<T> around) {
        this.around = around;
    }

    @Override
    public T call() {
        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        try {
            Locale language = FieldIDSession.get().getSessionUser().getLanguage();
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(language);
            return around.call();
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
        }
    }

}
