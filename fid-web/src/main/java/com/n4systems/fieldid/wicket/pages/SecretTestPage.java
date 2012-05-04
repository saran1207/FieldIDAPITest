package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.AutoCompleteSearch;
import com.n4systems.model.Asset;
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

    private BaseOrg org;
    private Asset asset;
    
    public SecretTestPage() {
        List<ITab> tabs = new ArrayList<ITab>();
        tabs.add(new TestTab("AutoComplete OrgPicker") {
            @Override protected Component getTestComponent(String id) {
                return new AutoCompleteOrgPicker(id, new PropertyModel<BaseOrg>(SecretTestPage.this, "org"));
            }
        });


        tabs.add(new TestTab("AutoComplete Asset Search") {
            @Override protected Component getTestComponent(String id) {
                return new AutoCompleteSearch(id, new PropertyModel<Asset>(SecretTestPage.this, "asset"));
            }
        });


        add(new AjaxTabbedPanel("tabs", tabs));
    }

    public BaseOrg getOrg() {
        return org;
    }
    
    public void setOrg(BaseOrg org) {
        this.org = org;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/component/tabbedPanel.css");
    }

    abstract class TestTab extends AbstractTab {
        
        private PropertyModel<String> testModel;
        
        public TestTab(String title) {
            super(new Model<String>(title));
        }
        
        @Override public Panel getPanel(String panelId) {
                return new AjaxLazyLoadPanel(panelId) {
                    @Override public Component getLazyLoadComponent(String id) {
                        return getTestComponent(id);
                    }
                };
            }

        protected abstract Component getTestComponent(String id);

    }
}
