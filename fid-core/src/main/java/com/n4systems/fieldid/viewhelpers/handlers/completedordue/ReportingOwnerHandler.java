package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.EventSchedule;
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
        BaseOrg org = null;
        EventSchedule schedule = (EventSchedule) value;

        if (schedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED && schedule.getEvent() != null) {
            org = getOrg(schedule.getEvent());
        } else {
            org = getOrg(schedule.getAsset());
        }

        if (org == null) {
            return "";
        }

        return org.getName();
    }

    protected abstract BaseOrg getOrg(HasOwner owner);

}
