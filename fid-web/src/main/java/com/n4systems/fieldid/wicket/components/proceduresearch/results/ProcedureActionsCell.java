package com.n4systems.fieldid.wicket.components.proceduresearch.results;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureResultsPage;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.util.views.RowView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ProcedureActionsCell extends Panel {

    public ProcedureActionsCell(String id, IModel<RowView> rowModel) {
        super(id);

        WebMarkupContainer actionsLink = new WebMarkupContainer("actionsLink");
        actionsLink.setOutputMarkupId(true);
        actionsLink.add(new ContextImage("dropwDownArrow", "images/dropdown_arrow.png"));
        add(actionsLink);

        Procedure procedure = (Procedure) rowModel.getObject().getEntity();

        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList");
        actionsList.setOutputMarkupId(true);

        BookmarkablePageLink<Void> viewLink = new BookmarkablePageLink<Void>("viewLink", ProcedureResultsPage.class, PageParametersBuilder.id(rowModel.getObject().getId()));
        viewLink.setVisible(procedure.getWorkflowState() == ProcedureWorkflowState.UNLOCKED || procedure.getWorkflowState() == ProcedureWorkflowState.LOCKED);
        actionsList.add(viewLink);

        actionsList.add(new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(procedure.getAsset().getId())));

        add(actionsList);
    }

}


