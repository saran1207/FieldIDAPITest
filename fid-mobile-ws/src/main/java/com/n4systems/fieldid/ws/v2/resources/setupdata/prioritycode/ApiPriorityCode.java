package com.n4systems.fieldid.ws.v2.resources.setupdata.prioritycode;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModelWithName2;

public class ApiPriorityCode extends ApiReadOnlyModelWithName2 {
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
