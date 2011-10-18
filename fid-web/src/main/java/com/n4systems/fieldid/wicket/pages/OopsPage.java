package com.n4systems.fieldid.wicket.pages;

public class OopsPage extends FieldIDFrontEndPage {

    public OopsPage() {
        getResponse().redirect("/fieldid/error.action");
    }

}
