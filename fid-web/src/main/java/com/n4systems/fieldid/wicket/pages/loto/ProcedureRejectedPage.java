package com.n4systems.fieldid.wicket.pages.loto;


import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureApprovalsActionsColumn;
import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureRejectedDateColumn;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.util.Iterator;
import java.util.List;

public class ProcedureRejectedPage extends ProcedureApprovalsPage implements IAjaxIndicatorAware {
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
    public String getAjaxIndicatorMarkupId(){
        return "loadingImage";
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        Form<Void> form = new Form<Void>("form");
        add(form);

        final TextField<String> field = new TextField<String>("field", new Model<String>(""));
        form.add(field);


        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                ProcedureRejectedPage.this.dataProvider.setSearchTerm(field.getDefaultModelObjectAsString());
                target.add(procedureListPanel);
            }
        };
        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(new Long(500)));
        field.add(onChangeAjaxBehavior);


        dataProvider = new ProcedureDefinitionDataProvider("created", SortOrder.DESCENDING);

        add(new ContextImage("loadingImage", "images/loading-small.gif"));

        add(procedureListPanel = new ProcedureListPanel("procedureListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {
                columns.add(new ProcedureRejectedDateColumn(new FIDLabelModel("label.rejected_on"), "rejectedDate", "rejectedDate"));
                columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.rejected_by"),"rejectedBy.firstName", "rejectedBy.fullName"));
                columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.rejectedReason"),"rejectedReason", "rejectedReason"));
         }

            @Override
            protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {

                columns.add(new ProcedureApprovalsActionsColumn(this));
            }

            @Override
            protected FIDFeedbackPanel getErrorFeedbackPanel() {
                return feedbackPanel;
            }

        });
        procedureListPanel.setOutputMarkupPlaceholderTag(true);

    }

    private class ProcedureDefinitionDataProvider extends FieldIDDataProvider<ProcedureDefinition> {

        String searchTerm;

        public ProcedureDefinitionDataProvider(String order, SortOrder sortOrder) {
            setSort(order, sortOrder);
            searchTerm = "";
        }

        @Override
        public Iterator<? extends ProcedureDefinition> iterator(int first, int count) {
            if(searchTerm.equalsIgnoreCase("")) {
                List<? extends ProcedureDefinition> procedureDefinitionList = procedureDefinitionService.getProcedureDefinitionsFor(PublishedState.REJECTED, getSort().getProperty(), getSort().isAscending(), first, count);
                return procedureDefinitionList.iterator();
            } else {
                List<? extends ProcedureDefinition> procedureDefinitionList = procedureDefinitionService.getSelectedProcedureDefinitionsFor(searchTerm, PublishedState.REJECTED, getSort().getProperty(), getSort().isAscending(), first, count);
                return procedureDefinitionList.iterator();
            }
        }

        @Override
        public int size() {
            int size = 0;

            if(searchTerm.equalsIgnoreCase("")) {
                Long waitingApprovalsCount = procedureDefinitionService.getRejectedApprovalsCount();
                return waitingApprovalsCount.intValue();
            } else {
                Long waitingApprovalsCount = procedureDefinitionService.getSelectedRejectedApprovalsCount(searchTerm);
                return waitingApprovalsCount.intValue();
            }
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

        public void setSearchTerm(String sTerm) {
            searchTerm = sTerm;
        }
    }

}

