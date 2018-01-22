package com.n4systems.fieldid.wicket.pages.customers;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created by agrabovskis on 2018-01-02.
 */
public class CustomerImportPanel extends Panel {

    private static final Logger logger = Logger.getLogger(CustomerImportPanel.class);

    public CustomerImportPanel(String id) {
        super(id);
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
    }

    private void addComponents() {

        Link downloadDataLink = new Link("downloadDataLink") {

            @Override
            public void onClick() {

            }
        };
        add(downloadDataLink);

        Link downloadTemplateLink = new Link("downloadTemplateLink") {

            @Override
            public void onClick() {

            }
        };
        add(downloadTemplateLink);


    }
}
