package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.event.ProcedureAuditEventService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.ProcedureAuditEventSummaryPage;
import com.n4systems.model.ProcedureAuditEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

/**
 * Created by rrana on 2014-06-16.
 */
public class ProcedureAuditCompletedListPage extends LotoPage {

    @SpringBean
    private ProcedureAuditEventService procedureAuditEventService;

    public ProcedureAuditCompletedListPage(PageParameters params) {
        super(params);

        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        ListView listView;

        listContainer.add(listView = new ListView<ProcedureAuditEvent>("list", new ProcedureListModel()) {

            @Override
            protected void populateItem(ListItem<ProcedureAuditEvent> item) {
                final IModel<ProcedureAuditEvent> procedureAuditEvent = item.getModel();
                item.add(new Label("completedDate", new DayDisplayModel(new PropertyModel<Date>(procedureAuditEvent, "completedDate"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("dateDue", new DayDisplayModel(new PropertyModel<Date>(procedureAuditEvent, "dueDate"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("procedureDefinition", new PropertyModel<String>(procedureAuditEvent, "procedureDefinition.procedureCode")));
                item.add(new Label("procedureType", new PropertyModel<String>(procedureAuditEvent, "procedureDefinition.procedureType.label")));
                item.add(new Label("performedBy", new PropertyModel<String>(procedureAuditEvent, "performedBy.displayName")));
                item.add(new Label("eventResult", new PropertyModel<String>(procedureAuditEvent, "eventResult")));
                item.add(new Label("eventStatus", new PropertyModel<Long>(procedureAuditEvent, "eventStatus.displayName")));
                item.add(new BookmarkablePageLink<ProcedureAuditEventSummaryPage>("viewLink", ProcedureAuditEventSummaryPage.class, PageParametersBuilder.id(procedureAuditEvent.getObject().getId())));

            }
        });

        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        blankSlate.setVisible(listView.getList().isEmpty());
        add(blankSlate);

    }

    class ProcedureListModel extends LoadableDetachableModel<List<ProcedureAuditEvent>> {

        @Override
        protected List<ProcedureAuditEvent> load() {
            return procedureAuditEventService.getAllCompletedAuditsForAsset(assetModel.getObject());
        }
    }

}
