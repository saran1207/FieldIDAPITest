package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class AllTemplates extends FieldIDTemplatePage {

    private AjaxLink toggleWideLink;
    private AjaxLink toggleRegularLink;

    public AllTemplates() {

        add(toggleWideLink = new AjaxLink<Void>("toggleWide") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("$('main').addClass('wide')");
                toggleWideLink.setVisible(false);
                toggleRegularLink.setVisible(true);
                target.add(toggleWideLink, toggleRegularLink);
            }
        });

        add(toggleRegularLink = new AjaxLink<Void>("toggleRegular") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("$('main').removeClass('wide')");
                toggleWideLink.setVisible(true);
                toggleRegularLink.setVisible(false);
                target.add(toggleWideLink, toggleRegularLink);
            }
        });

        toggleWideLink.setOutputMarkupPlaceholderTag(true);
        toggleRegularLink.setOutputMarkupPlaceholderTag(true);
        toggleRegularLink.setVisible(false);

    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Template Page"), AllTemplates.class)
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "All Templates");
    }
}


