package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgsForInternalOrgLoader;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.Collections;
import java.util.List;

public class CustomersUnderInternalOrgModel extends LoadableDetachableModel<List<CustomerOrg>> {

    private IModel<BaseOrg> internalOrgIModel;

    public CustomersUnderInternalOrgModel(IModel<BaseOrg> internalOrgIModel) {
        this.internalOrgIModel = internalOrgIModel;
    }

    @Override
    protected List<CustomerOrg> load() {
        if (internalOrgIModel.getObject() == null) {
            return Collections.emptyList();
        }
        return new CustomerOrgsForInternalOrgLoader(FieldIDSession.get().getSessionUser().getSecurityFilter())
                .parent(internalOrgIModel.getObject())
                .load();
    }

}
