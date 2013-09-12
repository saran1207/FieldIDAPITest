package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.components.localization.SelectLanguagePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;

public class SelectLanguagePage extends FieldIDAuthenticatedPage {

    public SelectLanguagePage() {
        add(new SelectLanguagePanel("selectLanguagePanel") {
            @Override
            public void onLanguageSelection(AjaxRequestTarget target) {
                target.appendJavaScript("window.parent.jQuery.colorbox.close();");
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }
}
