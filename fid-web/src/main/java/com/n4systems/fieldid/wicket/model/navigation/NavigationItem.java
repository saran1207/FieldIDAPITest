package com.n4systems.fieldid.wicket.model.navigation;

import com.google.common.collect.Lists;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;
import java.util.List;

public class NavigationItem implements Serializable {

    private IModel labelModel;
    private Class<? extends WebPage> pageClass;
    private PageParameters parameters = new PageParameters();
    private String nonWicketUrl;
    private boolean display = true;
    private boolean onRight = false;
    private List<String> ignoreParams;

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

    public PageParameters getParameters() {
        return parameters;
    }

    public void setParameters(PageParameters parameters) {
        this.parameters = parameters;
    }

    public boolean isOnRight() {
        return onRight;
    }

    public void setOnRight(boolean onRight) {
        this.onRight = onRight;
    }

    public void setIgnoreParams(String... params) {
        this.ignoreParams = Lists.newArrayList(params);
    }

    public void setIgnoreParams(List<String> params) {
        this.ignoreParams = params;
    }

    public List<String> getIgnoreParams() {
        return ignoreParams;
    }
}
