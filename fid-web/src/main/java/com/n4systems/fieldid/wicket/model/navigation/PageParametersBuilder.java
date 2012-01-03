package com.n4systems.fieldid.wicket.model.navigation;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class PageParametersBuilder {

    public static PageParameters uniqueId(Long uniqueId) {
        return param("uniqueID", uniqueId);
    }

    public static PageParameters id(Long id) {
        return param("id", id);
    }

    public static PageParameters param(String paramName, Object paramValue) {
        PageParameters simpleParams = new PageParameters();
        simpleParams.add(paramName, paramValue.toString());
        return simpleParams;
    }

}
