package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;

public class TemplatePage extends FieldIDTemplatePage {

    public TemplatePage() {
        add(new BookmarkablePageLink<Void>("twoColumnLeftLink", TwoColumnLeft.class));
        add(new BookmarkablePageLink<Void>("twoColumnRightLink", TwoColumnRight.class));
        add(new BookmarkablePageLink<Void>("twoColumnEqualLink", TwoColumnEqual.class));
        add(new BookmarkablePageLink<Void>("noColumnsLink", FirstTab.class));
        add(new BookmarkablePageLink<Void>("wide", Wide.class));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Template Page"), TemplatePage.class)
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Sample Layouts");
    }
}
