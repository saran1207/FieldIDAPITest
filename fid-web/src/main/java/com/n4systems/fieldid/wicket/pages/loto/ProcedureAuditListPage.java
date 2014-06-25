package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureAuditActionsColumn;
import com.n4systems.fieldid.wicket.components.loto.ProcedureAuditListPanel;
import com.n4systems.fieldid.wicket.data.ProcedureAuditDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import java.util.List;

/**
 * Created by rrana on 2014-06-10.
 */
public class ProcedureAuditListPage extends ProcedureAuditPage implements IAjaxIndicatorAware {

    private ProcedureAuditListPanel procedureDefinitionListPanel;
    private ProcedureAuditDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;

    private String procedureCodeString = null;
    private Asset asset = null;
    private boolean isProcedureCode = false;
    private boolean isAsset = false;

    private String searchTerm = "";
    private String textFilter = null;

    private DateRange dateRange = new DateRange(RangeType.CUSTOM);

    public ProcedureAuditListPage() {
        super();
    }

    public ProcedureAuditListPage(String procedureCodeString, Asset asset, boolean isProcedureCode, boolean isAsset){
        super();
        this.procedureCodeString = procedureCodeString;
        this.asset = asset;
        this.isProcedureCode = isProcedureCode;
        this.isAsset = isAsset;
        this.searchTerm = asset.getDisplayName();
    }

    public ProcedureAuditListPage(String procedureCodeString, Asset asset, boolean isProcedureCode, boolean isAsset, DateRange dateRange) {
        this(procedureCodeString, asset, isProcedureCode, isAsset);
        this.dateRange = dateRange;
    }

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

        final TextField<String> field = new TextField<String>("field", new PropertyModel<String>(this, "searchTerm"));
        form.add(field);

        form.add(new DateRangePicker("dueDateRangePicker", new FIDLabelModel("label.due_date"), new PropertyModel<DateRange>(this, "dateRange"), RangeType.allFloatingButFutureTypes()){
            @Override
            protected void onRangeTypeChanged(AjaxRequestTarget target) {
                if (!getSelectedRangeType().equals(RangeType.CUSTOM)) {
                    clearInput();
                    redoSearch(target);
                }
            }

            @Override
            protected void onFromDateChanged(AjaxRequestTarget target) {
                redoSearch(target);
            }

            @Override
            protected void onToDateChanged(AjaxRequestTarget target) {
                redoSearch(target);
            }

        }.withFormUpdatingBehavior());

        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                redoSearch(target);
            }
        };
        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(new Long(500)));
        field.add(onChangeAjaxBehavior);

        dataProvider = new ProcedureAuditDataProvider("dueDate", SortOrder.ASCENDING, PublishedState.PUBLISHED, procedureCodeString, asset, isProcedureCode, isAsset){
            @Override protected String getTextFilter() {
                return textFilter;
            }
        };

        add(new ContextImage("loadingImage", "images/loading-small.gif"));

        add(procedureDefinitionListPanel = new ProcedureAuditListPanel("procedureDefinitionListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends ProcedureAuditEvent>> columns) {
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends ProcedureAuditEvent>> columns) {
                columns.add(getActionsColumn(this));
            }

            @Override
            protected FIDFeedbackPanel getErrorFeedbackPanel() {
                return feedbackPanel;
            }
        });
        procedureDefinitionListPanel.setOutputMarkupPlaceholderTag(true);
    }

    private void redoSearch(AjaxRequestTarget target) {
        this.dataProvider.setSearchTerm(searchTerm);
        this.dataProvider.setDueDateRange(dateRange);
        this.dataProvider.resetProcedureCodeFlag();
        this.dataProvider.resetAssetCodeFlag();
        target.add(procedureDefinitionListPanel);
    }

    protected AbstractColumn getActionsColumn(ProcedureAuditListPanel procedureDefinitionListPanel) {
        return new ProcedureAuditActionsColumn(procedureDefinitionListPanel);
    }
}

