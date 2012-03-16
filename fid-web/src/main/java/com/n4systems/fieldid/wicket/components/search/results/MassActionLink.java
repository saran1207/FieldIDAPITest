package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import java.lang.reflect.Constructor;

public class MassActionLink<P extends WebPage> extends Link {

    private static final Logger logger = Logger.getLogger(MassActionLink.class);

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
            setResponsePage(constructor.newInstance(model));
        } catch (Exception e) {
            logger.error("error instantiating mass action page save name as page", e);
            throw new RuntimeException(e);
        }
    }

}
