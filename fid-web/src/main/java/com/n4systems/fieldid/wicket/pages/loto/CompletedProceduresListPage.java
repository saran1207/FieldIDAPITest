package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
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


public class CompletedProceduresListPage extends LotoPage {

    @SpringBean
    private ProcedureService procedureService;

    public CompletedProceduresListPage(PageParameters params) {
        super(params);

        add(new BookmarkablePageLink<ProcedureDefinitionListPage>("activeLink", ProcedureDefinitionListPage.class, PageParametersBuilder.uniqueId(getAssetId())));
        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("previouslyPublishedListLink", PreviouslyPublishedListPage.class, PageParametersBuilder.uniqueId(getAssetId())));
        add(new BookmarkablePageLink<CompletedProceduresListPage>("completedProceduresListLink", CompletedProceduresListPage.class, PageParametersBuilder.uniqueId(getAssetId())));

        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        ListView listView;

        listContainer.add(listView = new ListView<Procedure>("list", new ProcedureListModel()) {

            @Override
            protected void populateItem(ListItem<Procedure> item) {
                final IModel<Procedure> procedure = item.getModel();

                item.add(new Label("dateLocked", new DayDisplayModel(new PropertyModel<Date>(procedure, "lockDate"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("lockedBy", new PropertyModel<String>(procedure, "lockedBy.displayName")));
                item.add(new Label("dateUnlocked", new DayDisplayModel(new PropertyModel<Date>(procedure, "unlockDate"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("unlockedBy", new PropertyModel<String>(procedure, "unlockedBy.displayName")));
                item.add(new Label("procedureCode", new PropertyModel<String>(procedure, "type.procedureCode")));
                item.add(new Label("revision", new PropertyModel<Long>(procedure, "type.revisionNumber")));
                item.add(new BookmarkablePageLink<ProcedureResultsPage>("viewLink", ProcedureResultsPage.class, PageParametersBuilder.id(procedure.getObject().getId())));
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
            return procedureService.getCompletedProcedures(assetModel.getObject());
        }
    }

}
