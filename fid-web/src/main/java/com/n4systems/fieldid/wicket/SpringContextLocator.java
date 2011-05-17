package com.n4systems.fieldid.wicket;

import org.apache.wicket.spring.ISpringContextLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class SpringContextLocator implements ISpringContextLocator {

    private ServletContext servletContext;

    public SpringContextLocator(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ApplicationContext getSpringContext() {
        return (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

}
