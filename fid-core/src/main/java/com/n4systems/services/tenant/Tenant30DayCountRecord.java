package com.n4systems.services.tenant;

import java.io.Serializable;

public class Tenant30DayCountRecord implements Serializable {

    private Long tenantId;
    private Long count;

    public Tenant30DayCountRecord(Long tenantId, Long count) {
        this.tenantId = tenantId;
        this.count = count;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getCount() {
        return count;
    }

}
