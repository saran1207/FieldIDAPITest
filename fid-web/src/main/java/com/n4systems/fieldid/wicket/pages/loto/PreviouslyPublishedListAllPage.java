package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.components.loto.PublishedProcedureActionsColumn;
import com.n4systems.fieldid.wicket.data.ProcedureDefinitionDataProvider;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import java.util.List;

/**
 * Created by rrana on 2014-04-09.
 */
public class PreviouslyPublishedListAllPage extends ProceduresAllListPage implements IAjaxIndicatorAware {

    private ProcedureListPanel procedureDefinitionListPanel;
    private ProcedureDefinitionDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;

    private WebMarkupContainer formContainer;

    private String procedureCodeString = null;
    private Asset asset = null;
    private boolean isProcedureCode = false;

    private String textFilter = null;

    public PreviouslyPublishedListAllPage(){
        super();
    }

    public PreviouslyPublishedListAllPage(String procedureCodeString, Asset asset){
        super();
        this.procedureCodeString = procedureCodeString;
        this.asset = asset;
        this.isProcedureCode = true;
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

        formContainer = new WebMarkupContainer("formContainer"){
            @Override
            public boolean isVisible()
            {
                return !isProcedureCode;
            }
        };

        add(formContainer);

        Form<Void> form = new Form<Void>("form");
        formContainer.add(form);

        final TextField<String> field = new TextField<String>("field", new Model<String>(""));
        form.add(field);


        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                PreviouslyPublishedListAllPage.this.dataProvider.setSearchTerm(field.getDefaultModelObjectAsString());
                target.add(procedureDefinitionListPanel);
            }
        };
        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(new Long(500)));
        field.add(onChangeAjaxBehavior);

        dataProvider = new ProcedureDefinitionDataProvider("created", SortOrder.DESCENDING, PublishedState.PREVIOUSLY_PUBLISHED, procedureCodeString, asset, isProcedureCode){
            @Override protected String getTextFilter() {
                return textFilter;
            }
        };

        formContainer.add(new ContextImage("loadingImage", "images/loading-small.gif"));

        add(procedureDefinitionListPanel = new ProcedureListPanel("procedureDefinitionListPanel", dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {
                columns.add(new PublishedProcedureActionsColumn(this));
            }

            @Override
            protected FIDFeedbackPanel getErrorFeedbackPanel() {
                return feedbackPanel;
            }

        });
        procedureDefinitionListPanel.setOutputMarkupPlaceholderTag(true);

    }

}
