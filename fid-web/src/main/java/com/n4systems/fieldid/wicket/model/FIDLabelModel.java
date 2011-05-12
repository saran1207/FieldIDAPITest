package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class FIDLabelModel implements IModel<String> {

    private IModel<String> propertyKeyModel;
    private Object[] parameters;

    public FIDLabelModel(IModel<?> model, String propertyName) {
        this.propertyKeyModel = new PropertyModel<String>(model, propertyName);
    }

    public FIDLabelModel(IModel<String> propertyKeyModel) {
        this.propertyKeyModel = propertyKeyModel;
    }

    public FIDLabelModel(String propertyKey) {
        this.propertyKeyModel = new Model<String>(propertyKey);
    }

    public FIDLabelModel(String propertyKey, Object... params) {
        this.propertyKeyModel = new Model<String>(propertyKey);
        this.parameters = params;
    }

    @Override
    public String getObject() {
        String propertyKey = propertyKeyModel.getObject();
        Localizer localizer = Application.get().getResourceSettings().getLocalizer();
        String label =  localizer.getString(propertyKey, null);
        if (label != null && parameters != null ) {
            label = String.format(label, parameters);
        } else if (label == null) {
            label = propertyKey;
        }
        return label;
    }

    @Override
    public void setObject(String object) {
    }

    @Override
    public void detach() {
        propertyKeyModel.detach();
    }

}
