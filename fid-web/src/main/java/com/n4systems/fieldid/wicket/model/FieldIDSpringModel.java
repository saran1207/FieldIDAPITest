package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class FieldIDSpringModel<T> extends LoadableDetachableModel<T> {

    protected FieldIDSpringModel() {
        InjectorHolder.getInjector().inject(this);
    }

    protected FieldIDSpringModel(T object) {
        super(object);
        InjectorHolder.getInjector().inject(this);
    }

    protected SecurityFilter getSecurityFilter() {
        return FieldIDSession.get().getSessionUser().getSecurityFilter();
    }

}
