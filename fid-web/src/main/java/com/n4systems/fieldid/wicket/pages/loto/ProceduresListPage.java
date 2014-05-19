package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
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


public class ProceduresListPage extends LotoPage {

    @SpringBean
    private ProcedureService procedureService;

    public ProceduresListPage(PageParameters params) {
        super(params);

        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        ListView listView;

        listContainer.add(listView = new ListView<Procedure>("list", new ProcedureListModel()) {

            @Override
            protected void populateItem(ListItem<Procedure> item) {
                final IModel<Procedure> procedure = item.getModel();

                if(procedure.getObject().getWorkflowState().equals(ProcedureWorkflowState.OPEN)) {
                    item.add(new Label("dateLocked"));
                    item.add(new Label("dateUnlocked"));
                }else {
                    item.add(new Label("dateLocked", new DayDisplayModel(new PropertyModel<Date>(procedure, "lockDate"), true, getCurrentUser().getTimeZone())));
                    if(procedure.getObject().getWorkflowState().equals(ProcedureWorkflowState.LOCKED)) {
                        item.add(new Label("dateUnlocked"));
                    } else {
                        item.add(new Label("dateUnlocked", new DayDisplayModel(new PropertyModel<Date>(procedure, "unlockDate"), true, getCurrentUser().getTimeZone())));
                    }
                }
                item.add(new Label("lockedBy", new PropertyModel<String>(procedure, "lockedBy.displayName")));
                item.add(new Label("unlockedBy", new PropertyModel<String>(procedure, "unlockedBy.displayName")));
                item.add(new Label("procedureCode", new PropertyModel<String>(procedure, "type.procedureCode")));
                item.add(new Label("procedureType", new PropertyModel<String>(procedure, "type.procedureType.label")));
                item.add(new Label("revision", new PropertyModel<Long>(procedure, "type.revisionNumber")));
                item.add(new Label("state", new PropertyModel<Long>(procedure, "workflowState.label")));
                item.add(new BookmarkablePageLink<ProcedureResultsPage>("viewLink", ProcedureResultsPage.class, PageParametersBuilder.id(procedure.getObject().getId())).
                        setVisible(!procedure.getObject().getWorkflowState().equals(ProcedureWorkflowState.OPEN)));

            }
        });
        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        blankSlate.setVisible(listView.getList().isEmpty());
        add(blankSlate);

    }

    class ProcedureListModel extends LoadableDetachableModel<List<Procedure>> {

        @Override
        protected List<Procedure> load() {
            return procedureService.getAllProcedures(assetModel.getObject());
        }
    }

}
