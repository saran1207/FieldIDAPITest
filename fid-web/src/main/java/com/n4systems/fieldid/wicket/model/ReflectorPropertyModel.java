package com.n4systems.fieldid.wicket.model;

import com.n4systems.util.reflection.ReflectionException;
import com.n4systems.util.reflection.Reflector;
import org.apache.wicket.model.IModel;

public class ReflectorPropertyModel<T> implements IModel<T> {

    private IModel<?> model;
    private String propertyExpression;

    public ReflectorPropertyModel(IModel<?> model, String propertyExpression) {
        this.model = model;
        this.propertyExpression = propertyExpression;
    }

    @Override
    public T getObject() {
        T value = null;
        try {
            value = (T) Reflector.getPathValue(model.getObject(), propertyExpression);
        } catch (ReflectionException e) { }
        return value;
    }

    @Override
    public void setObject(T object) {
    }

    @Override
    public void detach() {
    }

}
