package com.n4systems.fieldid.wicket;

import org.apache.wicket.spring.ISpringContextLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class SpringContextLocator implements ISpringContextLocator {

    @Override
    public ApplicationContext getSpringContext() {
        return (ApplicationContext) FieldIDWicketApp.get().getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

}
