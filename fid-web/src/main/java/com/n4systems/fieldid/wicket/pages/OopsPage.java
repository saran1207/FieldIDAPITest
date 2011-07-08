package com.n4systems.fieldid.wicket.pages;

public class OopsPage extends FieldIDLoggedInPage {

    public OopsPage() {
        getResponse().redirect("/fieldid/error.action");
    }

}
