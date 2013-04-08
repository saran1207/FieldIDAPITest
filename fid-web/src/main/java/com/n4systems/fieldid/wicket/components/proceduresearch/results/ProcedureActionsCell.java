package com.n4systems.fieldid.wicket.components.proceduresearch.results;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureResultsPage;
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

        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList");
        actionsList.setOutputMarkupId(true);

        actionsList.add(new BookmarkablePageLink<Void>("viewLink", ProcedureResultsPage.class, PageParametersBuilder.id(rowModel.getObject().getId())));

        add(actionsList);
    }

}
