package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.Event;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.orgs.BaseOrg;

public abstract class ReportingOwnerHandler extends WebOutputHandler {

    public ReportingOwnerHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public final String handleWeb(Long entityId, Object value) {
        return getOrgName(value);
    }

    @Override
    public final Object handleExcel(Long entityId, Object value) {
        return getOrgName(value);
    }

    private String getOrgName(Object value) {
        BaseOrg org;
        Event event = (Event) value;

        if (event.getEventState() == Event.EventState.COMPLETED) {
            org = getOrg(event);
        } else {
            org = getOrg(event.getAsset());
        }

        if (org == null) {
            return "";
        }

        return org.getName();
    }

    protected abstract BaseOrg getOrg(HasOwner owner);

}
