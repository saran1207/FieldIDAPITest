package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;

public class ReportingCustomerHandler extends ReportingOwnerHandler {
    public ReportingCustomerHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    protected BaseOrg getOrg(HasOwner owner) {
        return owner.getOwner().getCustomerOrg();
    }

}
