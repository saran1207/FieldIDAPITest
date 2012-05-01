package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    public SecretTestPage() {
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTab(new Model<String>("autocomplete org picker")) {
            @Override public Panel getPanel(String panelId) {
                return new AjaxLazyLoadPanel(panelId) {
                    private BaseOrg org;
                    @Override public Component getLazyLoadComponent(String id) {
                        return new AutoCompleteOrgPicker(id, new PropertyModel<BaseOrg>(this, "org"));
                    }
                };
            }
        });
        add(new AjaxTabbedPanel("tabs", tabs));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/component/tabbedPanel.css");
    }
}
