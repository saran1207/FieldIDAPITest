package com.n4systems.fieldid.wicket.model.navigation;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class NavigationItemBuilder {

    private Class<? extends WebPage> pageClass;
    private PageParameters parameters = new PageParameters();
    private IModel<String> label;
    private String nonWicketUrl;
    private boolean display = true;
    private boolean onRight = false;

    public static NavigationItemBuilder aNavItem() {
        return new NavigationItemBuilder();
    }

    public NavigationItemBuilder page(Class<? extends WebPage> pageClass) {
        this.pageClass = pageClass;
        return this;
    }

    public NavigationItemBuilder page(String nonWicketUrl) {
        this.nonWicketUrl = nonWicketUrl;
        return this;
    }

    public NavigationItemBuilder label(String label) {
        this.label = new FIDLabelModel(label);
        return this;
    }

    public NavigationItemBuilder label(IModel<String> label) {
        this.label = label;
        return this;
    }

    public NavigationItemBuilder cond(boolean display) {
        this.display = display;
        return this;
    }

    public NavigationItemBuilder params(PageParameters params) {
        this.parameters = params;
        return this;
    }

    public NavigationItemBuilder onRight() {
        onRight = true;
        return this;
    }

    public NavigationItem build() {
        NavigationItem item;
        if (nonWicketUrl != null) {
            item = new NavigationItem(label, nonWicketUrl);
        } else {
            item = new NavigationItem(label, pageClass);
        }
        item.setDisplay(display);
        item.setParameters(parameters);
        item.setOnRight(onRight);
        return item;
    }

}
