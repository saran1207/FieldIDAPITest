package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.components.localization.SelectLanguagePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class SelectLanguagePage extends FieldIDAuthenticatedPage {

    public SelectLanguagePage() {
        add(new SelectLanguagePanel("selectLanguagePanel") {
            @Override
            public void onLanguageSelection(AjaxRequestTarget target) {
                target.appendJavaScript("window.parent.jQuery.colorbox.close();window.parent.location.reload();");
            }
        });
    }
}
