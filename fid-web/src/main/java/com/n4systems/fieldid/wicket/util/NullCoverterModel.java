package com.n4systems.fieldid.wicket.util;

import com.n4systems.util.StringUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class NullCoverterModel extends Model<String> {

    private String nullReplacement;
    private IModel<String> model;

    public NullCoverterModel(IModel<String> model, String nullReplacement) {
        this.model = model;
        this.nullReplacement = nullReplacement;
    }

    @Override
    public String getObject() {
        String value = model.getObject();
        return (StringUtils.isEmpty(value)) ? nullReplacement : value;
    }
}
