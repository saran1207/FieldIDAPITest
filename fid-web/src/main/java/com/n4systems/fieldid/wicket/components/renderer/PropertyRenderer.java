package com.n4systems.fieldid.wicket.components.renderer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class PropertyRenderer<T> implements IChoiceRenderer<T> {

    private static final Logger logger= Logger.getLogger(PropertyRenderer.class);

    private String displayProperty;
    private String idProperty;

    public PropertyRenderer(String displayProperty, String idProperty) {
        this.displayProperty = displayProperty;
        this.idProperty = idProperty;
    }

    @Override
    public Object getDisplayValue(T object) {
        try {
            return BeanUtils.getProperty(object, displayProperty);
        } catch (Exception e) {
            throw new IllegalStateException("can't render property " + displayProperty + " for  " + object + " of type " + object.getClass().getSimpleName());
        }
    }

    @Override
    public String getIdValue(T object, int index) {
        try {
            return BeanUtils.getProperty(object, idProperty);
        } catch (Exception e) {
            throw new IllegalStateException("can't get id property " + idProperty + " for  " + object + " of type " + object.getClass().getSimpleName());
        }
    }
}
