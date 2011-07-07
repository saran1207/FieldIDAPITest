package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.reporting.EventReportCriteriaPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ReportingPage extends FieldIDLoggedInPage {

    public ReportingPage(PageParameters params) {
        super(params);
        add(new EventReportCriteriaPanel("reportCriteriaPanel"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.new_report").page(ReportingPage.class).build(),
                aNavItem().label("nav.saved_reports").page("savedReports.action").build()));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("speed.reporting"));
    }
}
