package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

public class TwoColumnLeft extends FieldIDTemplatePage {

    public TwoColumnLeft() {
        add(new Image("featureImage", new ContextRelativeResource("/img/_temp/hoist.jpg")));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Next page in hierarchy"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), TwoColumnLeft.class)
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Two Column: Left Sidebar");
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    class ActionGroup extends Fragment {

        public ActionGroup(String id) {
            super(id, "actions", TwoColumnLeft.this);

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
