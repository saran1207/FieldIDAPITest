package com.n4systems.fieldid.api.mobile.resources.event.actions.prioritycode;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadonlyModelWithName;

public class ApiPriorityCode extends ApiReadonlyModelWithName {
    private String autoScheduleType;
    private Integer autoScheduleCustomDays;

    public String getAutoScheduleType() {
        return autoScheduleType;
    }

    public void setAutoScheduleType(String autoScheduleType) {
        this.autoScheduleType = autoScheduleType;
    }

    public Integer getAutoScheduleCustomDays() {
        return autoScheduleCustomDays;
    }

    public void setAutoScheduleCustomDays(Integer autoScheduleCustomDays) {
        this.autoScheduleCustomDays = autoScheduleCustomDays;
    }
}
