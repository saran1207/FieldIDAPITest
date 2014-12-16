package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModel;

/**
 * This is just a simple object representing the JSON structure for the ApiWarningTemplateService class.
 *
 * Created by Jordan Heath on 11/26/14.
 */
public class ApiWarningTemplate extends ApiReadWriteModel {
    private String name;
    private String warning;

    public ApiWarningTemplate() {
        //Do nothing, look pretty...
    }

    public ApiWarningTemplate(String name, String warning) {
        this.name = name;
        this.warning = warning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
