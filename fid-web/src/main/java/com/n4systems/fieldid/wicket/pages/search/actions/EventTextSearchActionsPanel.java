package com.n4systems.fieldid.wicket.pages.search.actions;

import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateEventsPage;
import com.n4systems.fieldid.wicket.pages.print.PrintInspectionCertPage;
import com.n4systems.fieldid.wicket.pages.print.PrintObservationCertReportPage;
import com.n4systems.fieldid.wicket.pages.print.PrintThisReportPage;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class EventTextSearchActionsPanel extends Panel {

    private IModel<Set<String>> selectedIds;

    private static final String COLORBOX_CLASS = "event_text_search_colorbox";

    public EventTextSearchActionsPanel(String id, final IModel<Set<String>> selectedIds) {
        super(id);
        this.selectedIds = selectedIds;

        add(new Link("massUpdateLink") {
            @Override
            public void onClick() {
                setResponsePage(new MassUpdateEventsPage(createEventSearchCriteria()) {
                    @Override
                    protected void returnToPage(IModel<EventReportCriteria> criteriaModel) {
                        setResponsePage(EventTextSearchActionsPanel.this.getPage());
                    }
                });
            }
        });

        Callable<IModel> modelFactory = new SearchModelFactory();
        add(makeLinkLightBoxed(new MassActionLink<PrintThisReportPage>("printReportLink", PrintThisReportPage.class, modelFactory)));
        add(makeLinkLightBoxed(new MassActionLink<PrintInspectionCertPage>("printSelectedPdfReportsLink", PrintInspectionCertPage.class, modelFactory)));
        add(makeLinkLightBoxed(new MassActionLink<PrintObservationCertReportPage>("printSelectedObservationReportsLink", PrintObservationCertReportPage.class, modelFactory)));
    }

    class SearchModelFactory implements Callable<IModel>, Serializable {
        @Override
        public IModel call() throws Exception {
            return createEventSearchCriteria();
        }
    }

    private IModel<EventReportCriteria> createEventSearchCriteria() {
        EventReportCriteria criteria = new EventReportCriteria();

        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());

        criteria.setSortDirection(SortDirection.DESC);

        List<Long> ids = new ArrayList<Long>();
        for (String id : selectedIds.getObject()) {
            ids.add(Long.parseLong(id));
        }

        MultiIdSelection multiIdSelection = new MultiIdSelection();
        criteria.setSelection(multiIdSelection);

        criteria.setColumnGroups(reportConfiguration.getColumnGroups());
        return Model.of(criteria);
    }

    protected <T extends Link> T makeLinkLightBoxed(T link) {
        link.setOutputMarkupId(true);
        link.add(new AttributeAppender("class", new Model<String>(COLORBOX_CLASS), " "));
        return link;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnLoadJavaScript("jQuery('." + COLORBOX_CLASS + "').colorbox({maxHeight: '600px', width: '600px', height:'350px', ajax:true, iframe: true});");
    }
}
