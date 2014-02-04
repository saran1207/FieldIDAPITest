package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.injection.Injector;

public class FieldIdMapDataProvider {

    public FieldIdMapDataProvider() {
        Injector.get().inject(this);
    }

    public SecurityFilter getSecurityFilter() {
        return FieldIDSession.get().getSessionUser().getSecurityFilter();
    }
}
