package com.n4systems.fieldid.wicket.util;

import ch.lambdaj.function.argument.Argument;
import ch.lambdaj.function.argument.ArgumentsFactory;
import com.google.common.base.Preconditions;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.PropertyModel;

import java.lang.reflect.InvocationHandler;

public class ProxyModel<T> {

    private PropertyModel model;
    private T proxy;
    private InvocationHandler handler;

    public static <T> PropertyModel<T> of(Object value, T proxiedValue) {
        Preconditions.checkArgument(!(value instanceof WebPage), "Can't use proxied models based on web page because it will try to create another instance of the page.  hint : you may want to create an internal class and use that instead.");
        Argument<T> argument = ArgumentsFactory.actualArgument(proxiedValue);
        String propertyName = argument.getInkvokedPropertyName();
        return new PropertyModel<T>(value, propertyName);
    }


}
