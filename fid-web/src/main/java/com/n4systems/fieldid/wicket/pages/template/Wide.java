package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;

public class Wide extends FieldIDTemplatePage {

    public Wide() {
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Next page in hierarchy"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), Wide.class)
        ));
    }

    @Override
    public String getMainCss() {
        return "wide";
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Two Column: Equal Width");
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    class ActionGroup extends Fragment {

        public ActionGroup(String id) {
            super(id, "actions", Wide.this);

            add(new Link<Void>("primaryLink") {
                @Override
                public void onClick() {
                }
            });

            add(new Link<Void>("secondaryLink") {
                @Override
                public void onClick() {
                }
            });
        }
    }
}
