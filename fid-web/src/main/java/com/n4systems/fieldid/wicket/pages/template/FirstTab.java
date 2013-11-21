package com.n4systems.fieldid.wicket.pages.template;

import org.apache.wicket.markup.html.basic.Label;

public class FirstTab extends NoColumns {

    public FirstTab() {
        add(new Label("text", "This layout shows all the possible page header options and a sample table listing in a no column layout."));
    }
}
