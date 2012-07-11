package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.model.IModel;

import java.util.MissingResourceException;

public class EnumLabelModel implements IModel<String> {

    private Enum<?> value;
    private String propertyKey;


    public EnumLabelModel(Enum<?> value) {
        this.value = value;
        propertyKey = value.getClass().getSimpleName()+ "." + value.name();
    }

    @Override
    public String getObject() {
        Localizer localizer = Application.get().getResourceSettings().getLocalizer();

        String label = null;
        try {
            label = localizer.getString(propertyKey, null);
        } catch(MissingResourceException e) {
            label = propertyKey;
        }
        return label;
    }

    @Override
    public void setObject(String object) {
        ; // do nothing.
    }

    @Override
    public void detach() {
        ; // do nothing.
    }
}
