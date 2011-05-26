package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {

    // Placeholder page for home.action before it's ported over to wicket
    public HomePage(PageParameters params) {
        super(params);

        getResponse().redirect("/fieldid/home.action");
    }

}
