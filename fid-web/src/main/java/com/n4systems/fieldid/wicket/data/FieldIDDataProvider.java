package com.n4systems.fieldid.wicket.data;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;

public abstract class FieldIDDataProvider<T> extends SortableDataProvider<T> {

    public FieldIDDataProvider() {
        InjectorHolder.getInjector().inject(this);
    }

}
