package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class FieldIDSpringModel<T> extends LoadableDetachableModel<T> {

    protected FieldIDSpringModel() {
        InjectorHolder.getInjector().inject(this);
    }

}
