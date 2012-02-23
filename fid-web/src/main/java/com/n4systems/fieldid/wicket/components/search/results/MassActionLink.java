package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import java.lang.reflect.Constructor;

public class MassActionLink<P extends WebPage> extends Link {

    private Class<P> pageClass;
    private Object model;

    public MassActionLink(String id, Class<P> pageClass, Object model) {
        super(id);
        this.pageClass = pageClass;
        this.model = model;
    }

    @Override
    public void onClick() {
        try {
            final Constructor<P> constructor = pageClass.getConstructor(IModel.class);
            getRequestCycle().setResponsePage(constructor.newInstance(model));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
