package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.request.flow.RedirectToUrlException;

public class OopsPage extends FieldIDFrontEndPage {

    public OopsPage() {
        throw new RedirectToUrlException("/error.action");
    }

}
