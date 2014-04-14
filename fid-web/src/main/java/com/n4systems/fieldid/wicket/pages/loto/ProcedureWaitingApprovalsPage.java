package com.n4systems.fieldid.wicket.pages.loto;


import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureApprovalsActionsColumn;
import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class ProcedureWaitingApprovalsPage extends ProcedureApprovalsPage {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private ProcedureListPanel procedureListPanel;
    private ProcedureDefinitionDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    private enum UserState { CREATOR, APPROVER, VIEWER };
    private UserState userState = UserState.VIEWER;


    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        dataProvider = new ProcedureDefinitionDataProvider("created", SortOrder.DESCENDING);
        add(procedureListPanel = new ProcedureListPanel("procedureListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {
                //TODO add place status
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {

                columns.add(new ProcedureApprovalsActionsColumn(this));
            }

        });
        procedureListPanel.setOutputMarkupPlaceholderTag(true);

    }

    private class ProcedureDefinitionDataProvider extends FieldIDDataProvider<ProcedureDefinition> {

        public ProcedureDefinitionDataProvider(String order, SortOrder sortOrder) {
            setSort(order, sortOrder);
        }

        @Override
        public Iterator<? extends ProcedureDefinition> iterator(int first, int count) {
            List<? extends ProcedureDefinition> procedureDefinitionList = procedureDefinitionService.getProcedureDefinitionsFor(PublishedState.WAITING_FOR_APPROVAL, getSort().getProperty(), getSort().isAscending(), first, count);
            return procedureDefinitionList.iterator();
        }

        @Override
        public int size() {
           Long waitingApprovalsCount = procedureDefinitionService.getWaitingApprovalsCount();
           return waitingApprovalsCount.intValue();
        }

        @Override
        public IModel<ProcedureDefinition> model(final ProcedureDefinition object) {
            return new AbstractReadOnlyModel<ProcedureDefinition>() {
                @Override
                public ProcedureDefinition getObject() {
                    return object;
                }
            };
        }

    }

}
