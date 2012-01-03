package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.reporting.EventReportCriteriaPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingReportSectionCollapseContainer;
import com.n4systems.fieldid.wicket.components.reporting.results.ReportResultsPanel;
import com.n4systems.fieldid.wicket.components.reporting.results.ReportingMassActionPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class ReportingResultsPage extends FieldIDFrontEndPage {

    private EventReportCriteriaModel reportCriteriaModel;
    private ReportResultsPanel reportResultsPanel;

    public ReportingResultsPage(EventReportCriteriaModel reportCriteriaModel) {
        this.reportCriteriaModel = reportCriteriaModel;
        PropertyModel<EventReportCriteriaModel> reportCriteriaPropertyModel = new PropertyModel<EventReportCriteriaModel>(this, "reportCriteriaModel");

        add(reportResultsPanel = new ReportResultsPanel("resultsPanel", reportCriteriaPropertyModel));

        add(new BookmarkablePageLink<Void>("startNewReportLink", ReportingPage.class));
        add(createSaveReportLink("saveReportLink", true));
        add(createSaveReportLink("saveReportLinkAs", false));

        add(new BookmarkablePageLink<Void>("startNewReportLink2", ReportingPage.class));
        add(createSaveReportLink("saveReportLink2", true));
        add(createSaveReportLink("saveReportLinkAs2", false));

        SlidingReportSectionCollapseContainer criteriaExpandContainer = new SlidingReportSectionCollapseContainer("criteriaExpandContainer", new FIDLabelModel("label.reportcriteria"));
        criteriaExpandContainer.addContainedPanel(new EventReportCriteriaPanel("criteriaPanel", reportCriteriaPropertyModel) {
        	@Override
        	protected void onNoDisplayColumnsSelected() {
        		reportResultsPanel.setVisible(false);
        	}
        });

        add(criteriaExpandContainer);
        add(new ReportingMassActionPanel("massActionPanel", reportCriteriaPropertyModel));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/reporting.css");
    }

    private Link createSaveReportLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override
            public void onClick() {
                setResponsePage(new SaveReportPage(reportCriteriaModel, ReportingResultsPage.this, overwrite));
            }
        };
        if (!overwrite) {
            // If this is not overwrite (ie the Save As link), it should be invisible if this isn't an existing saved report
            link.setVisible(reportCriteriaModel.getSavedReportId() != null);
        }
        return link;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(this, "pageLabel"));
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("label.reporting_results");
        if (reportCriteriaModel.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + reportCriteriaModel.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                NavigationItemBuilder.aNavItem().label("nav.new_report").page(ReportingResultsPage.class).build(),
                NavigationItemBuilder.aNavItem().label("nav.saved_reports").page("/savedReports.action").build()));
    }

}
