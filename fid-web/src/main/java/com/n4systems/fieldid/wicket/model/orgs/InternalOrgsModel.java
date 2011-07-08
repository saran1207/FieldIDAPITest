package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.InternalOrgsLoader;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

public class InternalOrgsModel extends LoadableDetachableModel<List<InternalOrg>> {

    @Override
    protected List<InternalOrg> load() {
        return new InternalOrgsLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).load();
    }

}
