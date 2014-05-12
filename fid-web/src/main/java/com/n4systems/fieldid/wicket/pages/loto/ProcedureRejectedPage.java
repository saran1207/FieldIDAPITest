package com.n4systems.fieldid.wicket.pages.loto;


import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureApprovalsActionsColumn;
import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureRejectedDateColumn;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
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

    private String procedureCodeString = null;
    private Asset asset = null;
    private boolean isProcedureCode = false;
    private boolean isAsset = false;
    private String searchTerm = "";

    public ProcedureRejectedPage() {super(); }

    public ProcedureRejectedPage(String procedureCodeString, Asset asset, boolean isProcedureCode, boolean isAsset){
        super();
        this.procedureCodeString = procedureCodeString;
        this.asset = asset;
        this.isProcedureCode = isProcedureCode;
        this.isAsset = isAsset;
        this.searchTerm = asset.getDisplayName();
    }

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

        final TextField<String> field = new TextField<String>("field", new Model<String>(searchTerm));
        form.add(field);


        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                ProcedureRejectedPage.this.dataProvider.setSearchTerm(field.getDefaultModelObjectAsString());
                ProcedureRejectedPage.this.dataProvider.resetProcedureCodeFlag();
                ProcedureRejectedPage.this.dataProvider.resetAssetCodeFlag();
                target.add(procedureListPanel);
            }
        };
        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(new Long(500)));
        field.add(onChangeAjaxBehavior);


        dataProvider = new ProcedureDefinitionDataProvider("created", SortOrder.DESCENDING, procedureCodeString, asset, isProcedureCode, isAsset);

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
        private String procedureCode;
        private Asset asset;
        private boolean isProcedureCode;
        private boolean isAsset;


        public ProcedureDefinitionDataProvider(String order, SortOrder sortOrder, String procedureCode, Asset asset, boolean isProcedureCode, boolean isAsset) {
            setSort(order, sortOrder);
            searchTerm = "";
            this.procedureCode = procedureCode;
            this.asset = asset;
            this.isProcedureCode = isProcedureCode;
            this.isAsset = isAsset;
        }

        @Override
        public Iterator<? extends ProcedureDefinition> iterator(int first, int count) {
            if(isAsset || isProcedureCode) {
                List<? extends ProcedureDefinition> procedureDefinitionList = procedureDefinitionService.getSelectedProcedureDefinitionsFor(procedureCode, asset, isAsset, PublishedState.REJECTED, getSort().getProperty(), getSort().isAscending(), first, count);
                return procedureDefinitionList.iterator();
            } else {
                List<? extends ProcedureDefinition> procedureDefinitionList = procedureDefinitionService.getProcedureDefinitionsFor(searchTerm, PublishedState.REJECTED, getSort().getProperty(), getSort().isAscending(), first, count);
                return procedureDefinitionList.iterator();
            }
        }

        @Override
        public int size() {
            if(isAsset || isProcedureCode) {
                Long waitingApprovalsCount = procedureDefinitionService.getSelectedRejectedApprovalsCount(procedureCode, asset, isAsset);
                return waitingApprovalsCount.intValue();
            } else {
                Long waitingApprovalsCount = procedureDefinitionService.getRejectedApprovalsCount(searchTerm);
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

        public void resetProcedureCodeFlag(){
            isProcedureCode = false;
        }

        public void resetAssetCodeFlag(){
            isAsset = false;
        }
    }

}

