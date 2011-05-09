package com.n4systems.fieldid.wicket.model.navigation;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

public class NavigationItem implements Serializable {

    private IModel labelModel;
    private Class<? extends WebPage> pageClass;
    private String nonWicketUrl;
    private boolean display = true;

    public NavigationItem(IModel labelModel, Class<? extends WebPage> pageClass) {
        this.labelModel = labelModel;
        this.pageClass = pageClass;
    }

    public NavigationItem(IModel labelModel, String nonWicketUrl) {
        this.labelModel = labelModel;
        this.nonWicketUrl = nonWicketUrl;
    }

    public IModel getLabelModel() {
        return labelModel;
    }

    public Class<? extends WebPage> getPageClass() {
        return pageClass;
    }

    public String getNonWicketUrl() {
        return nonWicketUrl;
    }

    public boolean display() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
