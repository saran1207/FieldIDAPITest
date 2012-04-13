package com.n4systems.fieldid.wicket.model;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class StringAppendModel extends LoadableDetachableModel<String> {

    private String separator;
    private IModel<String>[] joinModels;

    public StringAppendModel(String separator, IModel<String>... joinModels) {
        this.separator = separator;
        this.joinModels = joinModels;
    }
    
    @Override
    protected String load() {
        return StringUtils.join(getModelValues(), separator);
    }

    private List<String> getModelValues() {
        List<String> models = new ArrayList<String>();
        for (IModel<String> joinModel : joinModels) {
            models.add(joinModel.getObject());
        }
        return models;
    }

}
