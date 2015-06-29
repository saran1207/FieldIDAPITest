package com.n4systems.fieldid.api.mobile.resources.procedure;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadWriteModel;

/**
 * Created by rrana on 2015-01-07.
 */
public class ApiEnergySource extends ApiReadWriteModel {
    private String isolationPointSourceType;
    private String source;

    public ApiEnergySource() {
        //Do nothing, look pretty...
    }

    public String getIsolationPointSourceType() {
        return isolationPointSourceType;
    }

    public void setIsolationPointSourceType(String isolationPointSourceType) {
        this.isolationPointSourceType = isolationPointSourceType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
