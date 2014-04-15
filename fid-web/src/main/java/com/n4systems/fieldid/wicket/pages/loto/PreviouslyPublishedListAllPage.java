package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.components.loto.PublishedProcedureActionsColumn;
import com.n4systems.fieldid.wicket.data.ProcedureDefinitionDataProvider;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

import java.util.List;

/**
 * Created by rrana on 2014-04-09.
 */
public class PreviouslyPublishedListAllPage extends ProceduresAllListPage{

    private ProcedureListPanel procedureDefinitionListPanel;
    private ProcedureDefinitionDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;


    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        dataProvider = new ProcedureDefinitionDataProvider("created", SortOrder.DESCENDING, PublishedState.PREVIOUSLY_PUBLISHED);
        add(procedureDefinitionListPanel = new ProcedureListPanel("procedureDefinitionListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {
                //TODO add place status
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {
                columns.add(new PublishedProcedureActionsColumn(this));
            }


        });
        procedureDefinitionListPanel.setOutputMarkupPlaceholderTag(true);

    }

}
