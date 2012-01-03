package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;

public abstract class FieldIDDataProvider<T> extends SortableDataProvider<T> {

    public FieldIDDataProvider() {
        Injector.get().inject(this);
    }

    public SecurityFilter getSecurityFilter() {
        return FieldIDSession.get().getSessionUser().getSecurityFilter();
    }

}
