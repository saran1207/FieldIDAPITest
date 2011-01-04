package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;

public class FieldIDWicketPage extends WebPage {

    public FieldIDWicketPage(PageParameters params) {
        super(params);
        add(CSSPackageResource.getHeaderContribution("style/fieldid.css"));
        add(CSSPackageResource.getHeaderContribution("style/eventFormEdit.css"));
    }

    public FieldIDWicketPage() {
        this(null);
    }

}
