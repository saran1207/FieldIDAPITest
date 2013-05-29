package com.n4systems.services.tenant;

import com.n4systems.model.Tenant;

import java.io.Serializable;

public class Tenant30DayCountRecord implements Serializable {

    private Tenant tenant;
    private Long count;

    public Tenant30DayCountRecord(Tenant tenant, Long count) {
        this.tenant = tenant;
        this.count = count;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Long getCount() {
        return count;
    }

}
