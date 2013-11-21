package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.template.ActionGroup;
import com.n4systems.fieldid.wicket.components.template.SubHeader;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public abstract class NoColumns extends FieldIDTemplatePage {

    public NoColumns() {}

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Next page in hierarchy"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), this.getClass())
        ));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                new NavigationItem(Model.of("Tab Item"), FirstTab.class),
                new NavigationItem(Model.of("Tab Item"), SecondTab.class)
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Page Heading");
    }

    @Override
    protected Component createSubHeader(String subHeaderId) {
        return new SubHeader(subHeaderId);
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

}
