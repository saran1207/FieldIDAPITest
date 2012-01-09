package com.n4systems.fieldid.wicket.components.saveditems;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;

public class SavedItemsDropdown extends Panel {

    public SavedItemsDropdown(String id) {
        super(id);

        add(new ContextImage("downArrow", "images/down-arrow.png"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/my_saved_items.js");
        response.renderCSSReference("style/newCss/component/my_saved_items.css");
    }

}
