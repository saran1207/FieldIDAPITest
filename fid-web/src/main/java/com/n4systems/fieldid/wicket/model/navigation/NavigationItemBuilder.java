package com.n4systems.fieldid.wicket.model.navigation;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class NavigationItemBuilder {

    private Class<? extends WebPage> pageClass;
    private IModel<String> label;
    private String nonWicketUrl;
    private boolean display = true;

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
        this.label = new Model<String>(label);
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

    public NavigationItem build() {
        NavigationItem item;
        if (nonWicketUrl != null) {
            item = new NavigationItem(label, nonWicketUrl);
        } else {
            item = new NavigationItem(label, pageClass);
        }
        item.setDisplay(display);
        return item;
    }

}
