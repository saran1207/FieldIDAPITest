package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.RecurringLotoSchedulesPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ProcedureWithoutAuditsActionCell extends Panel {

    public ProcedureWithoutAuditsActionCell(String id, IModel<? extends ProcedureDefinition> procedureModel) {
        super(id);

        add(new BookmarkablePageLink<Void>("createAuditLink", RecurringLotoSchedulesPage.class, PageParametersBuilder.uniqueId(procedureModel.getObject().getAsset().getId())));
    }
}
