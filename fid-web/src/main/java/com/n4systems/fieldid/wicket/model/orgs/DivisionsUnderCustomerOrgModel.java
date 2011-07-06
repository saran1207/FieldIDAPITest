package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgsForCustomerOrgLoader;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.Collections;
import java.util.List;

public class DivisionsUnderCustomerOrgModel extends LoadableDetachableModel<List<DivisionOrg>> {

    private IModel<BaseOrg> internalOrgIModel;

    public DivisionsUnderCustomerOrgModel(IModel<BaseOrg> internalOrgIModel) {
        this.internalOrgIModel = internalOrgIModel;
    }

    @Override
    protected List<DivisionOrg> load() {
        if (internalOrgIModel.getObject() == null) {
            return Collections.emptyList();
        }
        return new DivisionOrgsForCustomerOrgLoader(FieldIDSession.get().getSessionUser().getSecurityFilter())
                .parent(internalOrgIModel.getObject())
                .load();
    }

}
